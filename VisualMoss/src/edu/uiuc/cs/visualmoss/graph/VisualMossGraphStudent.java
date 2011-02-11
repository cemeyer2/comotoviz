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

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 8, 2010
 * <p/>
 * <p> <p> A simple representation of a student object for use with the VisualMossGraph class
 */
public class VisualMossGraphStudent implements Comparable<VisualMossGraphStudent> {

    /**
     * The basic information of a student
     */
    private final String netid, pseudonym;

    /**
     * The matches for this student
     */
    private final ArrayList<VisualMossGraphMatch> matches;

    /**
     * Creates a new, basic student object from the given data
     *
     * @param netid     The netid of this student
     * @param pseudonym The pseudonym of this student
     */
    protected VisualMossGraphStudent(String netid, String pseudonym) {
        this.netid = netid;
        this.pseudonym = pseudonym;
        this.matches = new ArrayList<VisualMossGraphMatch>();
    }

    public String getNetid() {
        return netid;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    protected void addMatch(VisualMossGraphMatch match) {
        matches.add(match);
    }

    public List<VisualMossGraphMatch> getMatches() {
        return matches;
    }

    public int compareTo(VisualMossGraphStudent student) {
        return getNetid().compareTo(student.getNetid());
    }

    public String toString() {
        return getNetid();
    }
}
