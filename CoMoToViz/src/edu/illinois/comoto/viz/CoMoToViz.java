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

package edu.illinois.comoto.viz;

import edu.illinois.comoto.api.utility.Cache;
import edu.illinois.comoto.viz.utility.Pair;
import edu.illinois.comoto.viz.utility.VisualMossException;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.LoginDialog;
import edu.illinois.comoto.viz.view.MainWindow;
import org.lobobrowser.main.ExtensionManager;
import org.lobobrowser.main.PlatformInit;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class CoMoToViz {


    /**
     * Entry point for the CoMoTo dynamic visualization
     *
     * @param args  Command line arguments to launch the program
     * @throws VisualMossException  On errors with the viz itself
     */
    public static void main(String[] args) throws VisualMossException {

        setDefaultFont();

        // Enable object caching
        Cache.setEnabled(true);

        // Load all componenets for the lobo browser
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

        // If the system is not linux, use the system look and feel
        String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
        if (!System.getProperty("os.name").contains("Linux")) {
            try {
                // Set System look and feel
                UIManager.setLookAndFeel(systemLookAndFeel);
            } catch (UnsupportedLookAndFeelException e) {}
            catch (ClassNotFoundException e) {}
            catch (InstantiationException e) {}
            catch (IllegalAccessException e) {}
        }

        // Show the login dialog and get the credentials
        LoginDialog loginDialog = new LoginDialog(null);
        String netId = loginDialog.getNetId();
        String password = loginDialog.getPassword();
        Pair<String, String> activeDirectoryCredentials = new Pair<String, String>(netId, password);

        // Launch the main window
        MainWindow window = new MainWindow(activeDirectoryCredentials);
        window.pack();
        window.setVisible(true);
        window.setExtendedState(window.getExtendedState() | Frame.MAXIMIZED_BOTH);

        // Handle command-line arguments
        if (args.length == 1) {
            try {
                int assignmentId = Integer.parseInt(args[0]);
                window.changeAssignment(assignmentId);
            } catch (NumberFormatException nfe) {}
        }
    }

    //adopted from http://www.rgagnon.com/javadetails/java-0335.html
    private static void setDefaultFont() {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, BackendConstants.COMPONENT_LABEL_FONT);
            }
        }
    }
}
