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

package edu.uiuc.cs.visualmoss.gui.utility;

import edu.uiuc.cs.visualmoss.VisualMossConstants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LoadingProgressDialog extends JDialog {
    private final int DIALOG_WIDTH = 400;
    private final int DIALOG_HEIGHT = 50;
    private JProgressBar bar;
    private String title, label;

    public LoadingProgressDialog(JFrame owner, String title, String label) {
        super(owner, false); //make this not modal
        bar = new JProgressBar();
        this.title = title;
        this.label = label;
//		init();
    }

    public LoadingProgressDialog(String title, String label) {
        super();
        this.setModal(false);
        bar = new JProgressBar();
        this.title = title;
        this.label = label;
//		init();
    }

    public void init() {
        this.setTitle(title);
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

        // Change the program icon
        Image programIcon = Toolkit.getDefaultToolkit().getImage("VisualMoss/uiuc.png");
        setIconImage(programIcon);


        TitledBorder b = BorderFactory.createTitledBorder(label);
        b.setTitleFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        bar.setBorder(b);
        bar.setIndeterminate(true);
        bar.setStringPainted(true);
        bar.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        panel.add(bar);
        this.setContentPane(panel);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void setIndeterminate(boolean value) {
        bar.setIndeterminate(value);
    }

    public void setMessage(String message) {
        bar.setString(message);
    }
}
