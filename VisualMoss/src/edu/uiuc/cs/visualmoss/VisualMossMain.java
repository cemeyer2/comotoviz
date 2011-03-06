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

package edu.uiuc.cs.visualmoss;

import edu.illinois.comoto.api.utility.Cache;
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
import java.util.Enumeration;

public class VisualMossMain {

    public static void main(String[] args) throws VisualMossException, MalformedURLException, DataIOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {

        setDefaultFont();

        Cache.setEnabled(true); //enable object caching so calls to the api that are repeated are loaded from cache rather than from the api again

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

        // If the system is not linux
        String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();

        if (!systemLookAndFeel.contains("GTK")) {
            try {
                // Set System look and feel
                UIManager.setLookAndFeel(systemLookAndFeel);

            } catch (UnsupportedLookAndFeelException e) {
                // handle exception
            } catch (ClassNotFoundException e) {
                // handle exception
            } catch (InstantiationException e) {
                // handle exception
            } catch (IllegalAccessException e) {
                // handle exception
            }
        }

        LoginDialog loginDialog = new LoginDialog(null);
        String netId = loginDialog.getNetId();
        String password = loginDialog.getPassword();
        Pair<String, String> activeDirectoryCredentials = new Pair<String, String>(netId, password);

        VisualMossLayout window = new VisualMossLayout(activeDirectoryCredentials);
        window.pack();
        window.setVisible(true);
        window.setExtendedState(window.getExtendedState() | Frame.MAXIMIZED_BOTH);

        if (args.length == 1) { //if an assignment id was passed in from comoto
            try {
                int assignmentId = Integer.parseInt(args[0]);
                window.changeAssignment(assignmentId);
            } catch (NumberFormatException nfe) {
                //fail silently
            }
        }
    }

    //adopted from http://www.rgagnon.com/javadetails/java-0335.html
    private static void setDefaultFont() {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, VisualMossConstants.COMPONENT_LABEL_FONT);
            }
        }
    }
}
