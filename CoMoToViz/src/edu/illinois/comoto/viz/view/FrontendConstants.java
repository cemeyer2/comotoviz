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

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * This holds constants such as labels, that whose text will actually appear on GUI elements.
 * <p/>
 * These constants should be properly capitalized, spaced, etc.
 */
public class FrontendConstants {

    //Window Titles
    public static final String WEBPAGE_DIALOG_TITLE = "Code Comparison";
    public static final String PROGRAM_TITLE = "CoMoTo Dynamic Visualization Tool";

    // Labels
    public static final String SOLUTION = "Solution";
    public static final String NETID = "Netid";
    public static final String ACTIVE_DIRECTORY_PASSWORD = "Active Directory Password";
    public static final String LOGIN = "Login";
    public static final String ABOUT = "About";
    public static final String COURSES = "Courses";
    public static final String PNG_IMAGES = "PNG Images";
    public static final String GRAPH_ML_FILES = "GraphML files";
    public static final String LAUNCH_TEXT_REPORT = "Launch Text Report";
    public static final String ANONYMOUS_GRAPH = "Anonymous Graph";
    public static final String INCLUDE_PARTNER_EDGES = "Include Partner Edges";
    public static final String INCLUDE_SOLUTION = "Include Solution";
    public static final String INCLUDE_SINGLETONS = "Include Singletons";
    public static final String INCLUDE_PAST_STUDENTS = "Include Past Students";
    public static final String MINIMUM_EDGE_WEIGHT = "Minimum Edge Weight";
    public static final String ZOOM = "Zoom";
    public static final String LOGIN_ERROR = "Login Error";
    public static final String INVALID_URL = "Invalid URL";
    public static final String SUBMISSION = "Submission";
    public static final String QUIT_PROMPT = "Quit?";
    public static final String GENERIC_ERROR_MESSAGE = "Error";
    public static final String FILE = "File";
    public static final String SEARCH_FOR_STUDENT = "Search for student";
    public static final String EXPORT_GRAPH_ML = "Export GraphML";
    public static final String EXPORT_IMAGE = "Export Image";
    public static final String QUIT = "Quit";
    public static final String HELP = "Help";
    public static final String ADD_DATA_SET = "Add Data Set";

    //Help & About Dialog Content
    public static final String ABOUT_TITLE = "<html><center><br/>CoMoTo Dynamic Visualization Tool</center><br/></html>";
    public static final String ABOUT_AUTHORS = "<html>Charlie Meyer<br/>Jon Tedesco<br/>Cinda Heeren<br/>Eric Shaffer<br/><br/></center></html>";
    public static final String ABOUT_COPYRIGHT = "<html><center>© 2011 The Board of Trustees at the University of Illinois<br/>" +
            "University of Illinois at Urbana-Champaign - College of Engineering<br/>" +
            "Department of Computer Science<br/><br/>" +
            "https://comoto.cs.illinois.edu/</center></html>";
    public static final String HELP_TITLE = ABOUT_TITLE;
    public static final String HELP_TEXT = "<html>Use the controls at the left of the window to select assignments<br/>" +
            "    and students. Clicking on an assignment will reload the graph with that<br/>" +
            "   assignment and clicking on a student will pan the graph to that student<br/><br/>" +
            "The controls on the right side of the window can be used to alter the graph.<br/><br/>" +
            "You can click and drag on any white space in the graph to pan the view, use <br/>" +
            "   the mouse wheel to zoom in and out, or right click and move the mouse up and down<br/>" +
            "   to zoom. Moving the mouse over a node or an edge will cause a status message to<br/>" +
            "   appear about that graph component. Clicking on an edge in the graph will launch<br/>" +
            "   the side by side code comparison for the match that that edge represents</html>";


    // Messages
    public static final String ASSIGNMENT_DOES_NOT_EXIST_MESSAGE = "The requested assignment does not exist";
    public static final String BUILDING_SIMILARITY_DATA_MESSAGE = "Building Similarity Data";
    public static final String BUILDING_SUBMISSION_DATA_MESSAGE = "Building Submission Data";
    public static final String EDGE_URL_INVALID_MESSAGE = "URL associated with edge is invalid";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid netid or AD password or your AD account may be locked";
    public static final String LOADING_ANALYSIS_DATA_MESSAGE = "Loading Analysis Data";
    public static final String LOADING_FILE_SETS_MESSAGE = "Loading File Sets";
    public static final String QUIT_CONFIRMATION_MESSAGE = "Are you sure you would like to quit?";
    public static final String PNG = "PNG";
    public static final String BUILDING_STUDENT_DATA_MESSAGE = "Building Student Data";
    public static final String BUILDING_MATCH_DATA_MESSAGE = "Building Match Data";
    public static final String FILTERING_DATA_MESSAGE = "Filtering Data";
}
