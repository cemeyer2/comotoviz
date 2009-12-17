package edu.uiuc.cs.visualmoss;

import java.awt.Frame;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.lobobrowser.main.ExtensionManager;
import org.lobobrowser.main.PlatformInit;

import prefuse.data.io.DataIOException;
import edu.uiuc.cs.visualmoss.exceptions.VisualMossException;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;
import edu.uiuc.cs.visualmoss.gui.login.LoginDialog;

public class VisualMossMain {

	public static void main(String[] args) throws VisualMossException, MalformedURLException, DataIOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {
		try {
			PlatformInit init = PlatformInit.getInstance();
			//init.initLogging(false);
			ExtensionManager.getInstance().initExtensions();
			init.initOtherProperties();
			init.initProtocols();
			init.initSecurity();
	    } catch (Exception ex) {
	    	JOptionPane.showMessageDialog(null, "Error initializing lobo browser component.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
	    	throw new VisualMossException("Lobo did not initialize properly.", ex);
	    }
	    
		LoginDialog loginDialog = new LoginDialog(null);

		VisualMossLayout window = new VisualMossLayout();
	    window.pack();
	    window.setVisible(true);
	    window.setExtendedState(window.getExtendedState() | Frame.MAXIMIZED_BOTH);
	}
}
