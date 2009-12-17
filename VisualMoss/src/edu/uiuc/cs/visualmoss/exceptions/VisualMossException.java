package edu.uiuc.cs.visualmoss.exceptions;

public class VisualMossException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8276931130814119675L;
	
	private Exception causedBy;

	public VisualMossException(String reason)
	{
		super(reason);
	}
	
	public VisualMossException(String reason, Exception causedBy)
	{
		super(reason+": "+causedBy.getMessage());
		this.causedBy = causedBy;
	}
	
	public Exception getCausedBy()
	{
		return causedBy;
	}
}
