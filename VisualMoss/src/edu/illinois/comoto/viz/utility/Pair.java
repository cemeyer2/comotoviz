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

package edu.illinois.comoto.viz.utility;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 * <p/>
 * <p> <p> This implements a generic, typed Pair object
 */
public class Pair<A extends Comparable, B extends Comparable> implements Comparable<Pair<A, B>> {

    /**
     * The first element in the pair
     */
    private A first;

    /**
     * The second element in the pair
     */
    private B second;

    /**
     * Builds a pair containing the two items input
     *
     * @param first  The first item in the pair
     * @param second The second item in the pair
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Implements the comparable interface to compare two pairs to each other
     *
     * @param otherPair The pair to which to compare this pair
     * @return An integer value, representing the comparison of these two pairs (ordering)
     */
    public int compareTo(Pair<A, B> otherPair) {

        // If the pairs are the same object or contain the exact same objects
        if (otherPair == this || (otherPair.getFirst() == first && second == otherPair.getSecond())) {
            return 0;

        } else if (first.equals(otherPair.getFirst()) && second.equals(otherPair.getSecond())) {
            return 0;

        } else if (first.compareTo(otherPair.getFirst()) == 0) {
            return second.compareTo(otherPair.getSecond());

        } else {
            return first.compareTo(otherPair.getFirst());
        }
    }

    /**
     * Checks to see if this pair is equal to another pair
     *
     * @param otherPair The pair to which to compare this object
     * @return Whether or not this pair is equal to a given pair
     */
    @Override
    public boolean equals(Object otherPair) {
        if (this == otherPair)
            return true;
        if (!(otherPair instanceof Pair))
            return false;

        Pair pair = (Pair) otherPair;

        if (!first.equals(pair.getFirst()))
            return false;
        if (!second.equals(pair.getSecond()))
            return false;
        return true;
    }

    /**
     * Calculates the hash code of this pair
     *
     * @return The hashcode for this pair
     */
    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}