package edu.uiuc.cs.visualmoss;

import edu.uiuc.cs.visualmoss.exceptions.VisualMossException;
import edu.uiuc.cs.visualmoss.gui.layout.VisualMossLayout;
import edu.uiuc.cs.visualmoss.gui.login.LoginDialog;
import edu.uiuc.cs.visualmoss.utility.Pair;
import org.lobobrowser.main.ExtensionManager;
import org.lobobrowser.main.PlatformInit;
import prefuse.data.io.DataIOException;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.sql.SQLException;

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
        String netId = loginDialog.getNetId();
        String password = loginDialog.getPassword();
        Pair<String, String> activeDirectoryCredentials = new Pair<String, String>(netId, password);

        VisualMossLayout window = new VisualMossLayout(activeDirectoryCredentials);
        window.pack();
        window.setVisible(true);
        window.setExtendedState(window.getExtendedState() | Frame.MAXIMIZED_BOTH);
    }
}
