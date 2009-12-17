package edu.uiuc.cs.visualmoss.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

public class CampusIPCheck 
{
	public static void checkCampusIP()
	{
		try
		{
			Class.forName(VisualMossConstants.DB_DRIVER).newInstance();
	        Connection conn = DriverManager.getConnection(VisualMossConstants.DB_URL, VisualMossConstants.DB_USERNAME, VisualMossConstants.DB_PASSWORD);
	        Statement st = conn.createStatement();
	        st.close();
	        conn.close();
		}
		catch(Exception ie)
		{
			JOptionPane.showMessageDialog(null, "You must have a UIUC campus IP address to use this application. Please obtain a campus IP using the VPN", "Address Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
}
