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


import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * The help dialog
 */
public class HelpDialog extends JDialog {


    public HelpDialog() {
        super();
        this.setModal(true);

        // Add content
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(FrontendConstants.HELP_TITLE);
        JLabel helpLabel = new JLabel(FrontendConstants.HELP_TEXT);
        titleLabel.setFont(BackendConstants.HELP_TITLE_FONT);
        helpLabel.setFont(BackendConstants.COMPONENT_LABEL_FONT);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        helpLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setHorizontalTextPosition(JLabel.CENTER);
        helpLabel.setHorizontalTextPosition(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(helpLabel, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(FrontendConstants.HELP_DIALOG_WIDTH, FrontendConstants.HELP_DIALOG_HEIGHT));

        // Change the program icon
        setIconImage(BackendConstants.PROGRAM_IMAGE);

        // Display it
        this.setContentPane(panel);
        this.pack();
        this.setTitle(FrontendConstants.HELP);
        this.setVisible(true);
    }
}
