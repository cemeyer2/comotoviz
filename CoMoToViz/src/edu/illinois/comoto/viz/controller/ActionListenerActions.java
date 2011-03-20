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

import edu.illinois.comoto.viz.model.PrefuseGraphBuilder;
import edu.illinois.comoto.viz.utility.DataExport;
import edu.illinois.comoto.viz.utility.ExtensionFileFilter;
import edu.illinois.comoto.viz.view.*;
import org.apache.log4j.Logger;
import prefuse.data.io.DataIOException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * User: Jon
 * Date: 3/6/11
 * <p/>
 * Stores all action listeners
 */
public enum ActionListenerActions {

    // Login from the login dialog
    login {
        @Override
        ActionListener getActionListenerAction(final Object... parameters) {

            // Get a handle on the login window
            final LoginDialog loginWindow = (LoginDialog) parameters[0];

            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    loginWindow.login();
                }
            };
        }
    },

    // Export the current graph to XML
    exportGraph {
        @Override
        ActionListener getActionListenerAction(final Object... parameters) {

            // Get a handle on the main window
            final MainWindow mainWindow = (MainWindow) parameters[0];

            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    JFileChooser chooser = new JFileChooser();
                    ExtensionFileFilter filter = new ExtensionFileFilter(FrontendConstants.GRAPH_ML_FILES);
                    filter.addExtension(BackendConstants.XML);
                    chooser.setFileFilter(filter);
                    int retval = chooser.showSaveDialog(mainWindow);
                    if (retval == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        try {
                            new DataExport(PrefuseGraphBuilder.getBuilder().buildPrefuseGraph()).write(file);
                        } catch (DataIOException exception) {
                            LOGGER.fatal("Error exporting graph", exception);
                        }
                    }
                }
            };
        }
    },

    // Add an item from the menu
    quit {
        @Override
        ActionListener getActionListenerAction(final Object... parameters) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    WindowListenerActions.askAndQuit();
                }
            };
        }
    },

    // Open the 'about' menu
    about {
        @Override
        ActionListener getActionListenerAction(final Object... parameters) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    new AboutDialog();
                }
            };
        }
    },

    // Open the 'help' dialog
    help {
        @Override
        ActionListener getActionListenerAction(final Object... parameters) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    new AboutDialog();
                }
            };
        }
    },

    sim {
        @Override
        ActionListener getActionListenerAction(Object... parameters) {
            return new ActionListener() {

                public void actionPerformed(ActionEvent actionEvent) {
                    new EditForceSimulatorDialog();
                }
            };
        }
    },

    logging {
        @Override
        ActionListener getActionListenerAction(Object... parameters) {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    LoggingDialog.getInstance().showDialog();
                }
            };
        }
    },

    // Export the image of this graph
    exportImage {
        @Override
        ActionListener getActionListenerAction(final Object... parameters) {

            // Get a handle on the main window
            final MainWindow mainWindow = (MainWindow) parameters[0];

            return new ActionListener() {
                public void actionPerformed(ActionEvent event) {

                    // Create a new file chooser to save the image of the graph, and set the filter to PNG files only
                    JFileChooser chooser = new JFileChooser();
                    ExtensionFileFilter filter = new ExtensionFileFilter(FrontendConstants.PNG_IMAGES);
                    filter.addExtension(BackendConstants.PNG);
                    chooser.setFileFilter(filter);

                    // Show the save dialog and wait for user response
                    int userInput = chooser.showSaveDialog(mainWindow);
                    if (userInput == JFileChooser.APPROVE_OPTION) {

                        // Try to dump this graph to the file as an image
                        File file = chooser.getSelectedFile();
                        try {
                            mainWindow.getGraphPanel().writeToImage(file);
                        } catch (IOException ioe) {
                            LOGGER.fatal("Error writing graph to image file", ioe);
                            JOptionPane.showMessageDialog(null, "Error writing image to file. Please try again.", "IO Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            };
        }
    };

    abstract ActionListener getActionListenerAction(Object... parameters);

    private static final Logger LOGGER = Logger.getLogger(ActionListenerActions.class);
}
