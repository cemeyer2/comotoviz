package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Season;
import edu.uiuc.cs.visualmoss.dataimport.api.objects.enums.Type;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data of a semester
 */
public class Semester {

    private int id;
    private Type type = Type.semester;
    private Season season;
    private int year;

    public Semester(int id, Season season, int year) {
        this.id = id;
        this.season = season;
        this.year = year;
    }

    public Semester(int id, Type type, Season season, int year) {
        this.id = id;
        this.type = type;
        this.season = season;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Season getSeason() {
        return season;
    }

    public int getYear() {
        return year;
    }
}
