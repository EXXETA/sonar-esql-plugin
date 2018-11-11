/**
 *
 */
package com.exxeta.iss.sonar.msgflow.tree.impl;

import java.util.ArrayList;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowCommentNote;

/**
 * @author Arjav Shah
 *
 */
public class MessageFlowCommentNoteImpl implements MessageFlowCommentNote {
	/**
	 * an id of the node to which the comment note is associated with
	 */
	private final ArrayList<String> association;
	/**
	 * comment string of the comment note
	 */
	private final String comment;
	/**
	 * The x axis location of the comment note
	 */
	private final int locationX;
	/**
	 * The y axis location of the comment note
	 */
	private final int locationY;

	/**
	 * @param association
	 * @param comment
	 * @param locationX
	 * @param locationY
	 */
	public MessageFlowCommentNoteImpl(final ArrayList<String> association, final String comment, final int locationX,
			final int locationY) {
		super();
		this.association = association;
		this.comment = comment;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	public String comment() {
		return comment;
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
	 * This method returns the x axis location of the comment note
	 *
	 * @return the x axis location
	 */
	@Override
	public int locationX() {
		return locationX;
	}

	/**
	 * This method returns the y axis location of the comment note
	 *
	 * @return the y axis location
	 */
	@Override
	public int locationY() {
		return locationY;
	}

}