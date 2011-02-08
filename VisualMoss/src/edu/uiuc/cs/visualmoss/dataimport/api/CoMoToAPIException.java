package edu.uiuc.cs.visualmoss.dataimport.api;

/**
 * Exception thrown on most exceptions relating to the VisualMoss application
 */
public class CoMoToAPIException extends Exception {

    /**
     * Unique identifier for this exception type
     */
    private static final long serialVersionUID = 8276931130814119675L;

    /**
     * The exception that caused this exception (i.e. the root exception)
     */
    private Exception causedBy;

    /**
     * Builds a standard java exception of type VisualMossException
     *
     * @param reason Message containing the reason for the exception
     */
    public CoMoToAPIException(String reason) {
        super(reason);
    }

    /**
     * Builds a java exception of type VisualMossException with the root exception
     *
     * @param reason   Message containing the reason for the exception
     * @param causedBy A handle on the exception that caused this exception
     */
    public CoMoToAPIException(String reason, Exception causedBy) {
        super(reason + ": " + causedBy.getMessage());
        this.causedBy = causedBy;
    }

    /**
     * Gets the root exception for this typed VisualMossException
     *
     * @return A handle to the exception that caused this one
     */
    public Exception getCausedBy() {
        return causedBy;
    }
}
