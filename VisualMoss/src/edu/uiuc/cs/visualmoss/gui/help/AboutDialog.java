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

package edu.uiuc.cs.visualmoss.gui.help;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
    private final int DIALOG_WIDTH = 500;
    private final int DIALOG_HEIGHT = 300;

    public AboutDialog() {
        super();
        this.setModal(true);
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel titleLabel = new JLabel(VisualMossConstants.ABOUT_TITLE);
        JLabel authorsLabel = new JLabel(VisualMossConstants.ABOUT_AUTHORS);
        JLabel copyrightLabel = new JLabel(VisualMossConstants.ABOUT_COPYRIGHT);

        titleLabel.setFont(VisualMossConstants.HELP_TITLE_FONT);
        authorsLabel.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        copyrightLabel.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);

        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        authorsLabel.setHorizontalAlignment(JLabel.CENTER);
        copyrightLabel.setHorizontalAlignment(JLabel.CENTER);

        titleLabel.setHorizontalTextPosition(JLabel.CENTER);
        authorsLabel.setHorizontalTextPosition(JLabel.CENTER);
        copyrightLabel.setHorizontalTextPosition(JLabel.CENTER);

        GridBagConstraints c = new GridBagConstraints();

        // Change the program icon
        Image programIcon = Toolkit.getDefaultToolkit().getImage(VisualMossConstants.PROGRAM_ICON_PATH);
        setIconImage(programIcon);


        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridwidth = 1;
        c.gridheight = 2;
        panel.add(titleLabel, c);

        c.gridy = 4;
        c.gridheight = 3;
        panel.add(authorsLabel, c);

        c.gridy = 7;
        c.gridheight = 4;
        panel.add(copyrightLabel, c);

        panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        this.setContentPane(panel);
        this.pack();
        this.setTitle(VisualMossConstants.ABOUT_LABEL);
        this.setVisible(true);
    }
}
