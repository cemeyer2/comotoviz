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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * This class holds constants that will be used by the backend for
 */

public class BackendConstants {


    private BackendConstants() {
    }

    //Icons
    public static Icon CLOSED_NODE_ICON = null;
    public static Icon LEAF_NODE_ICON = null;
    public static Icon OPEN_NODE_ICON = null;
    public static Image PROGRAM_IMAGE = null;

    // Resource locations
    private static final String CLOSED_NODE_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/gray_node.png";
    private static final String LEAF_NODE_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/small_gray_node.png";
    private static final String OPEN_NODE_ICON_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/blue_node.png";
    private static final String PROGRAM_IMAGE_PATH = "CoMoToViz/src/edu/illinois/comoto/viz/resources/illinois.png";
    private static final URL CLOSED_NODE_ICON_RESOURCE = BackendConstants.class.getResource("/gray_node.png");
    private static final URL LEAF_NODE_ICON_RESOURCE = BackendConstants.class.getResource("/small_gray_node.png");
    private static final URL OPEN_NODE_ICON_RESOURCE = BackendConstants.class.getResource("/blue_node.png");
    private static final URL PROGRAM_IMAGE_RESOURCE = BackendConstants.class.getResource("/illinois.png");


    //icon initialization
    static {
        //for each icon, check if it can be loaded from the jar, if not, load from disk
        if (CLOSED_NODE_ICON_RESOURCE != null) {
            CLOSED_NODE_ICON = new ImageIcon(CLOSED_NODE_ICON_RESOURCE);
        } else {
            CLOSED_NODE_ICON = new ImageIcon(CLOSED_NODE_ICON_PATH);
        }

        if (LEAF_NODE_ICON_RESOURCE != null) {
            LEAF_NODE_ICON = new ImageIcon(LEAF_NODE_ICON_RESOURCE);
        } else {
            LEAF_NODE_ICON = new ImageIcon(LEAF_NODE_ICON_PATH);
        }

        if (OPEN_NODE_ICON_RESOURCE != null) {
            OPEN_NODE_ICON = new ImageIcon(OPEN_NODE_ICON_RESOURCE);
        } else {
            OPEN_NODE_ICON = new ImageIcon(OPEN_NODE_ICON_PATH);
        }

        if (PROGRAM_IMAGE_RESOURCE != null) {
            PROGRAM_IMAGE = Toolkit.getDefaultToolkit().getImage(PROGRAM_IMAGE_RESOURCE);
        } else {
            PROGRAM_IMAGE = Toolkit.getDefaultToolkit().getImage(PROGRAM_IMAGE_PATH);
        }
    }

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
    public static final String STUDENT_ID = "student_id";
    public static final String MOSSMATCH_ID = "mossmatch_id";
    public static final String CONNECTED_TO_PAST = "connectedToPast";
    public static final String MAX_DEGREE = "max_degree";

    //Font constants
    public static final String FONT_NAME = "Verdana";
    public static final Font COMPONENT_LABEL_FONT = FontLib.getFont(FONT_NAME, 12);
    public static final Font NODE_LABEL_FONT = FontLib.getFont(FONT_NAME, 12);
    public static final Font STATUS_LABEL_FONT = FontLib.getFont(FONT_NAME, 24);
    public static final Font HELP_TITLE_FONT = FontLib.getFont(FONT_NAME, Font.BOLD, 18);

