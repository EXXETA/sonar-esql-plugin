package com.exxeta.iss.sonar.esql;

/**
 * Exception thrown when the context is cancelled.
 *
 */
class CancellationException extends RuntimeException {

	private static final long serialVersionUID = 6753692484342076443L;

	CancellationException(String message) {
		super(message);
	}

}
