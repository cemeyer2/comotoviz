package edu.uiuc.cs.visualmoss.utility;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Dec 4, 2010
 *
 * <p> <p> This implements a generic, typed Pair object
 */
public class Pair<A extends Comparable, B extends Comparable> implements Comparable< Pair<A, B> > {

    /**
     * The first element in the pair
     */
    private A first;

    /**
     * The second element in the pair
     */
    private B second;

    /**
     * Builds a pair containing the two items input
     *
     * @param first The first item in the pair
     * @param second The second item in the pair
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Implements the comparable interface to compare two pairs to each other
     *
     * @param otherPair The pair to which to compare this pair
     * @return  An integer value, representing the comparison of these two pairs (ordering)
     *
     *
     */
    public int compareTo(Pair<A, B> otherPair) {

        // If the pairs are the same object or contain the exact same objects
        if(otherPair == this || (otherPair.getFirst() == first && second == otherPair.getSecond() )){
            return 0;

        } else if(first.equals(otherPair.getFirst()) && second.equals(otherPair.getSecond())){
            return 0;

        } else if(first.compareTo(otherPair.getFirst()) == 0){
            return second.compareTo(otherPair.getSecond());

        } else {
            return first.compareTo(otherPair.getFirst());
        }
    }

    /**
     * Checks to see if this pair is equal to another pair
     *
     * @param otherPair The pair to which to compare this object
     * @return  Whether or not this pair is equal to a given pair
     */
    @Override
    public boolean equals(Object otherPair) {
        if (this == otherPair)
            return true;
        if (!(otherPair instanceof Pair))
            return false;

        Pair pair = (Pair) otherPair;

        if (!first.equals(pair.getFirst()))
            return false;
        if (!second.equals(pair.getSecond()))
            return false;
        return true;
    }

    /**
     * Calculates the hash code of this pair
     *
     * @return  The hashcode for this pair
     */
    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}