package edu.uiuc.cs.visualmoss.dataimport.api.objects;

import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPI;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIConnection;
import edu.uiuc.cs.visualmoss.dataimport.api.CoMoToAPIReflector;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 18, 2010
 *
 * <p> <p> Holds the data of a semester
 */
public class Semester implements Refreshable{

    /**
     * The unique id for this semester
     */
    private int id;

    /**
     * The 'type' for this semester
     */
    private Type type;

    /**
     * The season
     */
    private Season season;

    /**
     * The year
     */
    private int year;

    /**
     * A connection to the API for lazily loading and refreshing data
     */
    private CoMoToAPIConnection connection;

    public Semester(Map abstractSemester, CoMoToAPIConnection connection) {

        //Save the connection
        this.connection = connection;

        //Use reflection to populate the rest of the object
        CoMoToAPIReflector<Semester> reflector = new CoMoToAPIReflector<Semester>();
        reflector.populate(this, abstractSemester);
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {

        //First, grab the new object from the API
        Semester newSemester = CoMoToAPI.getSemester(connection, id);

        //Copy the primitive data
        type = newSemester.getType();
        season = newSemester.getSeason();
        year = newSemester.getYear();
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
