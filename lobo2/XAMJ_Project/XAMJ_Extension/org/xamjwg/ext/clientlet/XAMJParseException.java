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

import org.lobobrowser.clientlet.ClientletException;
import org.xmlpull.v1.*;

/**
 *  
 */
public class XAMJParseException extends ClientletException
{
	public XAMJParseException(XmlPullParserException xppe, String message)
	{
		super(xppe.getLineNumber() + ": " + message, xppe);
	}

	public XAMJParseException(XmlPullParserException xppe)
	{
		super(xppe.getLineNumber() + ": " + xppe.getMessage(), xppe);
	}

	public XAMJParseException(XmlPullParser xpp, String message)
	{
		super(xpp.getLineNumber() + ": " + message);
	}

	public XAMJParseException(XmlPullParser xpp, Exception rootCause)
	{
		super(xpp.getLineNumber() + ": " + rootCause.getMessage(), rootCause);
	}

	public XAMJParseException(String message)
	{
		super(message);
	}
}
