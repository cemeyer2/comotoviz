package edu.uiuc.cs.visualmoss;

import prefuse.util.FontLib;

import java.awt.*;
import java.util.Calendar;

public class VisualMossConstants {

	//Application constants
	public static final String VERSION = "0.1";

	//Font constants
	public static final String FONT_NAME = "Verdana";
	public static final Font COMPONENT_LABEL_FONT = FontLib.getFont(FONT_NAME, 12);
	public static final Font NODE_LABEL_FONT = FontLib.getFont(FONT_NAME, 12);
	public static final Font STATUS_LABEL_FONT = FontLib.getFont(FONT_NAME, 18);
	public static final Font HELP_TITLE_FONT = FontLib.getFont(FONT_NAME, Font.BOLD, 18);

    //API constants
    public static final String API_USER_NAME = "username";
    public static final String API_PASSWORD = "password";

	//Naming constants
	public static final String SOLUTION_NODE_LABEL = "[solution]";

	//URL Constants
	public static final String URL_BASE = "https://comoto.cs.illinois.edu/api/";
	public static final String TEXT_REPORT_URL = "report.html";

	//Help Constants
	public static final String ABOUT_TITLE = "<html>vPlag "+VERSION+"</html>";
	public static final String ABOUT_AUTHORS = "<html>Charlie Meyer<br/>Kevin Phillips<br/>Michael Hines</html>";
	public static final String ABOUT_COPYRIGHT = "<html>© 2009 The Board of Trustees at the University of Illinois<br/>"+
                                                "University of Illinois at Urbana-Champaign - College of Engineering<br/>"+
                                                "Department of Computer Science<br/>"+
                                                "https://maggie.cs.illinois.edu/trac/VisualMoss</html>";
	public static final String HELP_TITLE = 	ABOUT_TITLE;
    public static final String HELP_TEXT =  "<html>Use the controls at the left of the window to select assignments<br/>"+
                                            "and students. Clicking on an assignment will reload the graph with that<br/>"+
                                            "assignment and clicking on a student will pan the graph to that student<br/><br/>"+
                                            "The controls on the right side of the window can be used to alter the graph.<br/><br/>"+
                                            "You can click and drag on any white space in the graph to pan the view, use <br/>"+
                                            "the mouse wheel to zoom in and out, or right click and move the mouse up and down<br/>"+
                                            "to zoom. Moving the mouse over a node or an edge will cause a status message to<br/>"+
                                            "appear about that graph component. Clicking on an edge in the graph will launch<br/>"+
                                            "the side by side code comparison for the match that that edge represents</html>";

    //Class defaults
    public static final String CS225 = "CS225";
    public static final String MP3 = "MP3";

    //Graph constants
    public static final String IS_PARTNER = "isPartner";
    public static final String IS_SOLUTION = "isSolution";
    public static final String NETID = "netid";
    public static final String LINK = "link";
    public static final String PSEUDONYM = "pseudonym";
    public static final String SCORE1 = "score1";
    public static final String SCORE2 = "score2";
    public static final String SEASON = "season";
    public static final String SUBMISSION_ID = "submission_id";
    public static final String WEIGHT = "weight";
    public static final String YEAR = "year";
    public static final String CURRENT_SEASON = getCurrentSeason();
    public static final String CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR)+"";
    private static final String getCurrentSeason(){
        int month = Calendar.getInstance().get(Calendar.MONTH);
        if(month < 6){
            return "Spring";
        } else if(month < 9) {
            return "Summer";
        } else {
            return "Fall";
        }
    }
}
