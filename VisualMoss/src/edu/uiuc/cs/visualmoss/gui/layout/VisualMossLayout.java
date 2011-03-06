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

package edu.uiuc.cs.visualmoss.gui.layout;

import edu.illinois.comoto.api.object.Assignment;
import edu.uiuc.cs.visualmoss.VisualMossConstants;
import edu.uiuc.cs.visualmoss.dataexport.DataExport;
import edu.uiuc.cs.visualmoss.dataimport.DataImport;
import edu.uiuc.cs.visualmoss.graph.VisualMossGraph;
import edu.uiuc.cs.visualmoss.gui.controls.VisualMossControls;
import edu.uiuc.cs.visualmoss.gui.dataimport.AssignmentChooser;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplay;
import edu.uiuc.cs.visualmoss.gui.graph.VisualMossGraphDisplayContainer;
import edu.uiuc.cs.visualmoss.gui.help.AboutDialog;
import edu.uiuc.cs.visualmoss.gui.help.HelpDialog;
import edu.uiuc.cs.visualmoss.utility.ExtensionFileFilter;
import edu.uiuc.cs.visualmoss.utility.Pair;
import prefuse.data.io.DataIOException;
import prefuse.util.ColorLib;
import prefuse.visual.NodeItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import static edu.uiuc.cs.visualmoss.VisualMossConstants.CS225;
import static edu.uiuc.cs.visualmoss.VisualMossConstants.MP3;

public class VisualMossLayout extends JFrame implements ActionListener, WindowListener {
    private static final long serialVersionUID = 1L;
    //Preferred Window Sizes
    final static int windowWidth = 1024;
    final static int windowHeight = 768;
    JMenuBar menu;
    JMenu fileMenu, helpMenu;
    JMenuItem add, exportGraph, exportImage, quit, help, about;
    private VisualMossGraph graph;
    private VisualMossGraphDisplayContainer container;
    private VisualMossGraphDisplay graphDisplay;
    private VisualMossControls rightControls;
    private AssignmentChooser leftControls;
    private JTextField searchBox;

    private NodeItem lastNode;
    private boolean lastNodeWasVisible;
    private int lastFillColor;


    private Pair<String, String> activeDirectoryCredentials;

    public VisualMossLayout(Pair<String, String> activeDirectoryCredentials) throws DataIOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Container visualMoss = getContentPane();
        visualMoss.setLayout(new BorderLayout());
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the current screen size
        Dimension scrnsize = toolkit.getScreenSize();
        visualMoss.setPreferredSize(new Dimension((int) scrnsize.getWidth() - 75, (int) scrnsize.getHeight() - 75));

        // Change the program icon
        Image programIcon = Toolkit.getDefaultToolkit().getImage(VisualMossConstants.PROGRAM_ICON_PATH);
        setIconImage(programIcon);

        this.setTitle(VisualMossConstants.PROGRAM_TITLE);
        container = new VisualMossGraphDisplayContainer(768, 768);
        graphDisplay = container.getVisualMossGraphDisplay();

        rightControls = new VisualMossControls();
        rightControls.addVisualMossControls(graphDisplay);

        leftControls = new AssignmentChooser(this, container, activeDirectoryCredentials);
        leftControls.setPreferredSize(new Dimension(200, windowHeight));

        visualMoss.add(container);
        visualMoss.add(leftControls, BorderLayout.WEST);
        visualMoss.add(rightControls, BorderLayout.EAST);

        addSearchBox();

        addMenuBar();

        container.getVisualMossGraphDisplay().run();

        this.addWindowListener(this);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void updateTitle(Assignment assignment) {
        this.setTitle("CoMoTo Dynamic Visualization Tool: " + assignment.getCourse().getName() + ": " + assignment.getName());
    }

    public void addSearchBox() {
        Container visualMoss = getContentPane();

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        JLabel searchLabel = new JLabel("Search for student: ");
        searchLabel.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        searchPanel.add(searchLabel, BorderLayout.WEST);

        searchBox = new JTextField();
        searchPanel.add(searchBox);

        searchBox.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                searchStudents();
            }

