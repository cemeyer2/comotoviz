package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Language;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Holds the data of an assignment
 */
public class Assignment {

    private int id;
    private int analysisId;
    private int courseId;
    private Language language;
    private String name;
    private List<Integer> fileSetIds;

    public Assignment(int id, int analysisId, int courseId, Language language, String name, List<Integer> fileSetIds) {
        this.id = id;
        this.analysisId = analysisId;
        this.courseId = courseId;
        this.language = language;
        this.name = name;
        this.fileSetIds = fileSetIds;
    }

    public int getId() {
        return id;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public int getCourseId() {
        return courseId;
    }

    public Language getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getFileSetIds() {
        return fileSetIds;
    }
}
