package edu.uiuc.cs.visualmoss.dataimport.api.dao;

import edu.uiuc.cs.visualmoss.dataimport.api.Student;

import java.util.Map;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Encapsulates accessing a <code>Student</code> object from the CoMoTo API through the XMLRPC singleton  
 */
public class StudentDAO extends CoMoToDAO<Student> {

    @Override
    protected Student getById(int id){
        Map struct = xmlrpc.getStruct("getStudent", id);
        return new Student(id, null, null, null, null);
    }
}
