/**
 * 
 */
package com.exxeta.iss.sonar.msgflow.parser;

import java.util.ArrayList;

/**
 * @author Arjav Shah
 *
 */
public class MessageFlowCommentNote {
	/**
	 * an id of the node to which the comment note is associated with
	 */
	private ArrayList<String> association;
	/**
	 * comment string of the comment note 
	 */
	private String comment;
	/**
	 * The x axis location of the comment note
	 */
	private int locationX;
	/**
	 * The y axis location of the comment note
	 */
	private int locationY;
		
	
	/**
	 * @param association
	 * @param comment
	 * @param locationX
	 * @param locationY
	 */
	public MessageFlowCommentNote(ArrayList<String> association, String comment, int locationX, int locationY) {
		super();
		this.association = association;
		this.comment = comment;
		this.locationX = locationX;
		this.locationY = locationY;
	}
	/**
	 * This method returns the list of associated nodes for the comment note
	 * 
	 * @return the list of associated node id
	 */
	public ArrayList<String> getAssociation() {
		return association;
	}
	/**
	 * This method returns the comment string
	 * 
	 * @return the comment string
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * This method returns the x axis location of the comment note
	 * 
	 * @return the x axis location
	 */
	public int getLocationX() {
		return locationX;
	}
	/**
	 * This method returns the y axis location of the comment note
	 * 
	 * @return the y axis location
	 */
	public int getLocationY() {
		return locationY;
	}

}