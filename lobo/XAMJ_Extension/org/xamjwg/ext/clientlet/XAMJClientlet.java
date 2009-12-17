/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: xamjadmin@users.sourceforge.net
*/
package org.xamjwg.ext.clientlet;

import java.io.*;
import java.util.*;

import java.awt.Component;
import java.beans.*;
import java.lang.reflect.*;
import java.net.*;

import org.xamjwg.dom.XamjEvaluatable;
import org.xamjwg.ext.*;
import org.xamjwg.ext.domimpl.*;
import org.xmlpull.v1.*;


import org.eclipse.jdt.internal.compiler.batch.*;
import org.lobobrowser.clientlet.*;
import org.lobobrowser.context.*;
import org.lobobrowser.io.ManagedStore;
import org.lobobrowser.request.*;
import org.lobobrowser.store.*;
import org.lobobrowser.ua.NavigatorFrame;
import org.lobobrowser.ua.UserAgent;
import org.lobobrowser.util.*;
import org.lobobrowser.util.io.IORoutines;

import java.util.logging.*;

/**
 * 
 */
public class XAMJClientlet implements Clientlet
{
	private static final Logger logger = Logger.getLogger(XAMJClientlet.class.getName());
    private static final Set SPECIAL_ELEMENTS = new HashSet();
    private static final String COMPILER_CLASS_PATH;
    private static final String ENGINE_CLASS_PATH;
    private static final String BOOT_CLASS_PATH;
    
    static {
    	// Note: SPECIAL_ELEMENTS should only have
    	// to do with code generation. A cached XAMJ clientlet
    	// should not need them.
        SPECIAL_ELEMENTS.add("code");
        SPECIAL_ELEMENTS.add("decl");
        SPECIAL_ELEMENTS.add("import");
        SPECIAL_ELEMENTS.add("implements");
        SPECIAL_ELEMENTS.add("extends");
        SPECIAL_ELEMENTS.add("archive");
        SPECIAL_ELEMENTS.add("minXamjVersion");
        SPECIAL_ELEMENTS.add("minJavaVersion");
        SPECIAL_ELEMENTS.add("document-disposition");
        
        BOOT_CLASS_PATH = System.getProperty("sun.boot.class.path");
        if(BOOT_CLASS_PATH == null) {
        	throw new IllegalStateException("XAMJClientlet expects property sun.boot.class.path to be set!");
        }
    	String classPath = System.getProperty("java.class.path");
    	String pathSeparator = System.getProperty("path.separator");
    	int colonIdx = classPath.indexOf(pathSeparator);
    	String firstEntry = colonIdx == -1 ? classPath : classPath.substring(0, colonIdx);
    	File classPathFile = new File(firstEntry);
        String compilerClassPath = System.getProperty("xamj.clientlet.class.path");
        if(compilerClassPath == null) {
        	File xamjJAR = new File(classPathFile.getParentFile(), "xamj.jar");
        	String xamjClassPath = "xamj.jar";
        	try {
        		xamjClassPath = xamjJAR.getCanonicalPath();
        	} catch(IOException ioe) {
        		logger.log(Level.SEVERE, "<clinit>", ioe);
        	}
    		compilerClassPath = xamjClassPath;
        }
        COMPILER_CLASS_PATH = compilerClassPath;
        String engineClassPath = System.getProperty("xamj.engine.class.path");
        if(engineClassPath == null) {
        	File warriorJAR = new File(classPathFile.getParentFile(), "warrior.jar");
        	try {
        		engineClassPath = warriorJAR.getCanonicalPath();
        	} catch(IOException ioe) {
        		engineClassPath = "warrior.jar";
        		logger.log(Level.SEVERE, "<clinit>", ioe);
        	}
        }
        ENGINE_CLASS_PATH = engineClassPath;
    	if(logger.isLoggable(Level.INFO)) logger.info("<clinit>: Setting Public API classpath as: " + COMPILER_CLASS_PATH);
    }
    
	public XAMJClientlet()
	{
	}
	
	private byte[] loadContent(ClientletResponse response) throws IOException {
		int cl = response.getContentLength();
		return cl == -1 ? IORoutines.load(response.getInputStream()) : IORoutines.loadExact(response.getInputStream(), cl);		
	}

	public void process(ClientletContext context) throws ClientletException 
	{
		ClientletResult cresult = this.generateClientlet(context);
		final ClientletContext wcontext = new ContextWrapper(context);
		cresult.clientlet.process(wcontext);
		ComponentContent baseContent = wcontext.getResultingContent();
		context.setResultingContent(new SimpleComponentContent(baseContent.getComponent(), baseContent.getTitle(), cresult.sourceCode));
	}

	private static class ClientletResult {
		public final Clientlet clientlet;
		public final String sourceCode;
		
		public ClientletResult(final Clientlet clientlet, final String sourceCode) {
			super();
			this.clientlet = clientlet;
			this.sourceCode = sourceCode;
		}		
	}
	
	public ClientletResult generateClientlet(ClientletContext context) throws ClientletException {
		ClientletResponse response = context.getResponse();
		try {
			URL url = response.getResponseURL();
			String className = this.getClassName(url);
			File classFile = this.getXamjFile(url, className, ".class");
			File xamjFile = null;
			Clientlet resultClientlet = null;
			byte[] newContent = null;			
			synchronized(className.intern()) {
				boolean getFromClassFile = false;
				if(classFile.exists()) {
					if(response.isFromCache()) {
						getFromClassFile = true;
					}
					else {
						newContent = this.loadContent(response);
						xamjFile = this.getXamjFile(url, className, ".xamj");
						if(IORoutines.equalContent(xamjFile, newContent)) {
							getFromClassFile = true;
						}
					}
				}
				if(getFromClassFile) {
					IORoutines.touch(classFile);
					File archiveFile = this.getJarsFile(url, className);
					boolean exists = archiveFile.exists();
					if(exists) {
						IORoutines.touch(archiveFile);
					}
					List archiveList = exists ? IORoutines.loadStrings(archiveFile) : null;
					ArchiveCollection archiveCollection = this.loadArchives(context, archiveList);
					resultClientlet = this.getClientletFromClassFile(url, classFile, className, archiveCollection);
				}
				if(newContent == null) {
					newContent = this.loadContent(response);
				}
				//TODO: Right encoding below
				String newContentText = new String(newContent, "UTF-8");
				if(resultClientlet == null) {
					resultClientlet = this.generateClientlet(context, newContent, className, url);
					if(resultClientlet == null) {
						throw new ClientletException("Unable to create generated clienlet");
					}
					if(xamjFile == null) {
						xamjFile = this.getXamjFile(url, className, ".xamj");
					}
					//RULE: .xamj file generated must always match 
					//its .class counterpart.
					IORoutines.save(xamjFile, newContent);
				}
				else {
					try {
						response.getInputStream().close();
					} catch(IOException ioe) {
						// Ignore
					}
				}
				return new ClientletResult(resultClientlet, newContentText);
			}
		} catch(IOException ioe) {
			throw new ClientletException(ioe);
		}		
	}

