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

package edu.illinois.comoto.viz.view.graph.control;

import edu.illinois.comoto.api.CoMoToAPI;
import edu.illinois.comoto.api.object.Student;
import edu.illinois.comoto.viz.view.BackendConstants;
import edu.illinois.comoto.viz.view.FrontendConstants;
import edu.illinois.comoto.viz.view.StudentInfoDialog;
import edu.illinois.comoto.viz.view.WebPageDialog;
import edu.illinois.comoto.viz.view.graph.GraphDisplayBuilder;
import edu.illinois.comoto.viz.view.graph.GraphPanel;
import org.apache.log4j.Logger;
import prefuse.Display;
import prefuse.controls.Control;
import prefuse.data.Node;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.net.MalformedURLException;
import java.util.Iterator;

public class GraphControl implements Control {
    private boolean enabled = true;

    private static Logger logger = Logger.getLogger(GraphControl.class);


    public GraphControl() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void itemClicked(VisualItem item, MouseEvent e) {

        logger.info("Item clicked: " + item);
        try {
            //if is edge, then launch diff viewer here
            if (item instanceof EdgeItem) {
                logger.info("Item is an edge");
                String url = item.getString(BackendConstants.LINK);
                try {
                    new WebPageDialog(null, url, false);
                } catch (MalformedURLException ex) {
                    JOptionPane.showMessageDialog(null, FrontendConstants.EDGE_URL_INVALID_MESSAGE, FrontendConstants.INVALID_URL,
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (item instanceof NodeItem) {
                logger.info("Item is a node");
                if (e.getButton() != MouseEvent.BUTTON1) {

                    NodeItem node = (NodeItem) item;
                    logger.info("node: " + node);
                    logger.info("degree: " + node.getDegree());
                    Iterator<EdgeItem> iter = node.edges();
                    while (iter.hasNext()) {
                        logger.info("edge: " + iter.next());
                    }
                    logger.info("launching dialog");
                    Student student = CoMoToAPI.getStudent(null, item.getInt(BackendConstants.STUDENT_ID));
                    new StudentInfoDialog(student);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception thrown in itemClicked", ex);
        }
    }

    public void itemDragged(VisualItem item, MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemEntered(VisualItem item, MouseEvent e) {
        logger.info("Item entered: " + item);
        try {
            if (item instanceof EdgeItem) {
                logger.info("Item is an edge");
                EdgeItem edge = (EdgeItem) item;
                Node source = edge.getSourceNode();
                Node target = edge.getTargetNode();
                String colname = BackendConstants.NETID;
                if (GraphDisplayBuilder.getBuilder().isAnonymous()) {
                    colname = BackendConstants.PSEUDONYM;
                }
                String netid1 = source.getString(colname);
                String netid2 = target.getString(colname);
                double weight = edge.getDouble(BackendConstants.WEIGHT);
                String partners = (edge.getBoolean(BackendConstants.IS_PARTNER)) ? "declared partners " : "";
                GraphPanel.getGraphPanel().setMessage("Similarity between " + partners + netid1 + " and " + netid2 + " with score " + weight);
            }
            if (item instanceof NodeItem) {
                logger.info("Item is a node");
                //setNeighborHighlight((NodeItem)item, true, (Display)e.getComponent());
                NodeItem node = (NodeItem) item;
                String colname = BackendConstants.NETID;
                if (GraphDisplayBuilder.getBuilder().isAnonymous()) {
                    colname = BackendConstants.PSEUDONYM;
                }
                String netid = node.getString(colname);
                String season = node.getString(BackendConstants.SEASON);
                String year = node.getString(BackendConstants.YEAR);
                GraphPanel.getGraphPanel().setMessage(FrontendConstants.SUBMISSION + ": " + netid + ", " + season + " " + year);
            }
        } catch (Exception ex) {
            logger.error("Exception caught in itemEntered", ex);
        }
    }

    public void itemExited(VisualItem item, MouseEvent e) {
        GraphPanel.getGraphPanel().clearMessage();
//        if (item instanceof EdgeItem) {
//
//        }
        if (item instanceof NodeItem) {
            setNeighborHighlight((NodeItem) item, false, (Display) e.getComponent());
        }
    }

    public void setEnabled(boolean enabled) {
        logger.info("setEnabled: " + enabled);
        this.enabled = enabled;
    }

    protected void setNeighborHighlight(NodeItem n, boolean state, Display display) {
        Iterator iter = n.edges();
        while (iter.hasNext()) {
            EdgeItem eitem = (EdgeItem) iter.next();
            NodeItem nitem = eitem.getAdjacentItem(n);
            eitem.setHighlighted(state);
            nitem.setHighlighted(state);
        }
        display.damageReport();
        display.repaint();
    }

    // Unused graph controls
    public void itemKeyPressed(VisualItem item, KeyEvent e) {
    }

    public void itemKeyReleased(VisualItem item, KeyEvent e) {
    }

    public void itemKeyTyped(VisualItem item, KeyEvent e) {
    }

    public void itemMoved(VisualItem item, MouseEvent e) {
    }

    public void itemPressed(VisualItem item, MouseEvent e) {
    }

    public void itemReleased(VisualItem item, MouseEvent e) {
    }

    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }
}
