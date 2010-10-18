package edu.uiuc.cs.visualmoss.dataimport.api;

import edu.uiuc.cs.visualmoss.dataimport.api.dao.SubmissionDAO;

import java.util.LinkedList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 17, 2010
 *
 * <p> <p> Data container for a student
 */
public class Student {

    private int id;
    private String netid, historyLink;
     private List<MossMatch> matches;
     private List<Integer> submissionIds;

     public Student(int id, String netid, String historyLink, List<MossMatch> matches, List<Integer> submissionIds) {
         this.id = id;
         this.netid = netid;
         this.historyLink = historyLink;
         this.matches = matches;
         this.submissionIds = submissionIds;
     }

     public int getId() {
         return id;
     }

     public String getHistoryLink() {
         return historyLink;
     }

     public List<MossMatch> getMossMatches() {
         return matches;
     }

     public List<Submission> getSubmissions() {
         List<Submission> submissions = new LinkedList<Submission>();
         SubmissionDAO dao = new SubmissionDAO();

         for(int id : submissionIds) {
            submissions.add(dao.getById(id));
         }

         return submissions;
     } 
}
