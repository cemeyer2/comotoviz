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
/*
 * Created on Mar 14, 2005
 */
package org.xamjwg.ext.domimpl;

import org.xamjwg.dom.XHeadElement;
import org.xamjwg.dom.XMetaElement;
import org.xamjwg.dom.XNode;

/**
 * @author J. H. S.
 */
public class HeadElement extends BaseElement implements XHeadElement {
    private String minXamjVersion;
    private String preferredXamjVersion;
    private String minJavaVersion;
    private String preferredJavaVersion;
    private String _import;
    private String _extends;
    private String _implements;

    //TODO: propery-change events?
    //TODO: Access to HeadElement from XDocument?
    
    public HeadElement(String name) {
        super(name);
    }

    /**
     * @return Returns
     */
    public String getExtends() {
        return _extends;
    }
    /**
     * @param _extends 
     */
    public void setExtends(String _extends) {
        this._extends = _extends;
    }
    /**
     * @return Returns
     */
    public String getImplements() {
        return _implements;
    }
    /**
     * @param _implements 
     */
    public void setImplements(String _implements) {
        this._implements = _implements;
    }
    /**
     * @return Returns
     */
    public String getImport() {
        return _import;
    }
    /**
     * @param _import 
     */
    public void setImport(String _import) {
        this._import = _import;
    }
    /**
     * @return Returns
     */
    public String getMinJavaVersion() {
        return minJavaVersion;
    }
    /**
     * @param minJavaVersion 
     */
    public void setMinJavaVersion(String minJavaVersion) {
        this.minJavaVersion = minJavaVersion;
    }
    /**
     * @return Returns
     */
    public String getMinXamjVersion() {
        return minXamjVersion;
    }
    /**
     * @param minXamjVersion 
     */
    public void setMinXamjVersion(String minXamjVersion) {
        this.minXamjVersion = minXamjVersion;
    }
    /**
     * @return Returns
     */
    public String getPreferredJavaVersion() {
        return preferredJavaVersion;
    }
    /**
     * @param preferredJavaVersion 
     */
    public void setPreferredJavaVersion(String preferredJavaVersion) {
        this.preferredJavaVersion = preferredJavaVersion;
    }
    /**
     * @return Returns
     */
    public String getPreferredXamjVersion() {
        return preferredXamjVersion;
    }
    /**
     * @param preferredXamjVersion 
     */
    public void setPreferredXamjVersion(String preferredXamjVersion) {
        this.preferredXamjVersion = preferredXamjVersion;
    }

    private XMetaElement titleElement;
	private XMetaElement documentIconElement;
	private XMetaElement descriptionElement;
	private XMetaElement documentWidthElement;
	private XMetaElement documentHeightElement;

	public void setTitleElement(XMetaElement metaElement) {
		this.titleElement = metaElement;
	}
	
	public String getTitle() {
		XMetaElement me = this.titleElement;
		return me == null ? "" : me.getInnerText();
	}
	
	public void setDocumentIconElement(XMetaElement metaElement) {
		this.documentIconElement = metaElement;
	}
	
	public String getDocumentIcon() {
		XMetaElement me = this.documentIconElement;
		return me == null ? "" : me.getInnerText();
	}

	public void setDescriptionElement(XMetaElement metaElement) {
		this.descriptionElement = metaElement;
	}
	
	public String getDescription() {
		XMetaElement me = this.descriptionElement;
		return me == null ? "" : me.getInnerText();
	}
	
	public void setDocumentHeightElement(XMetaElement metaElement) {
		this.documentHeightElement = metaElement;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XHeadElement#getDocumentHeight()
	 */
	public String getDocumentHeight() {
		XMetaElement me = this.documentHeightElement;
		return me == null ? null : me.getInnerText();
	}
	
	public void setDocumentWidthElement(XMetaElement metaElement) {
		this.documentWidthElement = metaElement;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XHeadElement#getDocumentWidth()
	 */
	public String getDocumentWidth() {
		XMetaElement me = this.documentWidthElement;
		return me == null ? null : me.getInnerText();
	}
	
	/* (non-Javadoc)
     * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
     */
    public void appendChild(XNode node) {
    	if(node instanceof XMetaElement) {
    		XMetaElement metaElement = (XMetaElement) node;
    		String name = metaElement.getNodeName();
    		if("title".equals(name)) {
    			this.setTitleElement(metaElement);
    		}
    		else if("description".equals(name)) {
    			this.setDescriptionElement(metaElement);
    		}
    		else if("document-icon".equals(name)) {
    			this.setDocumentIconElement(metaElement);
    		}
    		else if("document-width".equals(name)) {
    			this.setDocumentWidthElement(metaElement);
    		}
    		else if("document-height".equals(name)) {
    			this.setDocumentHeightElement(metaElement);
    		}
    		//TODO rest of properties
    	}
    	super.appendChild(node);
    }
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.getTitle();
	}
}
