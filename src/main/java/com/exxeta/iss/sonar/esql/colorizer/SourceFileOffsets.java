package com.exxeta.iss.sonar.esql.colorizer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.sonar.sslr.api.Token;

public class SourceFileOffsets {
	private final int length;
	private final List<Integer> lineStartOffsets = Lists.newArrayList();

	public SourceFileOffsets(String content) {
		this.length = content.length();
		initOffsets(content);
	}

	public SourceFileOffsets(File file, Charset charset) {
		this(fileContent(file, charset));
	}

	private static String fileContent(File file, Charset charset) {
		String fileContent;
		try {
			fileContent = Files.toString(file, charset);
		} catch (IOException e) {
			throw new IllegalStateException("Could not read " + file, e);
		}
		return fileContent;
	}

	private void initOffsets(String toParse) {
		lineStartOffsets.add(0);
		int i = 0;
		while (i < length) {
			if (toParse.charAt(i) == '\n' || toParse.charAt(i) == '\r') {
				int nextLineStartOffset = i + 1;
				if (i < (length - 1) && toParse.charAt(i) == '\r' && toParse.charAt(i + 1) == '\n') {
					nextLineStartOffset = i + 2;
					i++;
				}
				lineStartOffsets.add(nextLineStartOffset);
			}
			i++;
		}
	}

	public int startOffset(Token token) {
		int lineStartOffset = lineStartOffsets.get(token.getLine() - 1);
		int column = token.getColumn();
		return lineStartOffset + column;
	}

	public int endOffset(Token token) {
		return startOffset(token) + token.getValue().length();
	}
}