            public void keyPressed(KeyEvent e) {
            }
        });

        visualMoss.add(searchPanel, BorderLayout.NORTH);
    }

    public boolean searchStudents() {
        container.clearStatus();
        String searchText = searchBox.getText();

        if (graphDisplay == null || searchText.length() == 0) {
            searchBox.setBackground(Color.white);
            return false;
        }

        Iterator<NodeItem> it = graphDisplay.getNodes();
        if (it == null) {
            searchBox.setBackground(Color.white);
            return false;
        }
        while (it.hasNext()) {
            NodeItem node = it.next();
            String netid = node.getString("netid");
            if (netid.startsWith(searchText)) {
                graphDisplay.panToNode(netid, 500);
                boolean isVisible = graphDisplay.getVisibilityPredicate().getBoolean(node);
                if (isVisible) {
                    searchBox.setBackground(Color.green);
                    container.setStatus("Centered on node \"" + netid + "\".");
                } else {
                    searchBox.setBackground(Color.yellow);
                    container.setStatus("Centered on node \"" + netid + "\" not visible with current settings");
                }
                if (!lastNodeWasVisible && lastNode != null)
                    lastNode.setVisible(false);
                if (lastNode != null)
                    lastNode.setFillColor(lastFillColor);

                lastNodeWasVisible = isVisible;
                lastNode = node;

                lastFillColor = node.getFillColor();
                node.setFillColor(ColorLib.rgb(0, 255, 0));
                return true;
            }
        }
        if (!lastNodeWasVisible && lastNode != null)
            lastNode.setVisible(false);
        searchBox.setBackground(Color.red);
        return false;
    }

    public void addMenuBar() {
        //Add menu bar
        menu = new JMenuBar();
        //top level menus
        fileMenu = new JMenu("File");
        fileMenu.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        helpMenu = new JMenu("Help");
        helpMenu.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        //menu items
        add = new JMenuItem("Add Data Set");
        exportGraph = new JMenuItem("Export GraphML");
        exportImage = new JMenuItem("Export Image");
        quit = new JMenuItem("Quit");
        help = new JMenuItem("Help");
        about = new JMenuItem("About");

        add.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        exportGraph.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        exportImage.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        quit.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        help.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);
        about.setFont(VisualMossConstants.COMPONENT_LABEL_FONT);

        //File Menu
        menu.add(fileMenu);
        //fileMenu.add(add);
        fileMenu.add(exportGraph);
        fileMenu.add(exportImage);
        fileMenu.add(quit);

        //Help Menu
        menu.add(helpMenu);
        helpMenu.add(help);
        helpMenu.add(about);

        exportGraph.addActionListener(this);
        exportImage.addActionListener(this);
        quit.addActionListener(this);
        add.addActionListener(this);
        about.addActionListener(this);
        help.addActionListener(this);

        this.setJMenuBar(menu);
    }

    public void actionPerformed(ActionEvent ae) {
        JMenuItem item = (JMenuItem) ae.getSource();
        if (item.equals(exportImage)) {
            JFileChooser chooser = new JFileChooser();
            ExtensionFileFilter filter = new ExtensionFileFilter("PNG Images");
            filter.addExtension("png");
            chooser.setFileFilter(filter);
            int retval = chooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    graphDisplay.writeToImage(file);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (item.equals(exportGraph)) {
            JFileChooser chooser = new JFileChooser();
            ExtensionFileFilter filter = new ExtensionFileFilter("GraphML files");
            filter.addExtension("xml");
            chooser.setFileFilter(filter);
            int retval = chooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                try {
                    new DataExport(container.getVisualMossGraphDisplay().getGraph()).write(file);
                } catch (DataIOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (item.equals(quit)) {
            askAndQuit();
        } else if (item.equals(about)) {
            new AboutDialog();
        } else if (item.equals(add)) {
            DataImport importer = new DataImport(CS225, MP3, activeDirectoryCredentials);
            Assignment assignment = importer.getAssignment();
            VisualMossGraph graph = importer.getGraph();
            graphDisplay.setGraph(graph);
            updateTitle(assignment);
        } else if (item.equals(help)) {
            new HelpDialog();
        }
    }

    public void askAndQuit() {
        int retval = JOptionPane.showConfirmDialog(this, "Are you sure you would like to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
        if (retval == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void changeAssignment(int id) {
        DefaultMutableTreeNode node = leftControls.getAssignmentTreeNodeById(id);
        if (node != null) {
            leftControls.changeAssignment(node);
        } else {
            JOptionPane.showInputDialog(this, "The requested assignment does not exist", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    public void windowClosing(WindowEvent e) {
        askAndQuit();
    }

    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }
}
