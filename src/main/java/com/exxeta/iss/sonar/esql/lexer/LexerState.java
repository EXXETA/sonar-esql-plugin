package com.exxeta.iss.sonar.esql.lexer;

import java.util.ArrayDeque;
import java.util.Deque;

public class LexerState {
	final Deque<Integer> indentationStack = new ArrayDeque<Integer>();
	int brackets;
	boolean joined;

	public void reset() {
		indentationStack.clear();
		indentationStack.push(0);
		brackets = 0;
		joined = false;
	}
}