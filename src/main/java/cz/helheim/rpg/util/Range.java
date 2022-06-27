package cz.helheim.rpg.util;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class Range {
	private final int min;
	private final int max;

	public Range(final int min, final int max) {
		this.min = Math.min(min, max);
		this.max = Math.max(min, max);
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
