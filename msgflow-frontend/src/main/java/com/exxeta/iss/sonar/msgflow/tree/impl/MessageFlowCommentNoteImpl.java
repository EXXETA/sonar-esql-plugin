/**
 *
 */
package com.exxeta.iss.sonar.msgflow.tree.impl;

import java.util.List;

import com.exxeta.iss.sonar.msgflow.api.tree.MessageFlowCommentNote;
import com.google.common.collect.ImmutableList;


public class MessageFlowCommentNoteImpl implements MessageFlowCommentNote {
	/**
	 * an id of the node to which the comment note is associated with
	 */
	private final List<String> associations;
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
	public MessageFlowCommentNoteImpl(final List<String> associations, final String comment, final int locationX,
			final int locationY) {
		super();
		this.associations = associations;
		this.comment = comment;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	@Override
	public int locationX() {
		return locationX;
	}

	@Override
	public int locationY() {
		return locationY;
	}

	@Override
	public String comment() {
		return comment;
	}

	@Override
	public List<String> associations() {
		return ImmutableList.copyOf(associations);
	}



}