	protected Clientlet generateClientlet(ClientletContext document, byte[] content, String className, URL url) throws ClientletException, IOException {
		return this.generateClientlet(document, new ByteArrayInputStream(content), className, url);
	}

	protected Clientlet generateClientlet(ClientletContext document, InputStream origIn, String className, URL url) throws ClientletException, IOException
	{
	    if(origIn == null) {
	        throw new IllegalArgumentException("origIn is null");
	    }
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new InputStreamReader(origIn));
			// Entity definitions must go after setInput.
			xpp.defineEntityReplacementText("nbsp", "\u00A0");
			return this.generateClientlet(document, xpp, className, url);
		}
		catch(XmlPullParserException xppe) {
			throw new XAMJParseException(xppe);
		}
		catch(org.xamjwg.dom.XamjException xe) {
			throw new ClientletException(xe);
		}
		catch(IOException ioe) {
		    throw new ClientletException(ioe);
		} finally {
		    origIn.close();
		}
	}
	
	protected String getClassName(URL url) throws IOException {
	    String fileName = url.getFile();
	    String urlText = url.toExternalForm();
	    int lastSlashIdx = fileName.lastIndexOf('/');
	    String simpleName = lastSlashIdx == -1 ? fileName : fileName.substring(lastSlashIdx+1);
	    String normalizedName = Strings.getJavaIdentifier(simpleName);
	    String hash = Strings.getHash32(urlText);
	    return normalizedName + "_" + hash; 
	}

	protected Clientlet generateClientlet(ClientletContext context,  XmlPullParser parser, String className, URL url) throws ClientletException, org.xamjwg.dom.XamjException, XmlPullParserException, IOException {
	    TreeBuildContext cc = new TreeBuildContext(context, parser, className);
	    generateTreeSource(cc);
	    ArchiveCollection archiveCollection = this.loadArchives(context, cc);
	    SourceInfo sourceInfo = this.generateFullSource(cc);
	    context.getClientletFrame().updateProgress(url, context.getResponse().getLastRequestMethod(), org.lobobrowser.ua.ProgressType.BUILDING, 0, -1);
	    Clientlet clienlet = compile(sourceInfo, url, archiveCollection);
	    return clienlet;
	}

	private ArchiveCollection loadArchives(ClientletContext document, TreeBuildContext cc) throws ClientletException {
		return this.loadArchives(document, cc.archiveList);
	}
		
	/**
	 * Returns a collection of ArchiveInfo instances.
	 */
	private ArchiveCollection loadArchives(ClientletContext context, List archiveUrls) throws ClientletException {
		if(archiveUrls.isEmpty()) {
			return null;
		}
		else {
			Set archiveKey = new HashSet();
			Iterator i = archiveUrls.iterator();
			while(i.hasNext()) {
				Object item = i.next();
				if(item instanceof ParsedText) {
					archiveKey.add(((ParsedText) item).text);
				}
				else {
					archiveKey.add(item);
				}
			}
			Map archiveCache = MemoryCacheManager.getInstance().getArchiveCache();
			ArchiveCollection archiveCollection = (ArchiveCollection) archiveCache.get(archiveKey);
			if(archiveCollection == null) {
				Collection archiveInfos = new LinkedList();
				Iterator i2 = archiveKey.iterator();
				while(i2.hasNext()) {
					Object urlObj = i2.next();
					String archiveRelUrl = (String) urlObj;
					try {
						URL docUrl = context.getResponse().getResponseURL();
						URL absUrl = RequestEngine.createURL(docUrl, archiveRelUrl);						
						ArchiveRequestHandler arh = new ArchiveRequestHandler(absUrl, context.getClientletFrame());
						RequestEngine.getInstance().inlineRequest(arh);
						File jarFile = arh.getJarFile();
						if(jarFile == null) {
							throw new IllegalStateException("No file resulted from inline request to " + arh.getLatestRequestURL() + ". Document url is " + docUrl);
						}
						ArchiveInfo ai = new ArchiveInfo(absUrl, jarFile);
						archiveInfos.add(ai);
					} catch(IllegalStateException ise) {
						throw ise;
					} catch(Exception err) {
						throw new IllegalStateException(err);
					}		
				}
				archiveCollection = new ArchiveCollection(archiveInfos);
			}
			return archiveCollection;
		}
	}
	
	protected void generateTreeSource(TreeBuildContext cc) throws ClientletException, org.xamjwg.dom.XamjException,  XmlPullParserException, IOException
	{
	    XmlPullParser xpp = cc.getXmlPullParser();
		int eventType;
		for(; (eventType = xpp.getEventType()) != XmlPullParser.END_DOCUMENT; xpp.next())
		{
			if(eventType == XmlPullParser.START_TAG) 
			{
				generateForRoot(cc);
				return;
			}
		}
		throw new XAMJParseException("Document missing root element");
	}

	protected void generateForRoot(TreeBuildContext cc) throws ClientletException, org.xamjwg.dom.XamjException, XmlPullParserException, IOException {
		this.generateForElement(cc, null);
	}
	
	protected void generateForElement(TreeBuildContext cc, String parentID) throws ClientletException, org.xamjwg.dom.XamjException, XmlPullParserException, IOException
	{
		XmlPullParser xpp = cc.getXmlPullParser();
		String elementName = xpp.getName();
		String id = xpp.getAttributeValue(null, "id");
		String localIdentifier = cc.generateJavaID();
		String instanceIdentifier = null;
		if(!Strings.isBlank(id)) 
		{
			if(!Strings.isJavaIdentifier(id)) 
			{
				throw new XAMJParseException(xpp, "Element id='" + id + "' is not a valid identifier");
			}
			instanceIdentifier = id;
		}	
		ElementDescriptor ed = ElementFactory.getInstance().getElementDescriptor(elementName);
		if(ed != null) {
			String interName = ed.getElementInterfaceName();
			cc.print("final "); cc.print(interName); cc.print(" "); cc.print(localIdentifier);
			cc.print("=");
			if(!"XElement".equals(interName)) {
				// Add cast to interface
				cc.print("("); cc.print(interName); cc.print(")");
			}
			cc.print("document.createElement(\""); cc.print(elementName); cc.println("\");");
			if(instanceIdentifier != null) {
				cc.registerElement(new ElementInfo(interName, elementName, instanceIdentifier, parentID == null));
				cc.print(instanceIdentifier); cc.print("="); cc.print(localIdentifier); cc.println(";");
			}
			this.generateAttributeAssignments(localIdentifier, elementName, cc);
			if(parentID != null) {
				// NOTE: Append child before processing element so that
				// it knows its parent element to begin with.
				cc.print(parentID); cc.print(".appendChild("); cc.print(localIdentifier); cc.println(");");
			}
		}
		else {
			cc.print("final XElement "); cc.print(localIdentifier);	cc.println("= null;");
			cc.print("if("); cc.print(localIdentifier); cc.print("==null) {");
			cc.print("throw new ClientletException(\"Element named '"); cc.print(elementName); cc.println("' unknown\");");
			cc.println("}");
		}
		if(SPECIAL_ELEMENTS.contains(elementName)) {
		    cc.processSpecialElement();
		}
		else {
			cc.println("{");
			generateForRestOfElement(cc, localIdentifier, ed);
			cc.println("}");
		}
		if(parentID == null) {
			cc.print  ("document.setDocumentElement("); cc.print(localIdentifier); cc.println(");");
			cc.println("if(request.isNewWindowRequest()) {");
			cc.println("    context.setWindowProperties(document.getWindowProperties());");
			cc.println("}");
			cc.println("context.setResultingContent(document.createClientletContent(context));");
		}
	}
	
	private void generateAttributeAssignments(String javaName, String elementName, TreeBuildContext cc) throws ClientletException
	{
        XmlPullParser xpp = cc.getXmlPullParser();
	    try {
	        int acount = xpp.getAttributeCount();
	        ElementDescriptor elementDescriptor = ElementFactory.getInstance().getElementDescriptor(elementName);
	        if(elementDescriptor == null) {
	            throw new XAMJParseException(cc.getXmlPullParser(), "Element named '" + elementName + "' unknown");
	        }
	        Class elementClass = elementDescriptor.getElementClass();
	        for(int i = 0; i < acount; i++) {
	            String aname = xpp.getAttributeName(i);
                String avalue = xpp.getAttributeValue(i);
                this.generateAttributeAssignment(cc, aname, avalue, elementClass, javaName);
	        }		
	        cc.println("");
	    } catch(ClientletException ce) {
	        throw ce;
	    }
	    catch(Exception err) {
	        throw new XAMJParseException(xpp, err);
	    }
	}

	protected void generateAttributeAssignment(TreeBuildContext cc, String aname, String avalue, Class targetClass, String javaName) throws ClientletException {
	    try {
	        Bean bean = new Bean(targetClass);
	        int idx = aname.indexOf('.');
	        if(idx == -1) {
	            PropertyDescriptor pinfo = bean.getPropertyDescriptor(aname);
	            if(pinfo == null) {
	                throw new XAMJParseException(cc.xpp, "Property named '" + aname + "' does not exist in class '" + targetClass.getName() + "'");
	            }
	            java.lang.reflect.Method writeMethod = pinfo.getWriteMethod();
	            if(writeMethod == null) {
	            	throw new XAMJParseException(cc.xpp, "Property named '" + aname + "' is not settable");
	            }
	            String javaValue = this.getJavaValue(cc, pinfo, avalue);
	            cc.print(javaName); cc.print("."); cc.print(writeMethod.getName()); cc.print("("); cc.print(javaValue); cc.print(");");
	        }
	        else {
	            String prefix = aname.substring(0, idx);
	            PropertyDescriptor pinfo = bean.getPropertyDescriptor(prefix); 
	            if(pinfo == null) {
	                throw new XAMJParseException(cc.xpp, "Property named '" + prefix + "' does not exist in class '" + targetClass.getName() + "'");
	            }
	            String newJavaName = javaName + "." + pinfo.getReadMethod().getName() + "()";
	            String arest = aname.substring(idx+1);
	            this.generateAttributeAssignment(cc, arest, avalue, pinfo.getPropertyType(), newJavaName);
	        }
	    } catch(IntrospectionException ie) {
	        throw new XAMJParseException(cc.getXmlPullParser(), ie);
	    }
	}
	
	protected void generateForRestOfElement(TreeBuildContext cc, String elementID, ElementDescriptor elementDescriptor) throws XmlPullParserException, ClientletException, IOException
	{
	    try {
	        XmlPullParser xpp = cc.getXmlPullParser();
	        int eventType;
	        for(xpp.next(); (eventType = xpp.getEventType()) != XmlPullParser.END_DOCUMENT; xpp.next())
	        {
	            switch(eventType) 
	            {
	            case XmlPullParser.START_TAG:
	                this.generateForElement(cc, elementID);
	            break;
	            case XmlPullParser.END_TAG:
	                return;
	            case XmlPullParser.CDSECT:
	                if(elementDescriptor.isActionContainer()) {
	                    this.generateActionScript(cc, elementID, xpp.getText(), elementDescriptor);
	                }
	                else {
	                    this.generateForCData(cc, elementID, xpp.getText());
	                }
	                break;
	            case XmlPullParser.TEXT:
	                String text = xpp.getText();
	            	if(!"".equals(text)) {
	            		if(elementDescriptor.isActionContainer()) {
	            			String ttext = text.trim();
	            			if(!"".equals(ttext)) {
	            				this.generateActionScript(cc, elementID, ttext, elementDescriptor);
	            			}
	            		}
	            		else {
	            			this.generateForText(cc, elementID, text);
	            		}
	            	}
	            	break;
	            case XmlPullParser.COMMENT:
	                this.generateForComment(cc, elementID, xpp.getText());
	                break;
	            }               
	        }
	        throw new XAMJParseException(xpp, "End tag not found");
	    } catch(ClientletException ce) {
	        throw ce;
	    } catch(IOException ioe) {
	        throw ioe;
	    } catch(Exception err) {
	        throw new XAMJParseException(cc.getXmlPullParser(), err);
	    }
	}

	private void generateActionScript(TreeBuildContext tbc, String elementID, String text, ElementDescriptor descriptor) throws XAMJParseException {
		String eventType = descriptor.getEventTypeName();
	    tbc.print(elementID); 
	    tbc.print(".appendChild(document.createActionElement(new XamjListener<"); tbc.print(eventType); tbc.println(">() {");
	    tbc.indent();
	    tbc.print("public void execute(final "); tbc.print(eventType); tbc.print(" event) throws Exception {");
	    tbc.indent();
	    tbc.println(text);
	    tbc.unindent();
	    tbc.println("}");
	    tbc.unindent();
	    tbc.println("}));");
	}
	
	private void generateForText(TreeBuildContext tbc, String elementID, String text) throws XAMJParseException {
	    ValueInfo vinfo = this.getRawJavaValue(tbc, text);
	    String stringLiteral = vinfo.value;
	    if(vinfo.onlyLiteral) {
	        stringLiteral = Strings.getJavaStringLiteral(vinfo.value);
	    }
	    tbc.print(elementID); tbc.print(".appendChild(document.createTextNode(");
	    tbc.print(stringLiteral);
	    tbc.println("));");
	}
	
	private void generateForCData(TreeBuildContext tbc, String elementID, String text) throws XAMJParseException {
        String stringLiteral = Strings.getJavaStringLiteral(text);
	    tbc.print(elementID); tbc.print(".appendChild(document.createCDataSection(");
	    tbc.print(stringLiteral);
	    tbc.println("));");
	}
	
	private void generateForComment(TreeBuildContext tbc, String elementID, String text) throws XAMJParseException {
        String stringLiteral = Strings.getJavaStringLiteral(text);
	    tbc.print(elementID); tbc.print(".appendChild(document.createComment(");
	    tbc.print(stringLiteral);
	    tbc.println("));");
	}
	
	private String getAsEvaluatable(TreeBuildContext tbc, String javaExpression, Class valueType) throws XAMJParseException {
		if(valueType == null || Object.class == valueType) {
			return "new XamjEvaluatable() {\r\n" +
			"    public Object evaluate() throws Exception {\r\n" +
			"        return " + javaExpression + ";\r\n" +
			"    }\r\n" +
			"}";
		}
		else {
			String valueClassName = valueType.getName();
			return "new XamjEvaluatable<" + valueClassName + ">() {\r\n" +
			"    public " + valueClassName + " evaluate() throws Exception {\r\n" +
			"        return " + javaExpression + ";\r\n" +
			"    }\r\n" +
			"}";			
		}
	}

	private String getJavaValue(TreeBuildContext tbc, PropertyDescriptor pd, String avalue) throws XAMJParseException {
		Class ptype = pd.getPropertyType();
		if(ptype == String.class) {
			return this.getNonEvaluatableJavaValue(tbc, ptype, avalue);
		}
		if(XamjEvaluatable.class.isAssignableFrom(ptype)) {
			java.lang.reflect.Method writeMethod = pd.getWriteMethod();
			java.lang.reflect.Type[] paramTypes = writeMethod.getGenericParameterTypes();
			java.lang.reflect.Type gtype = paramTypes[0];
			return this.getXamjEvaluatableValue(tbc, ptype, gtype, avalue);
		}
		else {
			return this.getNonEvaluatableJavaValue(tbc, ptype, avalue);
		}
	}

	private String getXamjEvaluatableValue(TreeBuildContext tbc, Class ptype, Type genericType, String avalue) throws XAMJParseException {
	    ValueInfo valueInfo = this.getRawJavaValue(tbc, avalue);		
	    return this.getXamjEvaluatableValue(tbc, ptype, genericType, valueInfo);
	}

	private String getXamjEvaluatableValue(TreeBuildContext tbc, Class ptype, Type genericType, ValueInfo valueInfo) throws XAMJParseException {
    	Type valueType;
    	if(genericType instanceof ParameterizedType) {
    		ParameterizedType pt = (ParameterizedType) genericType;
    		Type[] targs = pt.getActualTypeArguments();
    		valueType = targs[0];
    	}
    	else {
    		valueType = Object.class;
    	}
	    if(valueInfo.onlyLiteral) {
        	return this.getAsEvaluatable(tbc, this.getNonEvaluatableJavaValue(tbc, (Class) valueType, valueInfo), (Class) valueType);
	    }
	    else {
        	return this.getAsEvaluatable(tbc, valueInfo.value, (Class) valueType);
	    }		
	}
