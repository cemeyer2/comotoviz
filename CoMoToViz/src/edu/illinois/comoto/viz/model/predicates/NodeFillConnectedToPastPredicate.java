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

package edu.illinois.comoto.viz.model.predicates;

import edu.illinois.comoto.viz.view.BackendConstants;
import org.apache.log4j.Logger;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.ExpressionListener;
import prefuse.data.expression.ExpressionVisitor;
import prefuse.data.expression.Predicate;


public class NodeFillConnectedToPastPredicate implements Predicate {

    private static final Logger LOGGER = Logger.getLogger(NodeFillConnectedToPastPredicate.class);

    public void addExpressionListener(ExpressionListener lstnr) {
        // TODO Auto-generated method stub

    }

    public Object get(Tuple t) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean getBoolean(Tuple t) {
        if (t.canGetBoolean(BackendConstants.CONNECTED_TO_PAST)) {
            return t.getBoolean(BackendConstants.CONNECTED_TO_PAST);
        }
        return false;
    }

    public double getDouble(Tuple t) {
        // TODO Auto-generated method stub
        return 0;
    }

    public float getFloat(Tuple t) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getInt(Tuple t) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLong(Tuple t) {
        // TODO Auto-generated method stub
        return 0;
    }

    public Class getType(Schema s) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeExpressionListener(ExpressionListener lstnr) {
        // TODO Auto-generated method stub

    }

    public void visit(ExpressionVisitor v) {
        // TODO Auto-generated method stub

    }

}
