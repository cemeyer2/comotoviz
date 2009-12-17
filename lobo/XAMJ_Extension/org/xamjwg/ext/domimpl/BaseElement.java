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
 * Created on Mar 5, 2005
 */
package org.xamjwg.ext.domimpl;

import java.net.URL;
import java.util.*;
import java.util.logging.*;
import java.beans.*;


import org.lobobrowser.clientlet.ClientletResponse;
import org.lobobrowser.util.*;
import org.xamjwg.dom.XamjNodeFilter;
import org.xamjwg.dom.XCodeElement;
import org.xamjwg.dom.XComment;
import org.xamjwg.dom.XDeclElement;
import org.xamjwg.dom.XElement;
import org.xamjwg.dom.XEventElement;
import org.xamjwg.dom.XNode;
import org.xamjwg.dom.XText;
import org.xamjwg.event.*;

/**
 * @author J. H. S.
 */
public abstract class BaseElement extends BaseNode implements XElement {
	private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(BaseElement.class.getName());
	// TODO: Consider creating the LinkedList on demand
	private final List nodes = new LinkedList();
	protected final String name;
	private Map listenersForEvent = null;	
	
	public BaseElement(String name) {
	    this.name = name;
	}
	
    protected URL getCurrentURL() {
    	ClientletResponse response = this.document.getClientletContext().getResponse();
    	return response == null ? null : response.getResponseURL();
    }    

	protected boolean hasListeners(String eventName) {
		synchronized(this) {
			if(this.listenersForEvent == null) {
				return false;
			}
			Collection listeners = (Collection) this.listenersForEvent.get(eventName);
			return listeners != null && !listeners.isEmpty();
		}			
	}
	
	public String getNodeName() {
	    return this.name;
	}
	
