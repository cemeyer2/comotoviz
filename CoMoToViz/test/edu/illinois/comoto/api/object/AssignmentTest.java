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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: Jon
 * Date: 4/17/11
 * Time: 9:33 PM
 * <p/>
 * Tests creation and refreshing an <code>Analysis</code> object.
 */
public class AssignmentTest extends TestCase {

    /**
     * A valid map of an assignment for testing
     */
    private Map<String, Object> assignmentMap;

    /**
     * A valid map of an offering for testing
     */
    private Map<String, Object> offeringMap;

    /**
     * Setup sample map data before any test is called (only called once).
     */
    @Before
    public void setUp() {

        // Create a valid map, we'll add to and remove from this for error cases
        assignmentMap = TestMapUtilities.getValidAssignmentMap();

    }

    /**
     * Test that the constructor works correctly in the best case scenario
     */
    @Test
    public void testConstructFromValidMap() {

        // Setup
        Connection connection = new Connection(null, null);

        // Test
        Assignment assignment = new Assignment(assignmentMap, connection);

        // Verify
        assertEquals(assignment.getId(), 1);
        assertEquals(assignment.getAnalysisId(), 2);
        assertEquals(assignment.getCourseId(), 3);
        assertEquals(assignment.getReportId(), 4);
        assertEquals(assignment.getLanguage(), Language.valueOf("python"));
        assertEquals(assignment.getName(), "A");
        assertNotNull(assignment.getMossAnalysisPrunedOffering());
        assertNotNull(assignment.getFilesetIds());
        assertEquals(assignment.getFilesetIds().size(), 1);
    }

    /**
     * Tests that constructing an Assignment from null inputs yields <code>CoMoToAPIException<code>s.
     */
    @Test
    public void testConstructFromNullInputs() {

        // Setup
        Connection connection = new Connection(null, null);

        // Test & verify
        try {
            Assignment assignment = new Assignment(null, connection);
            fail("Assignment constructor should not have accepted NULL map as input!");
        } catch (CoMoToAPIException e) {
        }
        try {
            Assignment assignment = new Assignment(assignmentMap, null);
            fail("Assignment constructor should not have accepted NULL connection as input!");
        } catch (CoMoToAPIException e) {
        }
    }

    /**
     * Tests that constructing an Assignment missing some critical data does not succeeed.
     */
    @Test
    public void testPartiallyInvalidMap() {

        // Test this for removing every key in the Assignment map
        for (String key : assignmentMap.keySet()) {

            // Setup
            Connection connection = new Connection(null, null);
            Map<String, Object> badAssignmentMap = new HashMap<String, Object>();
            badAssignmentMap.putAll(assignmentMap);
            badAssignmentMap.remove(key);

            // Test & verify that this invalid map fails
            try {
                Assignment assignment = new Assignment(badAssignmentMap, connection);
                fail("Assignment should not have accepted incomplete map as input!");
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
        Assignment assignment = new Assignment(assignmentMap, mockConnection);

        // Verify that the connection has not been touched yet
        try {
            verify(mockConnection, times(0)).execute(any(String.class), any(Array.class));
        } catch (XmlRpcException e) {
        }

        // Test
        try {
            assignment.refresh();
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