//
//	private String getJavaValue(TreeBuildContext tbc, Class ptype, Type genericType, ValueInfo valueInfo) throws XAMJParseException {
//		if(ptype == String.class) {
//			return this.getNonEvaluatableJavaValue(tbc, ptype, valueInfo);
//		}
//		if(XamjEvaluatable.class.isAssignableFrom(ptype)) {
//			return this.getXamjEvaluatableValue(tbc, ptype, genericType, valueInfo);
//		}
//		else {
//			return this.getNonEvaluatableJavaValue(tbc, ptype, valueInfo);
//		}		
//	}
//	
	private String getNonEvaluatableJavaValue(TreeBuildContext tbc, Class ptype, String avalue) throws XAMJParseException {
	    ValueInfo valueInfo = this.getRawJavaValue(tbc, avalue);
	    return this.getNonEvaluatableJavaValue(tbc, ptype, valueInfo);
	}
	
	private String getNonEvaluatableJavaValue(TreeBuildContext tbc, Class ptype, ValueInfo valueInfo) throws XAMJParseException {
	    if(valueInfo.onlyLiteral) {
	        if(ptype.isAssignableFrom(String.class)) {
	        	// Includes Object
	            return Strings.getJavaStringLiteral(valueInfo.value);
	        }
	        else if(ptype == char.class || ptype == Character.class) {
	        	return "'" + valueInfo.value + "'";
	        }
	        else if(ptype.isPrimitive() || Objects.isBoxClass(ptype)) {
		        return valueInfo.value;
		    }
	        else {
	        	return "(" + ptype.getName() + ") " + valueInfo.value;
	            //return this.getAsValueOf(tbc, ptype, valueInfo.value, false); 
	        }
	    }
	    else {
	    	return "(" + ptype.getName() + ") " + valueInfo.value;
//		    if(ptype.isPrimitive() || ptype.isAssignableFrom(String.class)) {
//		    	// Includes properties of type Object
//		        return valueInfo.value;
//		    }
//		    else {
//	            return this.getAsValueOf(tbc, ptype, "String.valueOf(" + valueInfo.value + ")", true); 
//	        }
	    }
	}

