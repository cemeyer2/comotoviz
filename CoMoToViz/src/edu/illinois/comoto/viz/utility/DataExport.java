/*
 * University of Illinois/NCSA
 * Open Source License
 *
 * Copyright (c) 2011 University of Illinois at Urbana-Champaign.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal with the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimers.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimers in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     * Neither the names of the CoMoTo Project team, the University of
 *       Illinois at Urbana-Champaign, nor the names of its contributors
 *       may be used to endorse or promote products derived from this
 *       Software without specific prior written permission.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
 */

package edu.illinois.comoto.viz.utility;

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLWriter;

import java.io.File;
import java.io.OutputStream;

/**
 * Exports a visual moss graph to GraphML format. Once exported into GraphML format, the file can be given as input to
 * initialize the graph data again.
 */
public class DataExport {

    private Graph graph;

    /**
     * Simple constructor that builds the data export functionality from an input <code>GraphToBeRemoved</code> structure
     *
     * @param graph The graph to export
     */
    public DataExport(Graph graph) {
        this.graph = graph;
    }

    /**
     * Writes the graph in GraphML format to some file via Prefuse's build-in export functionality
     *
     * @param filename The name of the file to which to write the graph
     * @throws DataIOException On errors writing to file as thrown by the <code>GraphMLWriter</code>
     * @see GraphMLWriter
     */
    public void write(String filename) throws DataIOException {
        new GraphMLWriter().writeGraph(graph, filename);
    }

    /**
     * Writes the graph in GraphML format to some file via Prefuse's build-in export functionality
     *
     * @param file The file to which to write the graph
     * @throws DataIOException On errors writing to file as thrown by the <code>GraphMLWriter</code>
     * @see GraphMLWriter
     */
    public void write(File file) throws DataIOException {
        new GraphMLWriter().writeGraph(graph, file);
    }

    /**
     * Writes the graph in GraphML format to some output stream via Prefuse's build-in export functionality
     *
     * @param outputStream The stream to which to write the graph
     * @throws DataIOException On errors writing to file as thrown by the <code>GraphMLWriter</code>
     * @see GraphMLWriter
     */
    public void write(OutputStream outputStream) throws DataIOException {
        new GraphMLWriter().writeGraph(graph, outputStream);
    }
}
