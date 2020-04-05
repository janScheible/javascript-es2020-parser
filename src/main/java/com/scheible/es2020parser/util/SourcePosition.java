package com.scheible.es2020parser.util;

import java.util.Objects;

/**
 *
 * @author sj
 */
public class SourcePosition {

	private final int line;
	private final int start;
	private final int end;

	public SourcePosition(final int line, final int start, final int end) {
		this.line = line;
		this.start = start;
		this.end = end;
	}

	/**
	 * @return line number (1..n)
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return start char position in line (0..n)
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return end char position in line (0..n)
	 */
	public int getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		return Objects.hash(line, start, end);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj != null && getClass().equals(obj.getClass())) {
			final SourcePosition other = (SourcePosition) obj;
			return Objects.equals(line, other.line) && Objects.equals(start, other.start)
					&& Objects.equals(end, other.end);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return line + ":" + start + "-" + end;
	}
}
