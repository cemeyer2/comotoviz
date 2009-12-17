/**
 * 
 */
package edu.uiuc.cs.visualmoss.dataimport;

import java.util.ArrayList;


public class Course
{
	private String name;
	private ArrayList<Assignment> assignments;
	
	public Course(String name)
	{
		this.name = name;
		assignments = new ArrayList<Assignment>();
	}
	
	public void addAssignment(Assignment a)
	{
		assignments.add(a);
	}
	
	public final ArrayList<Assignment> getAssignments()
	{
		return assignments;
	}
	
	public String getName()
	{
		return this.name;
	}
}