    // GUI component & action identifiers
    public static final String TLS = "TLS";
    public static final String WEIGHT = "weight";
    public static final String GRAPH = "graph";
    public static final String LAYOUT = "layout";
    public static final String NODES = "nodes";
    public static final String FONT = "font";
    public static final String STROKE = "stroke";
    public static final String SIZE = "size";
    public static final String DISTORTION = "distortion";
    public static final String PNG = "png";
    public static final String XML = "xml";
    public static final String ASSIGNMENT_CHOOSER = "assignmentChooser";
    public static final String MAIN_WINDOW = "mainWindow";
    public static final String EXPORT_GRAPH = "exportGraph";
    public static final String EXPORT_IMAGE = "exportImage";
    public static final String QUIT = "quit";
    public static final String ABOUT = "about";
    public static final String HELP = "help";
    public static final String SEARCH_STUDENTS = "searchStudents";
    public static final String LOGIN_WINDOW = "loginWindow";
    public static final String LOGIN = "login";
    public static final String GTK = "GTK";
    public static final String PSEUDONYM = "pseudonym";
    public static final String PAST_STUDENTS = "pastStudents";
    public static final String PARTNER_EDGES = "partnerEdges";
    public static final String SHOW_SOLUTION = "showSolution";
    public static final String ANONYMOUS_GRAPH = "anonymousGraph";
    public static final String LAUNCH_TEXT_REPORT = "launchTextReport";
    public static final String THRESHOLD = "threshold";
    public static final String ZOOM = "zoom";
    public static final String SINGLETONS = "singletons";
    public static final String SIM = "sim";
    public static final String LOGGING = "logging";

    public static final String EDGES = "edges";
    public static final String COLOR = "color";

    public static final String LINUX = "Linux";
    public static final String OS_PROPERTY = "os.name";

    public static final double DEFAULT_MINIMUM_EDGE_WEIGHT = FrontendConstants.DEFAULT_MINIMUM_EDGE_WEIGHT;
    public static final boolean DEFAULT_SHOW_SINGLETONS = FrontendConstants.DEFAULT_SHOW_SINGLETONS;
    public static final boolean DEFAULT_SHOW_SOLUTION = FrontendConstants.DEFAULT_SHOW_SOLUTION;
    public static final boolean DEFAULT_INCLUDE_PAST_STUDENTS = FrontendConstants.DEFAULT_INCLUDE_PAST_STUDENTS;
    public static final boolean DEFAULT_INCLUDE_PARTNERS = FrontendConstants.DEFAULT_INCLUDE_PARTNERS;
    public static final boolean DEFAULT_SHOW_BUILD_PROGRESS = true;

    //forces, taken from prefuse source
    //we will modify these to suit our needs, for most of them, i think some kind of interpolation
    //based on attributes of the graph should give a nice value

    //the coeff for a given spring in the graph is calculated by determining the maximum between
    //the degree of the source node and target node that are attached to the spring, then determining
    //the proportion that that number is compared to the node in the graph with maximum degree, then interpolating
    //1-that percentage between the max and min constants defined below
    public static final float MAX_SPRING_COEFF = 1E-4f;
    public static final float MIN_SPRING_COEFF = 1E-7f;
    public static final int SPRING_COEFF_PARAM_INDEX = 0;
    //spring length is similarly calculated to spring coeff, except higher edges connecting to higher degree
    //nodes have a longer spring length (inverse of how coeff is calculated)
    public static final float MIN_SPRING_LENGTH = 50;
    public static final float MAX_SPRING_LENGTH = 250;
    public static final float DEFAULT_SPRING_LENGTH = 50;
    public static final int SPRING_LENGTH_PARAM_INDEX = 1;
    //the drag coeff is calculated by determining the percentage of nodes are in the displayed graph against
    //the number of nodes in the graph before it was filtered by the predicate, then taking 1-that percentage
    //and interpolating it between the max and min values defined below
    public static final int DRAG_FORCE_COEFF_PARAM_INDEX = 0;
    public static final float MIN_DRAG_COEFF = 0.005f;
    public static final float MAX_DRAG_COEFF = 0.01f;
    public static final float MIN_GRAV_CONSTANT = -10f;
    public static final float MAX_GRAV_CONSTANT = 10f;
    public static final float MIN_DISTANCE = -1f;
    public static final float MAX_DISTANCE = 500f;
    public static final float MIN_THETA = 0.0f;
    public static final float MAX_THETA = 1.0f;
    public static final int NBODY_FORCE_GRAV_CONSTANT_PARAM_INDEX = 0;
    public static final int NBODY_FORCE_MIN_DISTANCE_PARAM_INDEX = 1;
    public static final int NBODY_FORCE_BARNES_HUT_THETA_PARAM_INDEX = 2;
    public static final float DEFAULT_THETA = 0.9f;
    public static final float DEFAULT_GRAV_CONSTANT = -1.0f;
    public static final float DEFAULT_DISTANCE = -1f;
}
