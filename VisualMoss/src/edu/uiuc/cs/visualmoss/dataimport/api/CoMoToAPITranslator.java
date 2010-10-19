package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.*;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Translates the generic 'Map' and 'List' fields from the XML-RPC server into usable objects
 */
public enum CoMoToAPITranslator {

    getAssignments {

        @Override
        public List<Assignment> getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getCourses {

        @Override
        public List<Course> getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getFileSet {

        @Override
        public FileSet getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getMossAnalysis {

        @Override
        public MossAnalysis getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getMossMatch {

        @Override
        public MossMatch getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getReport {

        @Override
        public Report getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getStudent {

        @Override
        public Student getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getSubmission {

        @Override
        public Object getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    },

    getSubmissionFile {

        @Override
        public Object getById(CoMoToAPIConnection connection, int id) {
            return null;
        }
    };

    public abstract Object getById(CoMoToAPIConnection connection, int id);
}

