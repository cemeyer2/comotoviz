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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static edu.illinois.comoto.api.CoMoToAPIConstants.*;

/**
 * User: Jon
 * Date: 4/18/11
 * Time: 12:01 AM
 * <p/>
 * Assists testing by providing valid maps for all API objects.
 */
public class TestUtilities {

    /**
     * Builds a valid analysisPseudonym map
     *
     * @return a valid analysisPseudonym map
     */
    static public Map<String, Object> getValidAnalysisPseudonymMap() {
        Map<String, Object> analysisPseudonymMap = new HashMap<String, Object>();
        analysisPseudonymMap.put(ID, 1);
        analysisPseudonymMap.put(ANALYSIS_ID, 2);
        analysisPseudonymMap.put(PSEUDONYM, "A");
        analysisPseudonymMap.put(SUBMISSION_ID, 3);
        return analysisPseudonymMap;
    }

    /**
     * Builds a valid analysis map
     *
     * @return a valid analysis map
     */
    static public Map<String, Object> getValidAnalysisMap() {
        Map<String, Object> analysisMap = new HashMap<String, Object>();
        analysisMap.put(ID, 1);
        analysisMap.put(TIMESTAMP, new Date());
        analysisMap.put(COMPLETE, true);
        analysisMap.put(ASSIGNMENT_ID, 2);
        analysisMap.put(MOSS_ANALYSIS_ID, 3);
        analysisMap.put(JPLAG_ANALYSIS_ID, 4);
        analysisMap.put(ANALYSIS_PSEUDONYMS, new Object[]{getValidAnalysisPseudonymMap(), getValidAnalysisPseudonymMap()});
        return analysisMap;
    }

    /**
     * Builds a valid assignment map
     *
     * @return a valid assignment map
     */
    static public Map<String, Object> getValidAssignmentMap() {
        Map<String, Object> assignmentMap = new HashMap<String, Object>();
        assignmentMap.put(ID, 1);
        assignmentMap.put(TIMESTAMP, new Date());
        assignmentMap.put(COMPLETE, true);
        assignmentMap.put(ASSIGNMENT_ID, 2);
        assignmentMap.put(MOSS_ANALYSIS_ID, 3);
        assignmentMap.put(JPLAG_ANALYSIS_ID, 4);
        assignmentMap.put(ANALYSIS_PSEUDONYMS, new Object[]{getValidAnalysisPseudonymMap(), getValidAnalysisPseudonymMap()});
        return assignmentMap;
    }

    /**
     * Builds a valid offering map
     *
     * @return a valid offering map
     */
    static public Map<String, Object> getValidOfferingMap() {
        Map<String, Object> offeringMap = new HashMap<String, Object>();
        offeringMap.put(COURSE_ID, 1);
        offeringMap.put(FILESET_IDS, new Object[]{0});
        offeringMap.put(ID, 2);
        offeringMap.put(LDAP_DNS, "A");
        offeringMap.put(SEMESTER, getValidSemesterMap());
        return offeringMap;
    }

    /**
     * Builds a valid semester map
     *
     * @return a valid semester map
     */
    static public Map<String, Object> getValidSemesterMap() {
        Map<String, Object> semesterMap = new HashMap<String, Object>();
        semesterMap.put(ID, 1);
        semesterMap.put(SEASON, "fall");
        semesterMap.put(TYPE, "semester");
        semesterMap.put(YEAR, 2000000);
        return semesterMap;
    }
}
