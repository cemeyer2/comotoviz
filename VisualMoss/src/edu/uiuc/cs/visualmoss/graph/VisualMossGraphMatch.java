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

package edu.uiuc.cs.visualmoss.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 8, 2010
 * <p/>
 * <p> <p> Simple representation of a match object from the graph
 */
public class VisualMossGraphMatch {

    /**
     * The students associated with this match
     */
    private final VisualMossGraphStudent student1, student2;

    /**
     * The scores associated with this match
     */
    private final double score1, score2;

    /**
     * Creates a new basic match object from the given parameters
     *
     * @param student1 The first student
     * @param student2 The second student
     * @param score1   The first match score
     * @param score2   The second match score
     */
    protected VisualMossGraphMatch(VisualMossGraphStudent student1, VisualMossGraphStudent student2, double score1, double score2) {
        this.score1 = score1;
        this.score2 = score2;
        this.student1 = student1;
        this.student2 = student2;
    }

    public VisualMossGraphStudent getStudent1() {
        return student1;
    }

    public VisualMossGraphStudent getStudent2() {
        return student2;
    }

    public double getScore1() {
        return score1;
    }

    public double getScore2() {
        return score2;
    }
}
