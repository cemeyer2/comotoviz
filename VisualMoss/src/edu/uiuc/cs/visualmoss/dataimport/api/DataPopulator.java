package edu.uiuc.cs.visualmoss.dataimport.api;

import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 22, 2010
 *
 * <p> <p>
 */
public enum DataPopulator {

    analysis {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    assignment {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    course {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    fileSet {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    mossAnalysis {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    report {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    student {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    submission {
        @Override
        public Object getData(Map data) {
            return null;
        }
    },

    submissionFile {
        @Override
        public Object getData(Map data) {
            return null;
        }
    };

    public abstract Object getData(Map data);
}
