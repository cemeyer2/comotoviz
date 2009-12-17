package edu.uiuc.cs.visualmoss.graph;

import java.io.File;

import prefuse.data.io.DataIOException;

public class VisualMossGraphDriver {
	public static void main(String[] args) throws DataIOException
	{
		VisualMossGraph vmg = new VisualMossGraph(new File("graph.xml"), "CS225", "MP3");
		
	}
}
