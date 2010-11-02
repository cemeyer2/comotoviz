package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data of a semester
 */
public class Semester {

    private int id;
    private Type type;
    private Season season;
    private int year;

    public Semester() {
    }

    public Semester(Map abstractSemester) {
        CoMoToAPIReflector<Semester> reflector = new CoMoToAPIReflector<Semester>();
        reflector.populate(this, abstractSemester);
    }

    public Map getMap(){
        return new HashMap();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Type.valueOf(type);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = Season.valueOf(season);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
