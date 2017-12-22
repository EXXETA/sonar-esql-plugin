package com.exxeta.iss.sonar.esql.codecoverage;

import org.sonar.api.batch.fs.InputFile;

public class CodePosition {
	private final InputFile file;
	private final int line;
	public CodePosition(InputFile file, int line) {
		super();
		this.file = file;
		this.line = line;
	}
	public InputFile getFile() {
		return file;
	}
	public int getLine() {
		return line;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + line;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodePosition other = (CodePosition) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (line != other.line)
			return false;
		return true;
	}
	
	
}
