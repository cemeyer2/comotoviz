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

import edu.illinois.comoto.viz.controller.ActionListenerFactory;
import edu.illinois.comoto.viz.controller.EventListenerFactory;
import edu.illinois.comoto.viz.controller.KeyListenerFactory;
import edu.illinois.comoto.viz.controller.WindowListenerFactory;
import edu.illinois.comoto.viz.utility.CoMoToiVizAuthenticator;
import edu.illinois.comoto.viz.utility.LDAPAuth;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.net.Authenticator;

/**
 * A simple login dialog
 */
public class LoginDialog extends JDialog {

    // The GUI components
    private JTextField netIdField;
    private JPasswordField passwordField;

    // The credentials
    private String netId;
    private String password;

    /**
     * Builds a login dialog with a parent frame
     *
     * @param owner The parent frame
     */
    public LoginDialog(JFrame owner) {
        super(owner, true); //make this modal
        initialize();
    }

    /**
     * Builds a login dialog without a parent frame
     */
    public LoginDialog() {
        super();
        initialize();
    }

    /**
     * Constructs this dialog
     */
    private void initialize() {

        // Create the empty window with the title
        this.setTitle(FrontendConstants.LOGIN);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setPreferredSize(new Dimension(FrontendConstants.LOGIN_DIALOG_WIDTH, FrontendConstants.LOGIN_DIALOG_HEIGHT));

        // Add the netid and password fields
        netIdField = new JTextField();
        netIdField.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        TitledBorder b1 = BorderFactory.createTitledBorder(FrontendConstants.NETID);
        b1.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
        netIdField.setBorder(b1);
        passwordField = new JPasswordField();
        TitledBorder b2 = BorderFactory.createTitledBorder(FrontendConstants.ACTIVE_DIRECTORY_PASSWORD);
        b2.setTitleFont(BackendConstants.COMPONENT_LABEL_FONT);
        passwordField.setBorder(b2);

        // Add the login button and its key listener
        JButton loginButton = new JButton(FrontendConstants.LOGIN);
        loginButton.setFont(BackendConstants.COMPONENT_LABEL_FONT);

        // Add these components to the window
        panel.add(netIdField);
        panel.add(passwordField);
        panel.add(loginButton);
        this.setContentPane(panel);

        // Add the window listener
        EventListenerFactory windowListenerFactory = new WindowListenerFactory();
        this.addWindowListener((WindowListener) windowListenerFactory.getEventListener(BackendConstants.LOGIN_WINDOW));

        // Add action listeners to each GUI components
        EventListenerFactory actionListenerFactory = new ActionListenerFactory();
        ActionListener loginActionListener = (ActionListener) actionListenerFactory.getEventListener(BackendConstants.LOGIN, this);
        loginButton.addActionListener(loginActionListener);
        netIdField.addActionListener(loginActionListener);
        passwordField.addActionListener(loginActionListener);

        // Add a key listener to each GUI components
        EventListenerFactory keyListenerFactory = new KeyListenerFactory();
        KeyListener loginKeyListener = (KeyListener) keyListenerFactory.getEventListener(BackendConstants.LOGIN, this);
        netIdField.addKeyListener(loginKeyListener);
        passwordField.addKeyListener(loginKeyListener);
        loginButton.addKeyListener(loginKeyListener);

        // Change the program icon
        Image programIcon = Toolkit.getDefaultToolkit().getImage(BackendConstants.PROGRAM_ICON_PATH);
        setIconImage(programIcon);

        // Display the dialog
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public String getNetId() {
        return netId;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Login using the credentials from the fields
     */
    public void login() {
        netId = netIdField.getText();
        password = new String(passwordField.getPassword());

        // changed this to use direct LDAP auth rather than query the http server to auth since on unsuccessful auth
        // against the http server, it keeps trying until the max redirects limit is reached, which unfortunately
        // caused the UIUC AD to lock the account, this way we only hit the AD once and won't lock out the account
        // unless many unsuccessful tries are made
        boolean auth = LDAPAuth.authenticate(netId, password);

        if (auth) {
            CoMoToiVizAuthenticator authenticator = new CoMoToiVizAuthenticator();
            authenticator.setDefaultAuthentication(netId, password);
            Authenticator.setDefault(authenticator);

            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, FrontendConstants.INVALID_CREDENTIALS_MESSAGE, FrontendConstants.LOGIN_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
