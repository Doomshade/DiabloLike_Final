package cz.helheim.rpg.util;

import org.apache.commons.lang3.Validate;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Doomshade
 * @version 1.0
 * @since 27.06.2022
 */
public class Range implements Iterable<Integer> {
	private static final Pattern rangePattern = Pattern.compile("(?<lower>\\d+)(-(?<upper>\\d+))?");
	private final int lower;
	private final int upper;

	public Range(final int lower, final int upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public Range(String range) {
		Validate.notNull(range, "Range cannot be null!");
		final Matcher m = rangePattern.matcher(range);
		if (!m.find()) {
			throw new IllegalArgumentException("Invalid range " + range);
		}
		this.lower = Integer.parseInt(m.group("lower"));
		this.upper = m.groupCount() == 2 ? Integer.parseInt(m.group("upper")) : lower;
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

	@Override
	public Iterator<Integer> iterator() {
		return new RangeIterator();
	}

	private class RangeIterator implements Iterator<Integer> {
		private final int[] nums;
		private int curr = -1;

		{
			if (upper < lower) {
				nums = new int[0];
			} else {
				nums = new int[upper - lower + 1];
				int num = lower;
				int i = 0;
				while (num <= upper) {
					nums[i++] = num++;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return curr < upper;
		}

		@Override
		public Integer next() {
			if (curr == -1) {
				curr = 0;
			}
			return nums[curr++];
		}
	}
}
