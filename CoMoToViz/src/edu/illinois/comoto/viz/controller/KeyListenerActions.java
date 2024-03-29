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

import edu.illinois.comoto.viz.view.LoginDialog;
import edu.illinois.comoto.viz.view.MainWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * Stores all key listeners,
 */
public enum KeyListenerActions {

    // Login if the enter key was pressed
    login {
        @Override
        KeyListener getKeyListenerAction(final Object... parameters) {
            return new KeyListener() {

                // Get a handle on the login dialog
                private final LoginDialog loginDialog = (LoginDialog) parameters[0];

                // If the enter key was hit, login
                public void keyTyped(KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                        loginDialog.login();
                    }
                }

                // Ignore other key actions
                public void keyPressed(KeyEvent e) {
                }

                public void keyReleased(KeyEvent e) {
                }
            };
        }
    },

    // When we search for students from the text box
    searchStudents {
        @Override
        KeyListener getKeyListenerAction(final Object... parameters) {
            final MainWindow mainWindow = (MainWindow) parameters[0];

            return new KeyListener() {
                public void keyTyped(KeyEvent e) {
                    mainWindow.searchStudents();
                }

                // Not used here
                public void keyPressed(KeyEvent e) {
                }

                public void keyReleased(KeyEvent e) {
                }
            };
        }
    };

    abstract KeyListener getKeyListenerAction(Object... parameters);
}
