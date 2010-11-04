package edu.uiuc.cs.visualmoss.dataexport;

import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLWriter;

import java.io.File;
import java.io.OutputStream;

/**
 * Exports a visual moss graph to GraphML format. Once exported into GraphML format, the file can be given as input to
 *  initialize the graph data again.
 *
 * @see
 * @see edu.uiuc.cs.visualmoss.graph.VisualMossGraph
 */
public class DataExport {

	private VisualMossGraph graph;

    /**
     * Simple constructor that builds the data export functionality from an input <code>VisualMossGraph</code> structure
     *
     * @param graph The graph to export
     * @see edu.uiuc.cs.visualmoss.graph.VisualMossGraph
     */
	public DataExport(VisualMossGraph graph) {
		this.graph = graph;
	}
	
     /**
      * Writes the graph in GraphML format to some file via Prefuse's build-in export functionality
      *
      * @param filename  The name of the file to which to write the graph
      * @throws DataIOException  On errors writing to file as thrown by the <code>GraphMLWriter</code>
      * @see GraphMLWriter
      */
	public void write(String filename) throws DataIOException {
		new GraphMLWriter().writeGraph(graph.getPrefuseGraph(), filename);
	}
	
    /**
     * Writes the graph in GraphML format to some file via Prefuse's build-in export functionality
     *
     * @param file  The file to which to write the graph
     * @throws DataIOException  On errors writing to file as thrown by the <code>GraphMLWriter</code>
     * @see GraphMLWriter
     */
	public void write(File file) throws DataIOException {
		new GraphMLWriter().writeGraph(graph.getPrefuseGraph(), file);
	}

    /**
     * Writes the graph in GraphML format to some output stream via Prefuse's build-in export functionality
     *
     * @param outputStream  The stream to which to write the graph
     * @throws DataIOException  On errors writing to file as thrown by the <code>GraphMLWriter</code>
     * @see GraphMLWriter
     */
	public void write(OutputStream outputStream) throws DataIOException {
		new GraphMLWriter().writeGraph(graph.getPrefuseGraph(), outputStream);
	}
}