//	private String getAsValueOf(TreeBuildContext tbc, Class ptype, String expression, boolean exprIsString) throws XAMJParseException {
//		// TODO Revise based on decision to use 1.5
//	    String boxName;
//	    String actualExpr = expression;
//	    if(ptype.isAssignableFrom(Integer.class)) {
//	        boxName = "Integer";
//	        if(!exprIsString) {
//	        	actualExpr = "\"" + expression + "\"";
//	        }
//	    }
//	    else if(ptype.isAssignableFrom(Boolean.class)) {
//	        boxName = "Boolean";
//	    }
//	    else if(ptype.isAssignableFrom(Byte.class)) {
//	        boxName = "Byte";
//	        if(!exprIsString) {
//	        	actualExpr = "\"" + expression + "\"";
//	        }
//	    }
//	    else if(ptype.isAssignableFrom(Character.class)) {
//	        boxName = "Character";
//	        if(!exprIsString) {
//	        	actualExpr = "'" + expression + "'";
//	        }
//	    }
//	    else if(ptype.isAssignableFrom(Double.class)) {
//	        boxName = "Double";
//	        if(!exprIsString) {
//	        	actualExpr = "\"" + expression + "\"";
//	        }
//	    }
//	    else if(ptype.isAssignableFrom(Float.class)) {
//	        boxName = "Float";
//	        if(!exprIsString) {
//	        	actualExpr = "\"" + expression + "\"";
//	        }
//	    }
//	    else if(ptype.isAssignableFrom(Long.class)) {
//	        boxName = "Long";
//	        if(!exprIsString) {
//	        	actualExpr = "\"" + expression + "\"";
//	        }
//	    }
//	    else if(ptype.isAssignableFrom(Short.class)) {
//	        boxName = "Short";
//	        if(!exprIsString) {
//	        	actualExpr = "\"" + expression + "\"";
//	        }
//	    }
//	    else {
//	        throw new XAMJParseException(tbc.getXmlPullParser(), "Properties of type " + ptype.getName() + " unsupported"); 
//	    }
//	    return boxName + ".valueOf(" + actualExpr + ")";
//	}

	private static final String SCRIPTLET_PREF = "(%";
	private static final String SCRIPTLET_SUFF = "%)";
	private static final int SCRIPTLET_PREF_LENGTH = SCRIPTLET_PREF.length();
	private static final int SCRIPTLET_SUFF_LENGTH = SCRIPTLET_SUFF.length();
	
	public ValueInfo getRawJavaValue(TreeBuildContext tbc, String avalue) throws XAMJParseException 
	{
		String result = null;
		StringBuffer sb = null;
		int pivotIdx = 0;
		for(;;) 
		{
			int startIdx = avalue.indexOf(SCRIPTLET_PREF, pivotIdx);
			if(startIdx == -1) 
			{
				if(pivotIdx == 0) 
				{
					result = avalue;
				}
				else 
				{
				    String rest = avalue.substring(pivotIdx);
				    if(!"".equals(rest)) {
				        sb.append("+");
				        sb.append(Strings.getJavaStringLiteral(rest));
				    }
				}
				break;
			}
			int endIdx = avalue.indexOf(SCRIPTLET_SUFF, startIdx + 2);
			if(endIdx == -1) 
			{
			    throw new XAMJParseException(tbc.getXmlPullParser(), "Incomplete expression scriptlet"); 
			}
			String before = avalue.substring(pivotIdx, startIdx); 
			if(!"".equals(before)) {
			    if(sb == null) {
			        sb = new StringBuffer();
			    }
			    else {
			        sb.append("+");
			    }
			    sb.append(Strings.getJavaStringLiteral(before));			
			}
			int selectIdx = startIdx + SCRIPTLET_PREF_LENGTH;
			char selectChar = avalue.charAt(selectIdx);
			String rawExpression = avalue.substring(selectIdx + 1, endIdx);
			String expression;
			switch(selectChar) {
				case '=':
					expression = rawExpression;
					break;
				case ':':
					expression = "(" + rawExpression + ").getElementValue()";
					break;
				case '#':
					expression = "java.net.URLEncoder.encode(String.valueOf((" + rawExpression + ").getElementValue()), \"UTF-8\")";
					break;
				default:
					throw new XAMJParseException(tbc.getXmlPullParser(), "Invalid expression or value scriptlet character. Expecting colon, pound or equals. Got '" + selectChar + "'.");
			}
			if(sb == null) 
			{
				sb = new StringBuffer();
			}
			else {
			    sb.append("+");
			}
			sb.append("(");
			sb.append(expression);
			sb.append(")");
			pivotIdx = endIdx + SCRIPTLET_SUFF_LENGTH;
		}
		boolean onlyLiteral = result != null;
		if(!onlyLiteral) 
		{
			result = sb.toString();
		}
		return new ValueInfo(result, onlyLiteral);
	}

	
	private static final String JAVA_ID_PREFIX = "xj__";

	private SourceInfo generateFullSource(TreeBuildContext tbc) throws ClientletException {
	    StringWriter sw = new StringWriter();
	    PrintWriter out = new PrintWriter(sw);

	    // Write out imports
	    // No package so that file is compiled in place
	    out.println("import org.xamjwg.dom.*;");
	    out.println("import org.xamjwg.event.*;");
	    out.println("import org.lobobrowser.clientlet.*;");
	    out.println("import org.lobobrowser.io.*;");
	    Collection importsCollection = tbc.getImports();
	    Iterator imports = importsCollection.iterator();
	    while(imports.hasNext()) {
	        String anImport = ((ParsedText) imports.next()).text;
	        out.print("import "); out.print(anImport); out.println(";");
	    }
	    
	    // Write out class declaration
	    String className = tbc.getClassName();
	    out.print("public class "); out.print(className);
	    ParsedText extpt = tbc.getExtends();
	    if(extpt != null) {
	        out.print(" extends ");
	        out.print(extpt.text);
	    }
        out.print(" implements ");
	    Collection impl = tbc.getImplements();
        Iterator i = impl.iterator();
        while(i.hasNext()) {
            String anImpl = ((ParsedText) i.next()).text;
            out.print(anImpl);
            out.print(",");
	    }
        out.println("Clientlet {");

        // Write out decls
        Collection decls = tbc.getDecls();
	    Iterator declIterator = decls.iterator();
	    while(declIterator.hasNext()) {
	        String aDecl = ((ParsedText) declIterator.next()).text;
	        out.println(aDecl);
	    }

	    // Write out element fields
	    Collection elementInfos = tbc.getElementInfos();
	    Iterator elementIterator = elementInfos.iterator();
	    while(elementIterator.hasNext()) {
	        ElementInfo einfo = (ElementInfo) elementIterator.next();
	        out.print("protected ");
	        out.print(einfo.interfaceName); out.print(" "); 
	        out.print(einfo.elementID); out.println(";");
	    }
	    
	    // Write out clientlet method
	    out.println("public void process(final ClientletContext context) throws ClientletException {");
	    out.println("try {");
	    out.println("    DocumentBuilder xj__db = DocumentBuilder.newInstance();");
	    out.println("    final XDocument document = xj__db.createDocument(context);");

	    // Write out version checks
	    this.checkMinJavaVersion(tbc, out);
	    
	    //TODO: XAMJ version is no longer checked because of decoupling of XAMJ with Lobo.
	    //this.checkMinXamjVersion(tbc, out);
	    
	    // Write out document disposition checking
		String docDisp = tbc.getDocumentDisposition();
		if("auto".equals(docDisp)) {
			// ignore
		}
		else if(docDisp == null || "frame".equals(docDisp)) {
			//NOP 
			
//			out.println("if(!context.isOnFrame()) {");
//			out.println("  context.openInFrame(context.getResponse().getResponseURL());");
//			out.println("  throw new CancelClientletException(\"open in frame\");");
//			out.println("}");
		}
		else if("window".equals(docDisp)) {
			out.println("if(!request.isNewWindowRequest() && request.isGetRequest()) {");
			// Only works for GET requests. POST is already done.
			out.println("  context.getClientletFrame().open(context.getResponse().getResponseURL());");
			out.println("  throw new CancelClientletException(\"open in window\");");
			out.println("}");
		}
		else {
			throw new ClientletException("Bad document-disposition value: " + docDisp);
		}
	    
	    // Write out DOM creation source code	    
	    out.println(tbc.getSource());
	    
	    // Wrap up
	    out.println("} catch(ClientletException xj__cerr) {");
	    out.println("throw xj__cerr;");
	    out.println("} catch(Throwable xj__err) {");
	    out.println("throw new ClientletException(xj__err);");
	    out.println("}");
	    // Close method
	    out.println("}");
	    // Close class
	    out.println("}");
	    out.flush();
	    //TODO: Map line numbers
	    return new SourceInfo(className, sw.toString());
	}

	private void checkMinXamjVersion(TreeBuildContext tbc, PrintWriter out) {
		ParsedText mxv = tbc.minXamjVersion;
		if(mxv == null) {
			return;
		}
		String required = mxv.text;
		out.println("String xj__version = context.getUserAgent().getXamjVersion();");
		out.print("if(xj__version.compareTo(\""); out.print(required); out.println("\") < 0) {");
		out.print("  throw new XamjVersionException(\"Requires "); out.print(required); out.println("\");");
		out.println("}");
	}

	private void checkMinJavaVersion(TreeBuildContext tbc, PrintWriter out) {
		ParsedText mjv = tbc.minJavaVersion;
		if(mjv == null) {
			return;
		}
		String required = mjv.text;
		out.println("String xj__version = context.getUserAgent().getJavaVersion();");
		out.print("if(xj__version.compareTo(\""); out.print(required); out.println("\") < 0) {");
		out.print("  throw new JavaVersionException(\"Requires "); out.print(required); out.println("\");");
		out.println("}");
	}
	
	private File getXamjFile(URL url, String className, String extension) throws IOException {
        StorageManager sm = StorageManager.getInstance();
        String fileName = className + extension; 
        String host = url.getHost();
        return sm.getXamjCacheFile(host, fileName);		
	}
	
	private File getJarsFile(URL url, String className) throws IOException {
		return this.getXamjFile(url, className, ".jars");
	}
	
	private Clientlet compile(SourceInfo sourceInfo, URL url, ArchiveCollection archiveCollection) throws ClientletException {
	    try {
	        String className = sourceInfo.getClassName();
	        File file = this.getXamjFile(url, className, ".java");
	        FileOutputStream out = new FileOutputStream(file);
	        try {
	            Writer writer = new OutputStreamWriter(out);
	            writer.write(sourceInfo.source);
	            writer.flush();
	        } finally {
	            out.close();
	        }
	        String classPath = COMPILER_CLASS_PATH;
	        String pathSeparator = System.getProperty("path.separator");
	        Collection urls = new LinkedList();
	        if(archiveCollection != null) {
	        	Iterator i = archiveCollection.iterator();
	        	while(i.hasNext()) {
	        		ArchiveInfo ainfo = (ArchiveInfo) i.next();
	        		classPath += pathSeparator;
	        		classPath += ainfo.file.getAbsolutePath();
	        		urls.add(ainfo.url.toExternalForm());
	        	}
	        }
	        if("res".equalsIgnoreCase(url.getProtocol())) {
	        	classPath += pathSeparator;
	        	classPath += ENGINE_CLASS_PATH;
	        }
	        String[] args = new String[] { 
	        		"-1.5",
	        		"-cp", classPath,
					"-bootclasspath", BOOT_CLASS_PATH,
	        		file.getAbsolutePath() };
	        StringWriter errorContent = new StringWriter();
	        StringWriter outputContent = new StringWriter();
	        PrintWriter compilerOut = new PrintWriter(outputContent);
	        PrintWriter compilerErr = new PrintWriter(errorContent);
	        Main compilerMain = new Main(compilerOut, compilerErr, false);
	        boolean success;
	        long time1 = System.currentTimeMillis();
	        try {
	        	success = compilerMain.compile(args);
	        } finally {
	        	if(logger.isLoggable(Level.INFO)){
	        		long time2 = System.currentTimeMillis();
	        		logger.info("compile(): Took " + (time2 - time1) + "ms to compile " + file.length() + " bytes.");
	        	}
	        }
	        compilerOut.flush();
	        compilerErr.flush();
	        if(!success) {
	        	logger.log(Level.SEVERE, "compile(): Compiler errors.\r\n--Output--\r\n" + outputContent.toString() +
	        			"\r\n--Error--\r\n" + errorContent.toString());
	            throw new ClientletException("Java compilation errors:\r\n" + errorContent.toString());
	        }
	        
	        // Create loaders list file
	        File jarsFile = this.getJarsFile(url, className);
	        IORoutines.saveStrings(jarsFile, urls);
	        
	        // Create class loder for this class
	        ClassLoader parentLoader = archiveCollection == null ? this.getClass().getClassLoader() : archiveCollection.getClassLoader();	        
	        ClassLoader loader = new XAMJClassLoader(parentLoader, url, file.getParent());
	        Class clazz = loader.loadClass(className);
	        return (Clientlet) clazz.newInstance();
	    }
	    catch(ClassNotFoundException cnf) {
	        throw new ClientletException("Fatal: generated class not found!", cnf); 
	    }
	    catch(InstantiationException ie) {
	        throw new ClientletException("Fatal: generated class does not have default constructor!", ie); 
	    }
	    catch(IllegalAccessException iae) {
	        throw new ClientletException("Fatal: " + iae.getMessage(), iae); 
	    }
	    catch(IOException ioe) {
	        throw new ClientletException(ioe);
	    }
	}
	
	private Clientlet getClientletFromClassFile(URL url, File classFile, String className, ArchiveCollection archiveCollection) throws ClientletException {
	    try {
	        ClassLoader parentLoader = archiveCollection == null ? this.getClass().getClassLoader() : archiveCollection.getClassLoader();	        
	        ClassLoader loader = new XAMJClassLoader(parentLoader, url, classFile.getParent());
	        Class clazz = loader.loadClass(className);
	        return (Clientlet) clazz.newInstance();
	    }
	    catch(ClassNotFoundException cnf) {
	        throw new ClientletException("Fatal: generated class not found!", cnf); 
	    }
	    catch(InstantiationException ie) {
	        throw new ClientletException("Fatal: generated class does not have default constructor!", ie); 
	    }
	    catch(IllegalAccessException iae) {
	        throw new ClientletException("Fatal: " + iae.getMessage(), iae); 
	    }
	    catch(IOException ioe) {
	    	throw new ClientletException("I/O Error: " + ioe.getMessage(), ioe);
	    }
	}
	
	private class TreeBuildContext 
	{
		private final XmlPullParser xpp;
		private final String className;
		
		private final Set idRegistry = new HashSet();
		private final Collection elements = new LinkedList();
		private final ArrayList javaLineToXML = new ArrayList();

		private int javaLine = 1;
		private int javaIDCounter = 0;
		private String indentation = "";

		private static final String INDENT = "  ";
		
		private final PrintWriter out;
		private final StringWriter sw;
		//private final BaseClientletContext context;
		
		public TreeBuildContext(ClientletContext context, XmlPullParser xpp, String className) 
		{
			this.xpp = xpp;
			//this.context = context;
			this.className = className;
			this.sw = new StringWriter();
			this.out = new PrintWriter(this.sw);
		}

		public String getClassName() {
		    return this.className;
		}
		
		public void flush() {
		    this.out.flush();
		}
		
		public String getSource() {
		    this.out.flush();
		    return this.sw.toString();
		}
		
		public void indent() 
		{
			this.indentation += INDENT;
			this.out.print(this.indentation);
		}

		public void unindent() 
		{
			this.indentation = this.indentation.substring(0, Math.max(this.indentation.length() - INDENT.length(), 0));
		}

		public String generateJavaID() 
		{
			return JAVA_ID_PREFIX + (this.javaIDCounter++);
		}

		public void println(String text) 
		{
			this.out.println(text);
			this.out.print(this.indentation);
			int lineCount = Strings.countLines(text);
			int current = this.javaLine;
			for(int i = 0; i < lineCount; i++) 
			{
				int jl = current + i;
				while(this.javaLineToXML.size() <= jl) {
				    this.javaLineToXML.add(null);
				}
				this.javaLineToXML.set(jl, new Integer(this.xpp.getLineNumber()));
			}
			this.javaLine = current + lineCount;
		}

		public void print(String text) 
		{
			this.out.print(text);
			final int lineCount = Strings.countLines(text);
			if(lineCount > 1) 
			{
				int current = this.javaLine;
				for(int i = 0; i < (lineCount - 1); i++) 
				{
					int jl = current + i;
					while(this.javaLineToXML.size() <= jl) {
					    this.javaLineToXML.add(null);
					}
					this.javaLineToXML.set(jl, new Integer(this.xpp.getLineNumber()));
				}
				this.javaLine = current + (lineCount - 1);
			}
		}

		public void registerElement(ElementInfo einfo) throws XAMJParseException 
		{
		    String id = einfo.elementID;
			if(this.idRegistry.contains(id)) 
			{
				throw new XAMJParseException(this.xpp, "Identifier id='" + id + "' is already defined");
			}
			this.idRegistry.add(id);
			this.elements.add(einfo);
		}

		public XmlPullParser getXmlPullParser() 
		{
			return this.xpp;
		}
		
		public Collection getElementInfos() {
		    return this.elements;
		}
		
		public void processElementWithMacro(String elementID, String macroID) throws ClientletException, XmlPullParserException, IOException {
			XmlPullParser xpp = this.xpp;
			if(!this.idRegistry.contains(macroID)) {
				throw new XAMJParseException(xpp, "There is no prior element with id '" + macroID + "'");
			}
			this.print(elementID);
			this.print(".replaceChildNodes((XMacroElement) ");
			this.print(macroID);
			this.println(");");			
			
			String elementName = xpp.getName();
			int eventType;
			OUTER:
				for(xpp.next(); (eventType = xpp.getEventType()) != XmlPullParser.END_DOCUMENT; xpp.next())
				{
					switch(eventType) 
					{
					case XmlPullParser.TEXT:
					case XmlPullParser.CDSECT:
					case XmlPullParser.START_TAG:
						throw new XAMJParseException(this.xpp, "Element named '" + elementName + "' with macro attribute cannot have children");
					case XmlPullParser.END_TAG:
						break OUTER;
					case XmlPullParser.COMMENT:
						break;
					}               
				}
			if(eventType != XmlPullParser.END_TAG) {
				throw new XAMJParseException(xpp, "Element named '" + elementName + "' is unclosed (end it with />)");
			}
			
		}
		
		private final Collection importsList = new LinkedList();
		private final Collection implementsList = new LinkedList();
		private final List archiveList = new LinkedList();
		private ParsedText extendsName = null;
		private ParsedText documentDisposition = null;
		private ParsedText minJavaVersion = null;
		private ParsedText minXamjVersion = null;
		
		public String getDocumentDisposition() {
			ParsedText pt = this.documentDisposition;
			return pt == null ? null : pt.text;
		}
		
		public void processSpecialElement() throws ClientletException, XmlPullParserException, IOException {
		    XmlPullParser xpp = this.xpp;
		    String elementName = xpp.getName();
		    if("code".equals(elementName)) {
		        this.processCodeElement(true);
		    }
		    else if("decl".equals(elementName)) {
		        this.processCodeElement(false);
		    }
		    else {
		        String innerText = "";
		        int eventType;
		        OUTER:
		            for(xpp.next(); (eventType = xpp.getEventType()) != XmlPullParser.END_DOCUMENT; xpp.next())
		            {
		                switch(eventType) 
		                {
		                case XmlPullParser.START_TAG:
		                    throw new XAMJParseException(this.xpp, "Meta element named '" + elementName + "' cannot have children elements");
		                case XmlPullParser.END_TAG:
		                    break OUTER;
		                case XmlPullParser.TEXT:
		                    innerText += xpp.getText().trim();
		                	break;
		                case XmlPullParser.CDSECT:
		                    innerText += xpp.getText();
		                	break;
		                case XmlPullParser.COMMENT:
		                    break;
		                }               
		            }
		        if(eventType != XmlPullParser.END_TAG) {
		            throw new XAMJParseException(xpp, "Meta element named '" + elementName + "' is unclosed (end it with />)");
		        }
		        if("document-disposition".equals(elementName)) {
		        	this.documentDisposition = new ParsedText(innerText, xpp.getLineNumber());
		        }
		        else if("import".equals(elementName)) {
		            this.importsList.add(new ParsedText(innerText, xpp.getLineNumber()));
		        }
		    	else if("extends".equals(elementName)) {
		    	    this.extendsName = new ParsedText(innerText, xpp.getLineNumber());
		    	}
		        else if("implements".equals(elementName)) {
		            this.implementsList.add(new ParsedText(innerText, xpp.getLineNumber()));
		        }
		        else if("archive".equals(elementName)) {
		        	this.archiveList.add(new ParsedText(innerText, xpp.getLineNumber()));
		        }
		        else if("min-xamj-version".equals(elementName)) {
		        	this.minXamjVersion = new ParsedText(innerText, xpp.getLineNumber());
		        }
		        else if("min-java-version".equals(elementName)) {
		        	this.minXamjVersion = new ParsedText(innerText, xpp.getLineNumber());
		        }
		        else if("preferredXamjVersion".equals(elementName)) {
		            // TODO
		        }
		        else if("preferredJavaVersion".equals(elementName)) {
		            // TODO
		        }
		        else {
				    throw new XAMJParseException(xpp, "Meta element named '" + elementName + "' unknown");		            
		        }
		    }
		}

		public Collection getImports() {
		    return this.importsList;
		}
		
		public ParsedText getExtends() {
		    return this.extendsName;
		}
		
		public Collection getImplements() {
		    return this.implementsList;
		}

		private final Collection decls = new LinkedList();

		public Collection getDecls() {
		    return this.decls;
		}

		public void processCodeElement(boolean inline) throws XmlPullParserException, IOException, ClientletException {
		    XmlPullParser xpp = this.xpp;
			int eventType;
			OUTER:
			for(xpp.next(); (eventType = xpp.getEventType()) != XmlPullParser.END_DOCUMENT; xpp.next())
			{
				switch(eventType) 
				{
				case XmlPullParser.START_TAG:
				    throw new XAMJParseException(this.xpp, "Code element cannot have children elements");
				case XmlPullParser.END_TAG:
					break OUTER;
				case XmlPullParser.TEXT:
				case XmlPullParser.CDSECT:
				    if(inline) {
				        this.out.println(xpp.getText());
				    }
				    else {
				        this.decls.add(new ParsedText(xpp.getText(), xpp.getLineNumber()));
				    }
					break;
				case XmlPullParser.COMMENT:
					break;
				}               
			}
		}
	}

	private static class SourceInfo {
	    private final String source;
	    private final String className;
	    
	    public SourceInfo(String className, String source) {
	        this.source = source;
	        this.className = className;
	    }
	    
	    public String getSource() {
	        return this.source;
	    }
	    
	    public String getClassName() {
	        return this.className;
	    }
	}

	private static class ElementInfo {
	    public final String interfaceName;
	    public final String elementName;
	    public final String elementID;
	    public final boolean isRoot;
	    
	    public ElementInfo(String interfaceName, String name, String id, boolean isRoot) {
	        this.interfaceName = interfaceName;
	        this.elementName = name;
	        this.elementID = id;
	        this.isRoot = isRoot;
	    }
	}
	
	private static class ValueInfo {
	    public final String value;
	    public final boolean onlyLiteral;
	    
	    public ValueInfo(String value, boolean onlyLiteral) {
	        this.value = value;
	        this.onlyLiteral = onlyLiteral;
	    }
	}
	
	private static class ParsedText {
	    public final String text;
	    public final int lineNumber;
	    
	    public ParsedText(String text, int lineNumber) {
	        this.text = text;
	        this.lineNumber = lineNumber;
	    }
	}
	
	private static class ContextWrapper implements ClientletContext {
		private final ClientletContext delegate;
		
		private ContextWrapper(ClientletContext delegate) {
			this.delegate = delegate;
		}

		public ContentBuffer createContentBuffer(String contentType, byte[] content) {
			return delegate.createContentBuffer(contentType, content);
		}

		public ContentBuffer createContentBuffer(String contentType, String content, String encoding) throws UnsupportedEncodingException {
			return delegate.createContentBuffer(contentType, content, encoding);
		}

		public NavigatorFrame getClientletFrame() {
			return delegate.getClientletFrame();
		}

		public Object getItem(String name) {
			return delegate.getItem(name);
		}

		public ManagedStore getManagedStore() throws IOException {
			return delegate.getManagedStore();
		}

		public Properties getOverriddingWindowProperties() {
			return delegate.getOverriddingWindowProperties();
		}

		public ClientletRequest getRequest() {
			return delegate.getRequest();
		}

		public ClientletResponse getResponse() {
			return delegate.getResponse();
		}

		public ComponentContent getResultingContent() {
			return this.content;
		}

		public UserAgent getUserAgent() {
			return delegate.getUserAgent();
		}

		public void navigate(String url) throws MalformedURLException {
			delegate.navigate(url);
		}

		public void overrideWindowProperties(Properties properties) {
			delegate.overrideWindowProperties(properties);
		}

		public void setItem(String name, Object value) {
			delegate.setItem(name, value);
		}

		public void setResultingContent(Component content) {
			this.setResultingContent(new SimpleComponentContent(content));
		}

		private ComponentContent content;
		
		public void setResultingContent(ComponentContent content) {
			this.content = content;
		}
	}
}