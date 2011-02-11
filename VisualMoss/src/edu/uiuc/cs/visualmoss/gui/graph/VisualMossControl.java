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

package edu.uiuc.cs.visualmoss.gui.graph;

import edu.uiuc.cs.visualmoss.gui.diff.WebpageDialog;
import prefuse.Display;
import prefuse.controls.Control;
import prefuse.data.Node;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.net.MalformedURLException;
import java.util.Iterator;

public class VisualMossControl implements Control {
    private boolean enabled = true;
    private VisualMossGraphDisplayContainer container;

    protected VisualMossControl(VisualMossGraphDisplayContainer container) {
        this.container = container;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void itemClicked(VisualItem item, MouseEvent e) {
        //if is edge, then launch diff viewer here
        if (item instanceof EdgeItem) {
            String url = item.getString("link");
            try {
                new WebpageDialog(null, url, false);
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(null, "URL associated with edge is invalid", "Invalid URL",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void itemDragged(VisualItem item, MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemEntered(VisualItem item, MouseEvent e) {
        if (item instanceof EdgeItem) {
            EdgeItem edge = (EdgeItem) item;
            Node source = edge.getSourceNode();
            Node target = edge.getTargetNode();
            String colname = "netid";
            if (container.getVisualMossGraphDisplay().isAnonymous())
                colname = "pseudonym";
            String netid1 = source.getString(colname);
            String netid2 = target.getString(colname);
            double weight = edge.getDouble("weight");
            String partners = (edge.getBoolean("isPartner")) ? "declared partners " : "";
            container.setStatus("Similarity between " + partners + netid1 + " and " + netid2 + " with score " + weight);
        }
        if (item instanceof NodeItem) {
            //setNeighborHighlight((NodeItem)item, true, (Display)e.getComponent());
            NodeItem node = (NodeItem) item;
            String colname = "netid";
            if (container.getVisualMossGraphDisplay().isAnonymous())
                colname = "pseudonym";
            String netid = node.getString(colname);
            String season = node.getString("season");
            String year = node.getString("year");
            container.setStatus("Submission: " + netid + ", " + season + " " + year);
        }
    }

    public void itemExited(VisualItem item, MouseEvent e) {
        container.clearStatus();
        if (item instanceof EdgeItem) {

        }
        if (item instanceof NodeItem) {
            //setNeighborHighlight((NodeItem)item, false, (Display)e.getComponent());
        }
    }

    public void itemKeyPressed(VisualItem item, KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemKeyReleased(VisualItem item, KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemKeyTyped(VisualItem item, KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemMoved(VisualItem item, MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemPressed(VisualItem item, MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemReleased(VisualItem item, MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        // TODO Auto-generated method stub

    }

    public void setEnabled(boolean enabled) {
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

}
