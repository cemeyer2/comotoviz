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

package edu.uiuc.cs.visualmoss.gui.login;

import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.net.VisualMossAuthenticator;
import edu.uiuc.cs.visualmoss.utility.ldap.LDAPAuth;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.Authenticator;

public class LoginDialog extends JDialog implements ActionListener, WindowListener, KeyListener {
    private final int DIALOG_WIDTH = 400;
    private final int DIALOG_HEIGHT = 200;

    private JTextField netidField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private String netId;
    private String password;

    public LoginDialog(JFrame owner) {
        super(owner, true); //make this modal
        init();
    }

    public LoginDialog() {
        super();
        init();
    }

    private void init() {
        this.setTitle("Login");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        netidField = new JTextField();
        netidField.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        TitledBorder b1 = BorderFactory.createTitledBorder("Netid");
        b1.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        netidField.setBorder(b1);

        passwordField = new JPasswordField();
        TitledBorder b2 = BorderFactory.createTitledBorder("Active Directory Password");
        b2.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        passwordField.setBorder(b2);

        loginButton = new JButton("Login");
        loginButton.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        loginButton.addActionListener(this);

        panel.add(netidField);
        panel.add(passwordField);
        panel.add(loginButton);

        this.setContentPane(panel);
        this.addWindowListener(this);

        netidField.addActionListener(this);
        passwordField.addActionListener(this);
        loginButton.addActionListener(this);
        netidField.addKeyListener(this);
        passwordField.addKeyListener(this);

        // Change the program icon
        Image programIcon = Toolkit.getDefaultToolkit().getImage("VisualMoss/uiuc.png");
        setIconImage(programIcon);

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent arg0) {
        login();
    }

    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowClosing(WindowEvent arg0) {
        System.exit(0);
    }

    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public String getNetId() {
        return netId;
    }

    public String getPassword() {
        return password;
    }

    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            login();
        }
    }

    private void login() {
        netId = netidField.getText();
        password = new String(passwordField.getPassword());

        //changed this to use direct LDAP auth rather than query the http server to auth since on unsuccessful auth
        //against the http server, it keeps trying until the max redirects limit is reached, which unfortunately
        //caused the UIUC AD to lock the account, this way we only hit the AD once and won't lock out the account
        //unless many unsuccessful tries are made
        boolean auth = LDAPAuth.authenticate(netId, password);

        if (auth) {
            VisualMossAuthenticator authenticator = new VisualMossAuthenticator();
            authenticator.setDefaultAuthentication(netId, password);
            Authenticator.setDefault(authenticator);

            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid netid or AD password or your AD account may be locked", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void keyReleased(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
