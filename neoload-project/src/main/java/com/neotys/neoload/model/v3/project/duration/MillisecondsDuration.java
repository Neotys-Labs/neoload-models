package com.neotys.neoload.model.v3.project.duration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.MillisecondsDurationToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToMillisecondsDurationConverter;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import java.math.BigDecimal;
import java.util.OptionalLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A duration in milliseconds: a plain number of milliseconds, ordered {@code h}, {@code m}, {@code s},
 * {@code ms} units such as {@code 1s500ms}, or a {@code ${variable}} reference. A variable is referenced
 * as {@code ${name}} or {@code ${name.field}} (see {@link Duration}).
 */
@JsonSerialize(converter = MillisecondsDurationToStringConverter.class)
@JsonDeserialize(converter = StringToMillisecondsDurationConverter.class)
public final class MillisecondsDuration extends DurationValue {
	private static final Pattern VARIABLE = Pattern.compile(
			"^\\s*\\$\\{(" + Duration.VARIABLE_NAME_PATTERN + "(?:\\." + Duration.VARIABLE_NAME_PATTERN + ")*)\\}\\s*$");
	private static final Pattern DECIMAL_SECONDS = Pattern.compile("^\\s*\\d+(\\.\\d+)?\\s*$");
	private static final int MILLISECONDS_PER_SECOND_SCALE = 3;

	private MillisecondsDuration(final Long amount, final String variable, final String invalidText) {
		super(DurationGrammar.MILLISECONDS, amount, variable, invalidText);
	}

	/**
	 * @return a literal of {@code milliseconds}, or the invalid state when negative.
	 */
	public static MillisecondsDuration of(final long milliseconds) {
		return milliseconds < 0 ? invalid(String.valueOf(milliseconds)) : new MillisecondsDuration(milliseconds, null, null);
	}

	public static MillisecondsDuration ofVariable(final String name) {
		return new MillisecondsDuration(null, name, null);
	}

	/**
	 * @return the parsed duration, or the invalid state holding {@code text} when it is neither a duration nor a variable.
	 */
	public static MillisecondsDuration parse(final String text) {
		final Matcher variable = VARIABLE.matcher(text);
		if (variable.matches()) {
			return ofVariable(variable.group(1));
		}
		final OptionalLong milliseconds = DurationGrammar.MILLISECONDS.parse(text);
		return milliseconds.isPresent() ? of(milliseconds.getAsLong()) : invalid(text);
	}

	/**
	 * @param seconds a possibly decimal number of seconds (e.g. {@code 1.5}) or a variable reference.
	 * @return the matching duration, or the invalid state holding {@code seconds}.
	 */
	public static MillisecondsDuration fromSeconds(final String seconds) {
		final Matcher variable = VARIABLE.matcher(seconds);
		if (variable.matches()) {
			return ofVariable(variable.group(1));
		}
		if (!DECIMAL_SECONDS.matcher(seconds).matches()) {
			return invalid(seconds);
		}
		try {
			return of(new BigDecimal(seconds.trim()).movePointRight(MILLISECONDS_PER_SECOND_SCALE).longValueExact());
		} catch (final ArithmeticException e) {
			return invalid(seconds);
		}
	}

	/**
	 * @return the duration as a number of seconds (e.g. {@code 1.5} for 1500 ms); a variable or invalid text is returned unchanged.
	 */
	public String toSeconds() {
		if (!isLiteral()) {
			return toText();
		}
		return BigDecimal.valueOf(getAmount()).movePointLeft(MILLISECONDS_PER_SECOND_SCALE).stripTrailingZeros().toPlainString();
	}

	private static MillisecondsDuration invalid(final String text) {
		return new MillisecondsDuration(null, null, text);
	}
}
