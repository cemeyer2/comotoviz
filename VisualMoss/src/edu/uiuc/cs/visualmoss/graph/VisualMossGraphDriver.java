package edu.uiuc.cs.visualmoss.graph;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import prefuse.data.io.DataIOException;

import java.io.File;

/**
 * A handle on the visual moss graph class to test in creation
 */
public class VisualMossGraphDriver {
	public static void main(String[] args) throws DataIOException {
		new VisualMossGraph(new File("graph.xml"), VisualMossConstants.CS225, VisualMossConstants.MP3);
	}
}
