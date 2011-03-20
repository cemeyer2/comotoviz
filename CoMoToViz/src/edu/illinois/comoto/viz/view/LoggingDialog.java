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

import edu.illinois.comoto.viz.utility.JTextAreaAppender;

import javax.swing.*;
import java.awt.*;

/**
 * Author:  Charlie Meyer <cemeyer2@illinois.edu>
 * Date:    3/16/11
 * Time:    1:41 PM
 * Package: edu.illinois.comoto.viz.view
 * Created by IntelliJ IDEA.
 */
public class LoggingDialog extends JDialog {

    private static final LoggingDialog singleton = new LoggingDialog();

    public static LoggingDialog getInstance(){
        return singleton;
    }

    public LoggingDialog() {
        super();
        initialize();
    }

    private void initialize() {
        setTitle(FrontendConstants.LOGGING_DIALOG_TITLE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(FrontendConstants.LOGGING_DIALOG_WIDTH, FrontendConstants.LOGGING_DIALOG_HEIGHT));
        JTextArea area = JTextAreaAppender.getJTextArea();
        panel.add(new JScrollPane(area),BorderLayout.CENTER);
        setContentPane(panel);
        // Change the program icon
        setIconImage(BackendConstants.PROGRAM_IMAGE);

        // Display the dialog
        this.pack();
        this.setVisible(false);
        this.setLocationRelativeTo(null);
    }

    public void showDialog(){
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
