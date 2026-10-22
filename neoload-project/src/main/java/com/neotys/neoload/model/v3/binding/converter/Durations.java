package com.neotys.neoload.model.v3.binding.converter;

import com.neotys.neoload.model.v3.project.variable.Offset;
import java.util.regex.Matcher;

/**
 * Parses, formats and validates duration texts against a {@link DurationGrammar}. Every failure is an
 * {@link InvalidDurationException}, never a {@link NumberFormatException}.
 */
public final class Durations {
	private static final String ZERO = "0";
	private static final String[] UNIT_GROUPS = {"h", "m", "s", "ms"};

	private Durations() {
		super();
	}

	public static ParsedDuration parse(final String text, final DurationGrammar grammar) {
		if (text == null) {
			throw new InvalidDurationException(text);
		}
		if (grammar == DurationGrammar.OFFSET) {
			return parseOffset(text);
		}
		final Matcher matcher = grammar.getPattern().matcher(text);
		if (!matcher.matches()) {
			throw new InvalidDurationException(text);
		}
		if (grammar.isVariableAllowed() && matcher.group("var") != null) {
			return ParsedDuration.variable(matcher.group("var"));
		}
		return ParsedDuration.literal(amountOf(text, matcher, grammar), grammar.getDefaultUnit());
	}

	public static boolean validate(final String text, final DurationGrammar grammar) {
		try {
			parse(text, grammar);
			return true;
		} catch (final InvalidDurationException e) {
			return false;
		}
	}

	public static String format(final long value, final DurationGrammar grammar) {
		if (value <= 0) {
			return ZERO;
		}
		final long[] unitSizes = formattableUnitSizes(grammar);
		final StringBuilder sb = new StringBuilder();
		long remaining = value;
		for (int i = 0; i < UNIT_GROUPS.length; i++) {
			if (unitSizes[i] == 0) {
				continue;
			}
			final long count = remaining / unitSizes[i];
			remaining -= count * unitSizes[i];
			if (count != 0) {
				sb.append(count).append(UNIT_GROUPS[i]);
			}
		}
		return sb.toString();
	}

	private static long[] formattableUnitSizes(final DurationGrammar grammar) {
		if (grammar.getUnitSizes().length == 0) {
			throw new IllegalArgumentException("Grammar cannot format a plain amount.");
		}
		return grammar.getUnitSizes();
	}

	private static ParsedDuration parseOffset(final String text) {
		final Offset offset = Offset.parse(text).orElseThrow(() -> new InvalidDurationException(text));
		return ParsedDuration.literal(offset.getAmount(), offset.getUnit().getCode());
	}

	private static long amountOf(final String text, final Matcher matcher, final DurationGrammar grammar) {
		final long[] unitSizes = grammar.getUnitSizes();
		try {
			final String plain = matcher.group("plain");
			if (plain != null) {
				return checkMax(Long.parseLong(plain), grammar);
			}
			long total = 0;
			for (int i = 0; i < UNIT_GROUPS.length; i++) {
				final String group = unitSizes[i] == 0 ? null : matcher.group(UNIT_GROUPS[i]);
				if (group != null) {
					total = Math.addExact(total, Math.multiplyExact(Long.parseLong(group), unitSizes[i]));
				}
			}
			return checkMax(total, grammar);
		} catch (final NumberFormatException | ArithmeticException e) {
			throw new InvalidDurationException(text);
		}
	}

	private static long checkMax(final long value, final DurationGrammar grammar) {
		if (value > grammar.getMaxValue()) {
			throw new ArithmeticException();
		}
		return value;
	}
}
