package edu.uiuc.cs.visualmoss.dataexport;

import java.io.File;
import java.io.OutputStream;

import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLWriter;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;

/**
 * exports a graph to GraphML format
 * @author chuck
 */
public class DataExport {

	private VisualMossGraph graph;
	
	public DataExport(VisualMossGraph graph)
	{
		this.graph = graph;
	}
	
	public void write(String filename) throws DataIOException
	{
		new GraphMLWriter().writeGraph(graph.getPrefuseGraph(), filename);
	}
	
	public void write(File f) throws DataIOException
	{
		new GraphMLWriter().writeGraph(graph.getPrefuseGraph(), f);
	}
	
	public void write(OutputStream os) throws DataIOException
	{
		new GraphMLWriter().writeGraph(graph.getPrefuseGraph(), os);
	}
}
