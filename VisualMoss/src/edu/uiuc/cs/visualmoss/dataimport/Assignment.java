/**
 * 
 */
package edu.uiuc.cs.visualmoss.dataimport;

public class Assignment
{
	private String name, language, workDir, webDirectory;
	private boolean hasAnalysis;
	private Course course;
	
	public Assignment(Course course, String name, String language, String workDir, String webDirectory, boolean hasAnalysis)
	{
		this.name = name;
		this.language = language;
		this.workDir = workDir;
		this.hasAnalysis = hasAnalysis;
		this.course = course;
		this.webDirectory = webDirectory;
	}
	
	public final Course getCourse()
	{
		return course;
	}

	public final String getName() {
		return name;
	}

	public final String getLanguage() {
		return language;
	}

	public final String getWorkDir() {
		return workDir;
	}

	public final boolean isHasAnalysis() {
		return hasAnalysis;
	}
	
	public String toString()
	{
		return getName();
	}
	
	public final String getWebDirectory()
	{
		return this.webDirectory;
	}
}