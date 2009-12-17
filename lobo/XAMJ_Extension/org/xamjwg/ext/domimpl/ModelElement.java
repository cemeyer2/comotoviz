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
 * Created on May 20, 2005
 */
package org.xamjwg.ext.domimpl;

import org.lobobrowser.clientlet.*;
import org.lobobrowser.ua.NavigatorFrame;
import org.lobobrowser.util.*;
import org.xamjwg.dom.*;
import org.xamjwg.event.*;
import org.xamjwg.ext.gui.*;

import java.util.logging.*;
import java.awt.Component;

/**
 * @author J. H. S.
 */
public class ModelElement extends BaseElement implements XModelElement {
	private static final Logger logger = Logger.getLogger(ModelElement.class.getName());
	private final XamjListener onFirstShownListener;
	
	/**
	 * @param name
	 */
	public ModelElement(String name) {
		super(name);
		this.onFirstShownListener = new XamjListener() {
			public void execute(AbstractXamjEvent event) throws Exception {
				refresh(false);
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#addedNode(org.xamjwg.dom.XNode)
	 */
	protected void addedNode(XNode node) {
		super.addedNode(node);
		if(node instanceof BaseWidgetElement) {
			this.addToParent((BaseWidgetElement) node);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#removingNode(org.xamjwg.dom.XNode)
	 */
	protected void removingNode(XNode node) {
		super.removingNode(node);
		if(node instanceof BaseWidgetElement) {
			this.removeFromParent((BaseWidgetElement) node);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseNode#setParent(org.xamjwg.dom.XElement)
	 */
	public void setParent(XElement parent) {
		if(!(parent instanceof BaseWidgetElement)) {
			throw new IllegalArgumentException("Parent of element named '" + this.getNodeName() + "' expected to be a widget element");
		}
		BaseWidgetElement pbwe = (BaseWidgetElement) parent;
		BaseWidgetElement oldParent = (BaseWidgetElement) this.parent;
		if(oldParent != null) {
			oldParent.removeEventListener("on-first-shown", this.onFirstShownListener);
		}
		super.setParent(parent);
		if(pbwe.isShowing()) {
			try {
				this.refresh(false);
			} catch(Exception err) {
				logger.log(Level.SEVERE, "setParent()", err);
			}
		}
		else {
			pbwe.addEventListener("on-first-shown", this.onFirstShownListener);
		}
	}
	
	private void addToParent(BaseWidgetElement source) {
		SpecialWidgetElement swe = new SpecialWidgetElement(source.getWidget());
		this.parent.appendChild(swe);
	}

	private void removeFromParent(BaseWidgetElement source) {
		SpecialWidgetElement swe = new SpecialWidgetElement(source.getWidget());
		this.parent.removeChildNode(swe);
	}
	
	private void replaceInParent(Widget source) {
		this.parent.removeChildNodes(new SpecialWidgetElementFilter());
		SpecialWidgetElement swe = new SpecialWidgetElement(source);
		this.parent.appendChild(swe);
	}

	private XamjEvaluatable<String> href;
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XModelElement#getHref()
	 */
	public XamjEvaluatable<String> getHref() {
		return this.href;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XModelElement#refresh()
	 */
	public void refresh() throws Exception {
		this.refresh(true);
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XModelElement#refresh()
	 */
	private void refresh(boolean noDocCache) throws Exception {
		XamjEvaluatable hr = this.href; 
		if(hr != null) {
			Object val = hr.evaluate();
			if(!(val instanceof String)) {
				throw new IllegalStateException("href does not evaluate to String");
			}
			this.setHrefImpl((String) val, noDocCache);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XModelElement#setHref(org.xamjwg.dom.XamjEvaluatable)
	 */
	public void setHref(XamjEvaluatable<String> value) throws Exception {
		Object val = value.evaluate();
		if(!(val instanceof String)) {
			throw new IllegalArgumentException("Does not evaluate to String");
		}
		Object oldValue = this.href;
		if(!Objects.equals(oldValue, value)) {
			this.href = value;
			this.firePropertyChange("href", oldValue, value);
			XElement parent = this.parent;
			if(parent instanceof BaseWidgetElement) {
				if(((BaseWidgetElement) parent).isShowing()) {
					this.setHrefImpl((String) val, false);
				}
			}
		}		
	}

	private void setHrefImpl(String url, boolean bypassLocalCache) throws Exception {
		NavigatorFrame parentFrame = this.document.getClientletContext().getClientletFrame();
		NavigatorFrame newFrame = parentFrame.createFrame();
		WFrame wframe = new WFrame((org.lobobrowser.gui.FramePanel) newFrame);
		newFrame.navigate(url);
		this.replaceInParent(wframe);
	}
	
	private class SpecialWidgetElement extends BaseWidgetElement {
		//TODO: This needs to be of a certain type, e.g. a menu-item.
		
		private final Widget source;

		/**
		 * @param name
		 */
		public SpecialWidgetElement(Widget source) {
			super("$internal", source, false);
			this.source = source;
		}
		
		public Object getOwner() {
			return ModelElement.this;
		}
		
		/* (non-Javadoc)
		 * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
		 */
		protected Widget createWidget() {
			throw new UnsupportedOperationException("not expected");
		}
		
		public boolean equals(Object other) {
			return other instanceof SpecialWidgetElement && ((SpecialWidgetElement) other).source.equals(this.source);
		}
		
		public int hashCode() {
			return this.source.hashCode();
		}
		
		/* (non-Javadoc)
		 * @see org.xamjwg.dom.XElement#getElementValue()
		 */
		public Object getElementValue() {
			return this.getId();
		}
	}
	
	private class SpecialWidgetElementFilter implements XamjNodeFilter {
		/* (non-Javadoc)
		 * @see org.xamjwg.dom.XamjNodeFilter#accept(org.xamjwg.dom.XNode)
		 */
		public boolean accept(XNode node) {
			return node instanceof SpecialWidgetElement &&
				((SpecialWidgetElement) node).getOwner() == ModelElement.this;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this;
	}
}
