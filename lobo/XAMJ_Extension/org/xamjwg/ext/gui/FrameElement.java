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
 * Created on Mar 29, 2005
 */
package org.xamjwg.ext.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


import org.lobobrowser.async.AsyncResult;
import org.lobobrowser.clientlet.*;
import org.lobobrowser.context.*;
import org.lobobrowser.gui.*;
import org.lobobrowser.request.*;
import org.lobobrowser.security.*;
import org.lobobrowser.store.*;
import org.lobobrowser.ua.ParameterInfo;
import org.lobobrowser.ua.TargetType;
import org.lobobrowser.util.*;

import java.util.logging.*;
import org.xamjwg.dom.*;
import org.xamjwg.event.*;

import java.util.*;

/**
 * @author J. H. S.
 */
public class FrameElement extends LeafWidgetElement implements XFrameElement {
	private static final Logger logger = Logger.getLogger(FrameElement.class.getName());
	private History history = null;
	
    /**
     * @param name
     */
    public FrameElement(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.xamj.domimpl.gui.BaseWidgetElement#createWidget()
     */
    protected Widget createWidget() {
        return new WFrame();
    }
        
    private String getHistoryPath() {
    	String id = this.getId();
    	if(id == null) {
    		return null;
    	}
    	else {
    		return this.getId() + "$History.dat";
    	}
    }
    
    private History getHistory() {
    	synchronized(this) {
    		if(this.history == null) {
    			final String historyPath = this.getHistoryPath();
    			if(historyPath == null) {
    				this.history = new History(300, 200);
    			}
    			else {
    				AccessController.doPrivileged(new java.security.PrivilegedAction() {
    					public Object run() {
    						try {
    							RestrictedStore store = StorageManager.getInstance().getRestrictedStore("");
    							history = (History) store.retrieveObject(historyPath);
    							if(history == null) {
    								history = new History(300, 200);
    							}
    						} catch(java.io.FileNotFoundException fnf) {
    							history = new History(300, 200);
    						} catch(Exception err) {
    							logger.log(Level.SEVERE, "getHistory()", err);
    							history = new History(300, 200);
    						}
    						return null;
    					}
    				});
    			}
    		}
    		return this.history;
    	}    	
    }

    private void saveHistory() {
		try {
			String historyPath = this.getHistoryPath();
			if(historyPath == null) {
				if(logger.isLoggable(Level.INFO))logger.info("saveHistory(): No ID for frame");
				return;
			}
			RestrictedStore store = (RestrictedStore) AccessController.doPrivileged(new java.security.PrivilegedAction() {
				public Object run() {
					try {
						return StorageManager.getInstance().getRestrictedStore("");
					} catch(java.io.IOException ioe) {
						throw new IllegalStateException(ioe);
					}
				}
			});
			long time1 = System.currentTimeMillis();
			try {
				store.saveObject(historyPath, this.getHistory());
			} finally {
				long time2 = System.currentTimeMillis();
				if(logger.isLoggable(Level.INFO))logger.info("saveHistory(): Took " + (time2 - time1) + " ms.");
			}
		} catch(Exception err) {
			logger.log(Level.SEVERE, "getHistory()", err);
			this.history = new History(300, 200);
		}
    }
    
    private String href;
    
    private void updateHref(String value) {
    	Object oldValue = this.href;
    	if(!Objects.equals(oldValue, value)) {
    		this.href = value;
    		this.firePropertyChange("href", oldValue, value);
    	}
    }
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XFrameElement#getHref()
	 */
	public String getHref() {
		return this.href;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XFrameElement#setHref(java.lang.String)
	 */
	public void setHref(String value) throws MalformedURLException {
		URL absURL = RequestEngine.createURL(this.getCurrentURL(), value);
		this.navigateImpl(absURL, true, true);
	}
	
    public void navigate(String url) throws java.net.MalformedURLException {
    	this.navigateImpl(url, true, true);
    }
    
    private AsyncResult navigateImpl(String url, boolean addToHistory, boolean addAsRecent) throws java.net.MalformedURLException {
    	return this.navigateImpl(url, addToHistory, addAsRecent, true);
    }

    private AsyncResult navigateImpl(String url, boolean addToHistory, boolean addAsRecent, boolean mayUseCache) throws java.net.MalformedURLException {
    	// This navigate method should not take relative URLs.    	
    	URL absURL = RequestEngine.createURL(url);
    	return this.navigateImpl(absURL, addToHistory, addAsRecent, mayUseCache);
    }

	public AsyncResult navigate(URL url, String method,
			ParameterInfo pinfo, Map items, boolean mayUseCache, TargetType targetType) {
		WFrame wframe = (WFrame) this.widget;
		FramePanel panel = wframe.getFramePanel();
		if(panel != null) {
			// Not obeying mayUseCache right now.
			panel.navigate(url, method, pinfo, targetType);
		}
		// Returning null at the moment.
		return null;
	}

//    /**
//     * If this key is part of a document items, it's added to
//     * the URL history (dropdown) when a response is received.
//     */
//    private static final String ADD_TO_HISTORY_KEY = "Warrior.AddToHistory";
    	
    private void navigateImpl(URL absURL, boolean addToHistory, boolean addAsRecent) {
    	this.navigateImpl(absURL, addToHistory, addAsRecent, true);
    }
    
    private AsyncResult navigateImpl(URL absURL, boolean addToHistory, boolean addAsRecent, boolean mayUseCache) {
		WFrame wframe = (WFrame) this.widget;
		FramePanel panel = wframe.getFramePanel();
		if(panel != null) {
			// Not adding to history right now.
			panel.navigate(absURL, "GET", null, null);
		}
		// Returning null at the moment.
		return null;
    }

    public void back() {
    	String url = this.getHistory().back();
    	if(url != null) {
    		try {
    			this.navigateImpl(url, false, false);
    		} catch(java.net.MalformedURLException mfu) {
    			logger.log(Level.SEVERE, "back()", mfu);
    		}
    	}
    }
    
    public void forward() {
    	String url = this.getHistory().forward();
    	if(url != null) {
    		try {
    			this.navigateImpl(url, false, false);
    		} catch(java.net.MalformedURLException mfu) {
    			logger.log(Level.SEVERE, "back()", mfu);
    		}
    	}
    }

    public void stop() {
    	RequestEngine.getInstance().cancelAllRequests();
    	//TODO: How to cancel compilation
    }
    
    
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XFrameElement#refresh()
	 */
	public void refresh() {
		// refresh not functional at the moment.
		
//		ClientletContext document = this.currentContext;
//		if(document != null) {
//			try {
//				this.navigateImpl(document.getResponse().getResponseURL().toExternalForm(), false, false, false);
//			} catch(java.net.MalformedURLException mfu) {
//				logger.log(Level.SEVERE, "refresh()", mfu);
//			}
//		}
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XFrameElement#getPotentialMatches(java.lang.String, int)
	 */
	public Collection getPotentialMatches(String partialUrl, int maxMatches) {
		SecurityManager sm = System.getSecurityManager();
		if(sm != null) {
			sm.checkPermission(new HistoryPermission());
		}
		int colonIdx = partialUrl.indexOf(':');
		String item;
		if(colonIdx == -1) {
			if(partialUrl.length() < 1 || "http".startsWith(partialUrl) || "www.".startsWith(partialUrl)) {
				return Collections.EMPTY_LIST;
			}
			item = "http://" + partialUrl;
		}
		else {
			int extraLen = partialUrl.length() - colonIdx;
			String rest = partialUrl.substring(colonIdx + 1);
			if(extraLen < 1 || (rest.startsWith("//") && extraLen < 3) || "//www.".startsWith(rest)) {
				return Collections.EMPTY_LIST;
			}
			item = partialUrl;
		}
		return this.getHistory().getHeadMatchItems(item, maxMatches);
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XFrameElement#getRecentLocations(int)
	 */
	public Collection getRecentLocations(int maxLocations) {
		SecurityManager sm = System.getSecurityManager();
		if(sm != null) {
			sm.checkPermission(new HistoryPermission());
		}
		return this.getHistory().getRecentItems(maxLocations);
	}
		
	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.BaseElement#checkEventElement(org.xamjwg.dom.XEventElement)
	 */
	protected void checkEventElement(XEventElement eventElement) {
		String eventName = eventElement.getNodeName();
		if("on-progress".equals(eventName)) {
			//passes
		}
		else {
			super.checkEventElement(eventElement);
		}
	}
	
	protected void navigatedTo(URL url) {
	}
	
	public String getSource() {
		// Not available at the moment.
		return "[Source Not Available]";
		
//		ClientletContext document = this.currentContext;
//		if(document != null) {
//			String source = document.getSourceCode();
//			if(source == null) {
//				return "[Source Not Available]";
//			}
//			else {
//				return source;
//			}
//		}
//		else {
//			return "[Document Not Available]";
//		}
	}
	
	public FrameElement getParentFrame() {
		XElement element = this.getParent();
		while(element != null && !(element instanceof FrameElement)) {
			element = element.getParent();
		}
		return (FrameElement) element;
	}
	
	public FrameElement getTopFrame() {
		FrameElement topFrame = this;
		for(;;) {
			FrameElement parentFrame = topFrame.getParentFrame();
			if(parentFrame == null) {
				break;
			}
			topFrame = parentFrame;
		}
		return topFrame;
	}
	
	/* (non-Javadoc)
	 * @see org.xamjwg.dom.XElement#getElementValue()
	 */
	public Object getElementValue() {
		return this.href;
	}
	
//	private class LocalFrameClientletContext extends BaseFrameClientletContext {
//        private LocalFrameClientletContext(Object requestSource) {
//            super(requestSource, null, null, false);
//        }
//
//        /**
//         * @param request
//         * @param response
//         */
//        public LocalFrameClientletContext(Object requestSource, ClientletRequest request,
//                ClientletResponse response, boolean onWindow) {
//            super(requestSource, request, response, onWindow);
//        }
//
//        /* (non-Javadoc)
//         * @see net.sourceforge.xamj.context.BaseClientletContext#createClientletContext(org.xamjwg.dom.ClientletRequest, org.xamjwg.dom.ClientletResponse)
//         */
//        public BaseClientletContext createNewContext(
//                ClientletRequest request, ClientletResponse response, boolean onWindow) {
//            return new LocalFrameClientletContext(this.requestSource, request, response, onWindow);
//        }
//
//        /* (non-Javadoc)
//         * @see net.sourceforge.xamj.context.BaseClientletContext#getNavigateContainer()
//         */
//        protected Container getNavigateContainer(BaseClientletContext context) {
//            return FrameElement.this.getWidget();
//        }
//        
//		/* (non-Javadoc)
//		 * @see net.sourceforge.xamj.context.BaseClientletContext#handleProgress(java.net.URL, int, int)
//		 */
//		public void handleProgress(final String message, final int value, final int max) {
//			if(EventQueue.isDispatchThread()) {
//				FrameElement.this.fireXamjEvent(AbstractXamjEvent.createProgress(this, message, value, max));
//			}
//			else {
//				EventQueue.invokeLater(new Runnable() {
//					public void run() {
//						FrameElement.this.fireXamjEvent(AbstractXamjEvent.createProgress(this, message, value, max));
//					}
//				});
//			}
//		}
//		
//		/* (non-Javadoc)
//		 * @see org.xamjwg.dom.XDocument#navigate(java.net.URL, java.lang.String, org.xamjwg.clientlet.ParameterInfo)
//		 */
//		public AsyncResult navigate(URL url, String method, ParameterInfo pinfo, boolean addAsRecent, boolean addToHistory, Map items, boolean mayUseCache) {
//			if(addAsRecent) {	
//				String externalForm = url.toExternalForm();
//				FrameElement.this.getHistory().addAsRecentOnly(externalForm);
//				FrameElement.this.saveHistory();
//			}
//			if(items == null) {
//				items = new HashMap();
//			}
//			items.put(ADD_TO_HISTORY_KEY, Boolean.valueOf(addToHistory));
//			return this.navigate(url, method, pinfo, items, mayUseCache, TargetType.self);
//		}
//
//		/* (non-Javadoc)
//		 * @see net.sourceforge.xamj.context.BaseDocument#navigate(java.net.URL, java.lang.String, org.xamjwg.clientlet.ParameterInfo, java.util.Map)
//		 */
//		public AsyncResult navigate(URL url, String method,
//				ParameterInfo pinfo, Map items, boolean mayUseCache, TargetType targetType) {
//			FrameElement other = null;
//			switch(targetType) {
//			case parent:
//				other = FrameElement.this.getParentFrame();
//				break;
//			case top:
//				other = FrameElement.this.getTopFrame();
//				break;
//			}
//			if(other != null && other != FrameElement.this) {
//				return other.navigate(url, method, pinfo, items, mayUseCache, targetType);
//			}
//			else {
//				if(items == null) {
//					items = new HashMap();
//					items.put(ADD_TO_HISTORY_KEY, Boolean.TRUE);
//				}
//				else {
//					if(!items.containsKey(ADD_TO_HISTORY_KEY)) {
//						items.put(ADD_TO_HISTORY_KEY, Boolean.TRUE);
//					}
//				}
//				AsyncResult ar = super.navigate(url, method, pinfo, items, mayUseCache, targetType);
//				ar.addResultListener(new AsyncResultListener() {
//					public void exceptionReceived(AsyncResultEvent event) {
//						// Should be handled elsewhere
//					}
//					
//					public void resultReceived(AsyncResultEvent event) {
//						FrameElement.this.currentContext = (LocalFrameClientletContext) event.getResult();
//					}				
//				});
//				return ar;
//			}
//		}
//		
//		public void responseHook(final ClientletResponse response) {
//			// Called in GUI thread
//			URL responseURL = response.getResponseURL();
//			String externalForm = responseURL.toExternalForm();
//			FrameElement.this.updateHref(externalForm);
//			if(Boolean.TRUE.equals(this.getItem(ADD_TO_HISTORY_KEY))) {
//				FrameElement.this.getHistory().addItem(externalForm, false);				
//			}
//			FrameElement.this.navigatedTo(responseURL);
//		}
//
//		/* (non-Javadoc)
//		 * @see org.xamjwg.clientlet.ClientletContext#getFrameElement()
//		 */
//		public XFrameElement getFrameElement() {
//			return FrameElement.this; 
//		}
//
//		/* (non-Javadoc)
//		 * @see org.xamjwg.clientlet.ClientletContext#getParentComponent()
//		 */
//		public Component getParentComponent() {
//			return FrameElement.this.widget;
//		}
//    }

}
