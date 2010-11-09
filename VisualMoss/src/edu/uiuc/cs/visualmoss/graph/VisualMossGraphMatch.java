package edu.uiuc.cs.visualmoss.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Nov 8, 2010
 *
 * <p> <p> Simple representation of a match object from the graph
 */
public class VisualMossGraphMatch {

    /**
     * The students associated with this match
     */
    private final VisualMossGraphStudent student1, student2;

    /**
     * The scores associated with this match
     */
    private final double score1, score2;

    /**
     * Creates a new basic match object from the given parameters
     *
     * @param student1 The first student
     * @param student2 The second student
     * @param score1 The first match score
     * @param score2 The second match score
     */
    protected VisualMossGraphMatch(VisualMossGraphStudent student1, VisualMossGraphStudent student2, double score1, double score2) {
        this.score1 = score1;
        this.score2 = score2;
        this.student1 = student1;
        this.student2 = student2;
    }

    public VisualMossGraphStudent getStudent1() {
        return student1;
    }

    public VisualMossGraphStudent getStudent2() {
        return student2;
    }

    public double getScore1() {
        return score1;
    }

    public double getScore2() {
        return score2;
    }
}
