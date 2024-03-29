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
 * Created on Jun 23, 2005
 */
package org.xamjwg.ext;

import java.util.*;
import org.xamjwg.ext.*;

/**
 * @author J. H. S.
 */
public class ArchiveCollection {
	private final Collection archiveInfos;
	
	/**
	 * 
	 */
	public ArchiveCollection() {
		this.archiveInfos = new LinkedList();
	}

	/**
	 * 
	 */
	public ArchiveCollection(Collection archiveInfos) {
		this.archiveInfos = archiveInfos;
	}
	
//	public void addArchiveInfo(ArchiveInfo ainfo) {
//		synchronized(this) {
//			this.archiveInfos.add(ainfo);
//		}
//	}
	
	private ArchiveClassLoader classLoader;
	
	public ClassLoader getClassLoader() throws java.io.IOException {
		synchronized(this) {
			if(this.archiveInfos.size() == 0) {
				return this.getClass().getClassLoader();
			}				 
			if(this.classLoader == null) {
				this.classLoader = new ArchiveClassLoader(this.archiveInfos);
			}
			return this.classLoader;
		}
	}
	
	public Iterator iterator() {
		return this.archiveInfos.iterator();
	}
}