	private String id;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getId()
	 */
	public String getId() {
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#setId(java.lang.String)
	 */
	public void setId(String value) {
		String oldValue = this.id;
		if(!Objects.equals(value, oldValue)) {
			this.id = value;
			this.firePropertyChange("id", oldValue, value);
		}
	}

	private XMacroElement macro;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getMacro()
	 */
	public XMacroElement getMacro() {
		return this.macro;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#setMacro(org.xamjwg.dom.XMacroElement)
	 */
	public void setMacro(XMacroElement value) {
		//TODO Should this have an effect, or only while parsing?
		XMacroElement oldValue = this.macro;
		if(!Objects.equals(value, oldValue)) {
			this.macro = value;
			this.firePropertyChange("id", oldValue, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#appendChild(org.xamjwg.dom.XNode)
	 */
	public void appendChild(XNode node) {
		this.appendChildImpl(node);
	}
	
	protected final void appendChildImpl(XNode node) {
		if(!(node instanceof BaseNode)) {
			if(node == null) {
				throw new IllegalArgumentException("node is null");
			}
			else {
				throw new IllegalArgumentException("Node of type '" + node.getClass().getName() + "' is not supported by this clientlet engine.");
			}
		}
	    if(node instanceof XEventElement) {
	        this.checkEventElement((XEventElement) node);
	        this.addEventListener(((XElement) node).getNodeName(), (XamjListener) node);
	    }
	    ((BaseNode) node).setParent(this);
		this.nodes.add(node);
		this.addedNode(node);
	}

	public void removeChildNode(XNode node) {
		this.removingNode(node);
		this.nodes.remove(node);
	}
	
	protected void checkEventElement(XEventElement eventElement) {
		String eventName = eventElement.getNodeName();
		if("on-property-change".equals(eventName)) {
			// passes
		}
		else {
			throw new IllegalArgumentException("Element named '" + this.getNodeName() + "' does not accept event elements named '" + eventElement.getNodeName() + "'");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#endBatch()
	 */
	public void endBatch() {
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getChildren()
	 */
	public Iterator getChildNodes() {
		return this.nodes.iterator();
	}
	
	public int getChildCount(final XamjNodeFilter filter) {
		int count = 0;
		Iterator i = this.getChildNodes();
		while(i.hasNext()) {
			if(filter.accept((XNode) i.next())) {
				count++;
			}
		}
		return count;
	}
	
	public Iterator getChildNodes(final XamjNodeFilter filter) {
		final Iterator baseIterator = this.getChildNodes();
		return new Iterator() {
			private boolean atNext = false;
			private Object next;
			
			public boolean hasNext() {
				if(this.atNext) {
					return true;
				}
				try {
					this.advanceToNext();
					return true;
				} catch(NoSuchElementException nse) {
					return false;
				}
			}
			
			public Object next() {
				if(!this.atNext) {
					this.advanceToNext();
				}
				this.atNext = false;
				return this.next;
			}

			private void advanceToNext() {
				while(baseIterator.hasNext()) {
					XNode node = (XNode) baseIterator.next();
					if(filter.accept(node)) {
						this.atNext = true;
						this.next = node;
						return;
					}
				}
				throw new NoSuchElementException();
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#startBatch()
	 */
	public void startBatch() {
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
	    this.addEventListener("on-property-change", new PropertyChangeClientletListener(listener));
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
	    this.removeEventListener("on-property-change", new PropertyChangeClientletListener(listener));
	}
	
	protected void addEventListener(String eventName, XamjListener listener) {
	    synchronized(this) {
	        if(this.listenersForEvent == null) {
	            this.listenersForEvent = new HashMap();
	        }
	        Collection listeners = (Collection) this.listenersForEvent.get(eventName);
	        if(listeners == null) {
	            listeners = new LinkedList();
	            this.listenersForEvent.put(eventName, listeners);
	        }
	        listeners.add(listener);
	    }
	}
	
	protected void removeEventListener(String eventName, XamjListener listener) {
	    synchronized(this) {
	        if(this.listenersForEvent == null) {
	            return;
	        }
	        Collection listeners = (Collection) this.listenersForEvent.get(eventName);
	        if(listeners == null) {
	            return;
	        }
	        listeners.remove(listener);
	    }
	}
	
	protected void fireEvent(String eventName, EventListenerVisitor visitor) {
	    EventListener[] listenersArray;
	    synchronized(this) {
	        if(this.listenersForEvent == null) {
	            return;
	        }
	        Collection listeners = (Collection) this.listenersForEvent.get(eventName);
	        if(listeners == null) {
	            return;
	        }
	        listenersArray = (EventListener[]) listeners.toArray(new EventListener[0]);
	    }
	    
	    //if(logger.isLoggable(Level.INFO))logger.info("fireEvent(): eventName=" + eventName + ", " + listenersArray.length + " handlers.");
	    
	    for(int i = 0; i < listenersArray.length; i++) {
	    	try {
	    		visitor.visit(listenersArray[i]);
	    	} catch(Exception err) {
	    		logger.log(Level.SEVERE, "fireEvent()", err);
	    		//TODO To page log or console
	    	}
	    }	    
	}
	
	protected void fireXamjEvent(final AbstractXamjEvent event) {
	    this.fireEvent(event.getName(), new EventListenerVisitor() {
	        public void visit(EventListener listener) throws Exception {
	            ((XamjListener) listener).execute(event);
	        }
	    });
	}

	protected void firePropertyChange(final String name, final Object oldValue, final Object newValue) {
		this.fireXamjEvent(AbstractXamjEvent.createPropertyChanged(this, name, oldValue, newValue));
	}
	
	//NOTE: Problem with cloneNode is that event scriptlets
	//added to cloned node refer to variables only available
	//in source document.
	
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XNode#cloneNode(boolean)
//	 */
//	public XNode cloneNode() {
//		try {
//			String elementName = this.getName();
//			ElementDescriptor ed = ElementFactory.getInstance().getElementDescriptor(elementName);
//			PropertyDescriptor[] pds = ed.getPropertyDescriptors();
//			XElement clone = this.document.createElement(elementName);
//			int len = pds.length;
//			for(int i = 0; i < len; i++) {
//				PropertyDescriptor pd = pds[i];
//				Method readMethod = pd.getReadMethod();
//				Method writeMethod = pd.getWriteMethod();
//				if(readMethod != null && writeMethod != null) {
//					writeMethod.invoke(clone, new Object[] { readMethod.invoke(this, EMPTY_ARGS) });
//				}				
//				//TODO Clone style attributes
//			}
//			Iterator childNodes = this.getChildNodes();
//			while(childNodes.hasNext()) {
//				XNode child = (XNode) childNodes.next();
//				child = child.cloneNode();
//				clone.appendChild(child);
//			}
//			return clone;
//		} catch(Exception err) {
//			logger.log(Level.SEVERE, "cloneNode()", err);
//			return null;
//		}
//	}
	
	public String toString() {
	    return this.getNodeName();
	}
	
	protected boolean allowedAlways(XElement element) {
		return element instanceof XText ||
			element instanceof XComment ||
			element instanceof XCodeElement ||
			element instanceof XDeclElement;
	}
	
	public void removeChildNodes() {
		Iterator i = this.nodes.iterator();
		while(i.hasNext()) {
			this.removingNode((XNode) i.next());
		}
		this.nodes.clear();
	}

	public void removeChildNodes(XamjNodeFilter filter) {
		Iterator i = this.nodes.iterator();
		Collection toReadd = new LinkedList();
		while(i.hasNext()) {
			XNode node = (XNode) i.next();
			if(filter.accept(node)) {
				this.removingNode(node);
			}
			else {
				toReadd.add(node);
			}
		}
		this.nodes.clear();
		i = toReadd.iterator();
		while(i.hasNext()) {
			this.nodes.add(i.next());
		}
	}

//	public void replaceChildNodes(XElement source) {
//		this.removeChildNodes();
//		Iterator i = source.getChildNodes();
//		while(i.hasNext()) {
//			XNode node = ((XNode) i.next()).cloneNode();
//			this.appendChild(node);
//		}
//	}
	
	/**
	 * Extending classes should override this 
	 * as a hook for node removal.
	 * @param node
	 */
	protected void removingNode(XNode node) {
		if(node instanceof XEventElement) {
			//TODO What if we removed the node but not the listener?
	        this.removeEventListener(((XElement) node).getNodeName(), (XamjListener) node);
		}
	}

	protected void addedNode(XNode node) {		
	}
	
//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(java.lang.String)
//	 */
//	public void replaceChildNodes(final String url) throws java.net.MalformedURLException {
//		if(logger.isLoggable(Level.INFO))logger.info("replaceChildNodes(): Requesting " + url);
//		AsyncResult ar = this.getOwnerDocument().requestXamj(url);
//		ar.addResultListener(new AsyncResultListener() {
//		    public void resultReceived(AsyncResultEvent event) {
//		    	XElement element = (XElement) event.getResult();
//				if(logger.isLoggable(Level.INFO))logger.info("replaceChildNodes(): Result received for " + url + ": " + element);
//				if(!(element instanceof XMacroElement)) {
//					throw new IllegalStateException("Element received on url '" + url + "' should be 'macro', not '" + element.getName() + "'");
//				}
//		    	replaceChildNodes(element);
//		    }
//		    
//		    public void exceptionReceived(AsyncResultEvent event){
//		    	logger.log(Level.SEVERE, "replaceChildNodes()", (Exception) event.getResult());
//		    }
//		});
//	}

//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(java.lang.String, org.xamjwg.dom.NodeFilter)
//	 */
//	public void replaceChildNodes(String url, final XamjNodeFilter filter)
//			throws MalformedURLException {
//		AsyncResult ar = this.getOwnerDocument().request(url);
//		ar.addResultListener(new AsyncResultListener() {
//		    public void resultReceived(AsyncResultEvent event) {
//		    	XElement element = (XElement) event.getResult();
//		    	replaceChildNodes(element, filter);
//		    }
//		    
//		    public void exceptionReceived(AsyncResultEvent event){
//		    	logger.log(Level.SEVERE, "replaceChildNodes()", (Exception) event.getResult());
//		    }
//		});
//	}

//	/* (non-Javadoc)
//	 * @see org.xamjwg.dom.XElement#replaceChildNodes(org.xamjwg.dom.XElement, org.xamjwg.dom.NodeFilter)
//	 */
//	public void replaceChildNodes(XElement source, XamjNodeFilter filter) {
//		this.removeChildNodes(filter);
//		Iterator i = source.getChildNodes();
//		while(i.hasNext()) {
//			XNode node = ((XNode) i.next()).cloneNode();
//			this.appendChild(node);
//		}
//	}
	
	protected static interface EventListenerVisitor {
	    public void visit(EventListener listener) throws Exception;
	}
	
	private static class PropertyChangeClientletListener implements XamjListener<XamjPropertyChangeEvent> {
		private final PropertyChangeListener listener;
		
		public PropertyChangeClientletListener(PropertyChangeListener listener) {
			this.listener = listener;
		}
		
		/* (non-Javadoc)
		 * @see org.xamjwg.clientlet.ClientletListener#execute(org.xamjwg.clientlet.XamjEvent)
		 */
		public void execute(XamjPropertyChangeEvent event) throws Exception {
			this.listener.propertyChange(new PropertyChangeEvent(event.getSource(), event.getPropertyName(), event.getOldValue(), event.getNewValue()));
		}
		
		public boolean equals(Object other) {
			return other instanceof PropertyChangeClientletListener && ((PropertyChangeClientletListener) other).listener.equals(this.listener); 
		}
		
		public int hashCode() {
			return this.listener.hashCode();
		}
	}
}
