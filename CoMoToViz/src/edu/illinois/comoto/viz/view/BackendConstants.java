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

package edu.illinois.comoto.viz.view;

import prefuse.util.FontLib;

import java.awt.*;

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * This class holds constants that will be used by the backend for
 */

public class BackendConstants {

    // Resource locations
    public static final String PROGRAM_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/illinois.png";
    public static final String OPEN_NODE_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/blue_node.png";
    public static final String CLOSED_NODE_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/gray_node.png";
    public static final String LEAF_NODE_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/small_gray_node.png";

    // Field identifiers
    public static final String IS_PARTNER = "isPartner";
    public static final String IS_SOLUTION = "isSolution";
    public static final String NETID = "netid";
    public static final String LINK = "link";
    public static final String SCORE1 = "score1";
    public static final String SCORE2 = "score2";
    public static final String SEASON = "season";
    public static final String SUBMISSION_ID = "submission_id";
    public static final String YEAR = "year";
    public static final String CURRENT_SEMESTER = "currentSemester";

    //Font constants
    public static final String FONT_NAME = "Verdana";
    public static final Font COMPONENT_LABEL_FONT = FontLib.getFont(FONT_NAME, 12);
    public static final Font NODE_LABEL_FONT = FontLib.getFont(FONT_NAME, 12);
    public static final Font STATUS_LABEL_FONT = FontLib.getFont(FONT_NAME, 18);
    public static final Font HELP_TITLE_FONT = FontLib.getFont(FONT_NAME, Font.BOLD, 18);

    // GUI component identifiers
    public static final String ASSIGNMENT_CHOOSER_CLICKED = "assignmentChooser";
    public static final String TLS = "TLS";
    public static final String WEIGHT = "weight";
    public static final String GRAPH = "graph";
    public static final String COLOR = "color";
    public static final String EDGES = "edges";
    public static final String LAYOUT = "layout";
    public static final String NODES = "nodes";
    public static final String FONT = "font";
    public static final String PNG = "PNG";
    public static final String PSEUDONYM = "pseudonym";
}
