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

package edu.illinois.comoto.api.object;

import edu.illinois.comoto.api.CoMoToAPIException;
import edu.illinois.comoto.api.utility.Connection;
import junit.framework.TestCase;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import static edu.illinois.comoto.api.CoMoToAPIConstants.COMPLETE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: Jon
 * Date: 4/17/11
 * Time: 9:56 PM
 * <p/>
 * Tests the analysis pseudonym construction & refresh functionality
 */
public class AnalysisPseudonymTest extends TestCase {

    /**
     * A valid map of an analysis pseudonym for testing
     */
    private Map<String, Object> analysisPseudonymMap;

    /**
     * Setup sample map & date test data before any test is called (only called once).
     */
    @Before
    public void setUp() {

        // Create a valid 'analysis pseudonym' map, we'll add to and remove from this for error cases
        analysisPseudonymMap = TestMapUtilities.getValidAnalysisPseudonymMap();
    }

    /**
     * Test that the constructor works correctly in the best case scenario
     */
    @Test
    public void testConstructFromValidMap() {

        // Setup
        Connection connection = new Connection(null, null);

        // Test
        AnalysisPseudonym analysisPseudonym = new AnalysisPseudonym(analysisPseudonymMap, connection);

        // Verify
        assertEquals(analysisPseudonym.getId(), 1);
        assertEquals(analysisPseudonym.getAnalysisId(), 2);
        assertEquals(analysisPseudonym.getPseudonym(), "A");
        assertEquals(analysisPseudonym.getSubmissionId(), 3);
    }

    /**
     * Tests that constructing an analysis pseudonym from null inputs yields <code>CoMoToAPIException<code>s.
     */
    @Test
    public void testConstructFromNullInputs() {

        // Setup
        Connection connection = new Connection(null, null);

        // Test & verify
        try {
            AnalysisPseudonym analysisPseudonym = new AnalysisPseudonym(null, connection);
            fail("Analysis pseudonym constructor should not have accepted NULL map as input!");
        } catch (CoMoToAPIException e) {
        }
        try {
            AnalysisPseudonym analysisPseudonym = new AnalysisPseudonym(analysisPseudonymMap, null);
            fail("Analysis pseudonym constructor should not have accepted NULL connection as input!");
        } catch (CoMoToAPIException e) {
        }
    }

    /**
     * Tests that constructing an analysis pseudonym missing some critical data does not succeeed.
     */
    @Test
    public void testPartiallyInvalidMap() {

        // Test this for removing every key in the analysis map
        analysisPseudonymMap.remove(COMPLETE);
        for (String key : analysisPseudonymMap.keySet()) {

            // Setup
            Connection connection = new Connection(null, null);
            Map<String, Object> badAnalysisPseudonymMap = new HashMap<String, Object>();
            badAnalysisPseudonymMap.putAll(analysisPseudonymMap);
            badAnalysisPseudonymMap.remove(key);

            // Test & verify that this invalid map fails
            try {
                AnalysisPseudonym analysisPseudonym = new AnalysisPseudonym(badAnalysisPseudonymMap, connection);
                fail("Analysis pseudonym should not have accepted incomplete map as input!");
            } catch (CoMoToAPIException e) {
            }
        }

    }

    /**
     * Test that the refresh, and only the refresh, calls the API, and exactly once.
     */
    @Test
    public void testRefreshHitsAPI() {

        // Setup
        Connection mockConnection = mock(Connection.class);

        // Test
        AnalysisPseudonym analysisPseudonym = new AnalysisPseudonym(analysisPseudonymMap, mockConnection);

        // Verify that the connection has not been touched yet
        try {
            verify(mockConnection, times(0)).execute(any(String.class), any(Array.class));
        } catch (XmlRpcException e) {
        }

        // Test
        try {
            analysisPseudonym.refresh();
        } catch (CoMoToAPIException e) {
            // Ignore errors building this new object, it's given bad data
        }

        // Verify that the API has been hit now
        try {
            verify(mockConnection, times(1)).execute(any(String.class), any(Array.class));
        } catch (XmlRpcException e) {
        }
    }
}
