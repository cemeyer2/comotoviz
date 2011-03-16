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

import org.lobobrowser.gui.BrowserPanel;
import org.lobobrowser.gui.FramePanel;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

public class WebPageDialog extends JDialog {
    private static final int DIALOG_WIDTH = 950;
    private static final int DIALOG_HEIGHT = 650;

    private BrowserPanel browserPanel;
    private FramePanel framePanel;

    /**
     * Creates a modal webpage dialog
     *
     * @param owner          The parent frame for this dialog
     * @param url            The URL to which to launch this
     * @param showNavigation Flag to show or hide navigation
     * @throws MalformedURLException When this is launched with an invalid URL
     */
    public WebPageDialog(Frame owner, String url, boolean showNavigation) throws MalformedURLException {
        super(owner, true);
        initialize(url, showNavigation);
    }

    /**
     * Creates a modeless webpage dialog
     *
     * @param url            The URL to which to launch this
     * @param showNavigation Flag to show or hide navigation
     * @throws MalformedURLException When this is launched with an invalid URL
     */
    public WebPageDialog(String url, boolean showNavigation) throws MalformedURLException {
        super();
        initialize(url, showNavigation);
    }

    /**
     * Initialize this dialog
     *
     * @param url            The URL to launch
     * @param showNavigation Flag to show/hide navigation
     * @throws MalformedURLException When this is launched with an invalid URL
     */
    private void initialize(String url, boolean showNavigation) throws MalformedURLException {

        // Change the program icon and setup system properties of dialog
        setIconImage(BackendConstants.PROGRAM_IMAGE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

        // Set the title of the dialog
        setTitle(FrontendConstants.WEBPAGE_DIALOG_TITLE);

        // Display the navigation?
        if (showNavigation) {
            browserPanel = new BrowserPanel();
            this.getContentPane().add(browserPanel);
        } else {
            framePanel = new FramePanel();
            this.getContentPane().add(framePanel);
        }

        // The URL to launch this to
        setUrl(url);

        // Show it
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    /**
     * Attempts to launch this browser dialog to this URL
     *
     * @param url The URL to which to launch
     * @throws MalformedURLException When this is given an invalid URL
     */
    public void setUrl(String url) throws MalformedURLException {
        if (browserPanel != null) {
            browserPanel.navigate(url);
        } else {
            framePanel.navigate(url);
        }
    }
}