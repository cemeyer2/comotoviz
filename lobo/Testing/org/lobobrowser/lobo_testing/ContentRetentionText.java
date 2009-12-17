/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The XAMJ Project

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

    Contact info: lobochief@users.sourceforge.net
*/
package org.lobobrowser.lobo_testing;

import org.lobobrowser.gui.*;
import org.lobobrowser.main.*;
import org.lobobrowser.util.*;
import org.lobobrowser.context.*;
import org.lobobrowser.store.*;
import org.lobobrowser.clientlet.*;

import java.awt.Component;
import java.io.*;

/**
 * Repeatedly loads a page with a new set of images.
 * It checks for memory leaks due to images retention.
 */
public class ContentRetentionText {
	public static void main(String[] args) throws Exception {
		PlatformInit.getInstance().init(false, false);
		ClientletFactory.getInstance().addClientletSelector(new LocalClientletSelector());
		BrowserPanel panel = TestWindow.newWindow();
		for(int i = 0; i < 100; i++) {
			newTest(panel);
		}
	}
	
	public static void newTest(BrowserPanel panel) throws Exception {
		panel.navigate(getNewURL());
		System.gc();
		Thread.sleep(5000);
		System.out.println("### Free memory: " + Runtime.getRuntime().freeMemory());
		System.out.println("### Total memory: " + Runtime.getRuntime().totalMemory());
		CacheInfo cacheInfo = CacheManager.getInstance().getTransientCacheInfo();
		System.out.println("### RAM cache entries: " + cacheInfo.numEntries);
		System.out.println("### RAM cache size: " + cacheInfo.approximateSize);		
	}
	
	private static Object retainVc;

 	public static String getNewURL() {
		VolatileContentImpl vc = new VolatileContentImpl("text/html", new byte[0]);
		retainVc = vc;
		return vc.getURL().toExternalForm();
	}
 	
 	private static class LocalClientletSelector implements ClientletSelector {
		public Clientlet lastResortSelect(ClientletRequest request,
				ClientletResponse response) {
			return null;
		}

		public Clientlet select(ClientletRequest request,
				ClientletResponse response) {
			if(request.getRequestURL().getProtocol().equals("vc")) {
				return new LocalClientlet();
			}
			else {
				return null;
			}
		}
 	}
 	
 	private static class LocalClientlet implements Clientlet {
		public void process(ClientletContext context) throws ClientletException {
			System.out.println("### Setting component content");
			context.setResultingContent(new LocalComponentContent());
		}
 	}
 	
 	private static class LocalComponentContent extends AbstractComponentContent {
 		private final byte[] balderdash = new byte[5000000];
 		
		@Override
		public Component getComponent() {
			return new javax.swing.JLabel("The content");
		}

		@Override
		public String getSourceCode() {
			return "";
		}

		@Override
		public String getTitle() {
			return "The title";
		}
 	}
}
