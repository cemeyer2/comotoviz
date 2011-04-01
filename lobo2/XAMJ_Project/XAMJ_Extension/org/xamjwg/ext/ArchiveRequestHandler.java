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
 * Created on Jun 19, 2005
 */
package org.xamjwg.ext;

import java.io.IOException;
import java.net.URL;

import org.lobobrowser.clientlet.*;
import org.lobobrowser.request.*;
import org.lobobrowser.ua.*;

/**
 * @author J. H. S.
 */
public class ArchiveRequestHandler extends SimpleRequestHandler {
	private java.io.File jarFile;
	private final NavigatorFrame clientletUI;
	
	public ArchiveRequestHandler(URL url, NavigatorFrame clientletUI) {
		super(false, url, true);
		this.clientletUI = clientletUI;
	}

	public java.io.File getJarFile() {
		return this.jarFile;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.http.RequestHandler#handleException(java.lang.Exception)
	 */
	public void handleException(URL latestURL, String method, Exception exception) throws ClientletException {
		if(exception instanceof ClientletException) {
			throw (ClientletException) exception;
		}
		throw new ClientletException(exception);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.http.RequestHandler#processResponse(org.xamjwg.clientlet.ClientletResponse)
	 */
	public void processResponse(ClientletResponse response)
			throws ClientletException, IOException {
		ClientletResponseImpl cr = (ClientletResponseImpl) response;
		if(cr.isFromCache()) {
			this.jarFile = cr.getCacheFile();
			if(this.jarFile == null) {
				throw new IllegalStateException("JAR file cached but not there!");
			}
		}
		else {
			URL url = response.getResponseURL();
			if(RequestEngine.isFile(url)) {
				this.jarFile = new java.io.File(url.getPath());
			}
			else {
				if(cr.isCacheable()) {
					// Consume
					java.io.InputStream in = response.getInputStream();
					byte[] buffer = new byte[8192];
					while(in.read(buffer) != -1) {
						// nop
					}
					this.jarFile = cr.getCacheFile();
					if(this.jarFile == null) {
						throw new IllegalStateException("JAR not cached");
					}
				}
				else {
					// TODO: Probably a res: URL.
					throw new UnsupportedOperationException("JAR file in this type of URL not supported: " + url);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.http.RequestHandler#handleProgress(int, java.net.URL, int, int)
	 */
	public void handleProgress(ProgressType progressType, URL url, String method, int value, int max) {
		if(this.clientletUI != null) {
			this.clientletUI.updateProgress(url, method, progressType, value, max);
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.http.RequestHandler#getCacheFileSuffix()
	 */
	public String getCacheFileSuffix() {
		return ".jar";
	}	
}
