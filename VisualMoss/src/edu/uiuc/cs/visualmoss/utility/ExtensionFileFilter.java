package edu.uiuc.cs.visualmoss.utility;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.*;

/* ImageFilter.java is used by FileChooserDemo2.java. */
public class ExtensionFileFilter extends FileFilter {

	ArrayList<String> extensions;
	String description;
	
	public ExtensionFileFilter(String description)
	{
		extensions = new ArrayList<String>();
		this.description = description;
	}
	
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String[] split;
		try {
			split = f.getCanonicalPath().split(".");
			if(split.length == 0)
			{
				return false;
			}
			String extension = split[split.length-1];
	        if (extension != null) {
	            return extensions.contains(extension.toLowerCase());
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    public String getDescription() {
        return description;
    }
    
    public void addExtension(String ext)
    {
    	extensions.add(ext.toLowerCase());
    }
}

