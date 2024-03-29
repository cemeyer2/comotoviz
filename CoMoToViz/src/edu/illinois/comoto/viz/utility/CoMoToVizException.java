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
 * Exception thrown on most exceptions relating to the VisualMoss application
 */
public class CoMoToVizException extends RuntimeException {

    /**
     * Unique identifier for this exception type
     */
    private static final long serialVersionUID = 8276931130814119675L;

    /**
     * The exception that caused this exception (i.e. the root exception)
     */
    private Exception causedBy;

    /**
     * Builds a standard java exception of type CoMoToVizException
     *
     * @param reason Message containing the reason for the exception
     */
    public CoMoToVizException(String reason) {
        super(reason);
    }

    /**
     * Builds a java exception of type CoMoToVizException with the root exception
     *
     * @param reason   Message containing the reason for the exception
     * @param causedBy A handle on the exception that caused this exception
     */
    public CoMoToVizException(String reason, Exception causedBy) {
        super(reason + ": " + causedBy.getMessage());
        this.causedBy = causedBy;
    }

    /**
     * Gets the root exception for this typed CoMoToVizException
     *
     * @return A handle to the exception that caused this one
     */
    public Exception getCausedBy() {
        return causedBy;
    }
}
