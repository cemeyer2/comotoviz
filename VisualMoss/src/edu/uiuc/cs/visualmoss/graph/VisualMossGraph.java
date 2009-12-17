package edu.uiuc.cs.visualmoss.graph;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

public class VisualMossGraph 
{
	private Graph graph;
	private Hashtable<String, Student> students;
	private ArrayList<Match> matches;
	private String courseName;
	private String mpName;
	
	private VisualMossGraph(String courseName, String mpName)
	{
		students = new Hashtable<String, Student>();
		matches = new ArrayList<Match>();
		this.courseName = courseName;
		this.mpName = mpName;
	}
	
	public VisualMossGraph(InputStream stream, String courseName, String mpName) throws DataIOException
	{
		this(courseName, mpName);
		graph = new GraphMLReader().readGraph(stream);
		init();
	}
	
	public VisualMossGraph(File f, String courseName, String mpName) throws DataIOException
	{
		this(courseName, mpName);
		graph = new GraphMLReader().readGraph(f);
		init();
	}
	
	public VisualMossGraph(Graph g, String courseName, String mpName) throws DataIOException
	{
		this(courseName, mpName);
		graph = g;
		init();
	}
	
	private void init()
	{
		//init students
		Iterator iter = graph.nodes();
		while(iter.hasNext())
		{
			Node node = (Node)iter.next();
			String netid = node.getString("netid");
			String pseudonym = node.getString("pseudonym");
			Student student = new Student(netid, pseudonym);
			students.put(netid, student);
		}
		iter = graph.edges();
		while(iter.hasNext())
		{
			Edge edge = (Edge)iter.next();
			Student student1 = students.get(edge.getSourceNode().getString("netid"));
			Student student2 = students.get(edge.getTargetNode().getString("netid"));
			double score1 = edge.getDouble("score1");
			double score2 = edge.getDouble("score2");
			Match match = new Match(student1, student2, score1, score2);
			matches.add(match);
			student1.addMatch(match);
			student2.addMatch(match);
		}
	}
	
	public List<Student> getStudents()
	{
		ArrayList<Student> list = new ArrayList<Student>(students.values());
		Collections.sort(list);
		return list;
	}
	
	public List<Match> getMatches()
	{
		return matches;
	}
	
	public Graph getPrefuseGraph()
	{
		return graph;
	}
	
	
	public final String getCourseName() {
		return courseName;
	}

	public final void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public final String getMpName() {
		return mpName;
	}

	public final void setMpName(String mpName) {
		this.mpName = mpName;
	}


	public class Student implements Comparable<Student>
	{
		private final String netid, pseudonym;
		private final ArrayList<Match> matches;
		
		protected Student(String netid, String pseudonym)
		{
			this.netid = netid;
			this.pseudonym = pseudonym;
			this.matches = new ArrayList<Match>();
		}
		
		public String getNetid()
		{
			return netid;
		}
		
		public String getPseudonym()
		{
			return pseudonym;
		}
		
		protected void addMatch(Match match)
		{
			matches.add(match);
		}
		
		public List<Match> getMatches()
		{
			return matches;
		}

		public int compareTo(Student arg0) {
			return getNetid().compareTo(arg0.getNetid());
		}
		
		public String toString()
		{
			return getNetid();
		}
	}
	
	public class Match
	{
		private final Student student1, student2;
		private final double score1, score2;
		
		protected Match(Student student1, Student student2, double score1, double score2)
		{
			this.score1 = score1;
			this.score2 = score2;
			this.student1 = student1;
			this.student2 = student2;
		}

		public Student getStudent1() {
			return student1;
		}

		public Student getStudent2() {
			return student2;
		}

		public double getScore1() {
			return score1;
		}

		public double getScore2() {
			return score2;
		}
		
		
	}
}
