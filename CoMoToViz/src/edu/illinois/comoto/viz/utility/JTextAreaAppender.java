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

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

//from http://textareaappender.zcage.com/
public class JTextAreaAppender extends WriterAppender {

	private static JTextArea jTextArea = null;
    private static JTextAreaAppender pseudoSingleton;
	protected String name = "JTextAreaAppender";

	
	/** Set the target JTextArea for the logging information to appear. */
	public JTextAreaAppender() {
        if(jTextArea == null){
            jTextArea = new JTextArea();
            pseudoSingleton = this;
        }
	}

    public static JTextAreaAppender getPseudoSingleton(){
        return pseudoSingleton;
    }

    public static JTextArea getJTextArea(){
        return jTextArea;
    }

    public static void setJTextArea(JTextArea jta){
        jTextArea = jta;
    }
	/**
	 * Format and then append the loggingEvent to the stored
	 * JTextArea.
	 */
	public void append(LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
                if(jTextArea != null){
                    jTextArea.append(message);
                    jTextArea.setCaretPosition( jTextArea.getDocument().getLength() ); //scroll it down
                }
			}
		});
	}
	
}