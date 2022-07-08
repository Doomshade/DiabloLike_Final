package cz.helheim.rpg.util;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class Range {
	private final int lower;
	private final int upper;

	public Range(final int lower, final int upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public int getUpper() {
		return upper;
	}

	public Range add(final int lower, final int upper) {
		return new Range(this.lower + lower, this.upper + upper);
	}

	public Range surround(final int num) {
		return new Range(num - lower, num + upper);
	}

	public boolean isInRange(final int num, final boolean inclusiveLeft, final boolean inclusiveRight) {
		return (num > lower && num < upper) || (inclusiveLeft && num == lower) || (inclusiveRight && num == upper);
	}
}
