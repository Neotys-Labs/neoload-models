package com.neotys.neoload.model.v3.project.duration;

import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A duration grammar: a plain number in the grammar's default unit, or ordered {@code h}, {@code m},
 * {@code s} (and {@code ms} when allowed) units such as {@code 1h30m}. Parsing and formatting are shared
 * by every typed duration so that they accept, and write, the same text.
 */
final class DurationGrammar {
	static final DurationGrammar MILLISECONDS = new DurationGrammar(
			new String[] {"h", "m", "s", "ms"}, new long[] {3_600_000, 60_000, 1_000, 1});
	static final DurationGrammar SECONDS = new DurationGrammar(
			new String[] {"h", "m", "s"}, new long[] {3_600, 60, 1});

	private static final String ZERO = "0";

	private final String[] units;
	private final long[] factors;
	private final Pattern pattern;

	private DurationGrammar(final String[] units, final long[] factors) {
		this.units = units;
		this.factors = factors;
		this.pattern = Pattern.compile(patternOf(units));
	}

	private static String patternOf(final String[] units) {
		final StringBuilder sb = new StringBuilder("^\\s*(?:(\\d+)|(?=\\d)");
		for (final String unit : units) {
			sb.append("(?:(\\d+)").append(unit).append("\\s*)?");
		}
		return sb.append(")\\s*$").toString();
	}

	/**
	 * @return the amount in the default unit, or empty when the text does not follow the grammar or overflows.
	 */
	OptionalLong parse(final String text) {
		final Matcher matcher = pattern.matcher(text);
		if (!matcher.matches()) {
			return OptionalLong.empty();
		}
		try {
			return OptionalLong.of(amountOf(matcher));
		} catch (final NumberFormatException | ArithmeticException e) {
			return OptionalLong.empty();
		}
	}

	private long amountOf(final Matcher matcher) {
		if (matcher.group(1) != null) {
			return Long.parseLong(matcher.group(1));
		}
		long amount = 0;
		for (int i = 0; i < units.length; i++) {
			final String group = matcher.group(i + 2);
			if (group != null) {
				amount = Math.addExact(amount, Math.multiplyExact(Long.parseLong(group), factors[i]));
			}
		}
		return amount;
	}

	String format(final long amount) {
		if (amount <= 0) {
			return ZERO;
		}
		final StringBuilder sb = new StringBuilder();
		long remaining = amount;
		for (int i = 0; i < units.length; i++) {
			final long count = remaining / factors[i];
			if (count > 0) {
				sb.append(count).append(units[i]);
				remaining -= count * factors[i];
			}
		}
		return sb.toString();
	}
}
