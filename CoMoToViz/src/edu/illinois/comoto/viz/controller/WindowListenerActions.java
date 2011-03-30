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

package edu.illinois.comoto.viz.controller;

import edu.illinois.comoto.viz.view.FrontendConstants;

import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * Stores all window listeners
 */
public enum WindowListenerActions {

    // When something happens to the login window
    loginWindow {
        @Override
        WindowListener getWindowListenerAction(final Object... parameters) {
            return new WindowListener() {

                /**
                 * Prompt user before closing the window
                 */
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }

                // Default, empty actions for everything else
                public void windowClosed(WindowEvent e) {
                }

                public void windowOpened(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            };
        }
    },

    // When something happens to the main window
    mainWindow {
        @Override
        WindowListener getWindowListenerAction(final Object... parameters) {
            return new WindowListener() {

                /**
                 * Prompt user before closing the window
                 */
                public void windowClosing(WindowEvent e) {
                    askAndQuit();
                }

                // Default, empty actions for everything else
                public void windowClosed(WindowEvent e) {
                }

                public void windowOpened(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            };
        }
    };

    /**
     * Prompts the user before closing the window
     */
    static public void askAndQuit() {
        int doYouWantToQuit = JOptionPane.showConfirmDialog(null, FrontendConstants.QUIT_CONFIRMATION_MESSAGE,
                FrontendConstants.QUIT_PROMPT, JOptionPane.YES_NO_OPTION);
        if (doYouWantToQuit == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    abstract WindowListener getWindowListenerAction(Object... parameters);
}
