package com.neotys.neoload.model.v3.project.duration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.SecondsDurationToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToSecondsDurationConverter;
import java.util.OptionalLong;

/**
 * A duration in seconds: a plain number of seconds, or ordered {@code h}, {@code m}, {@code s} units
 * such as {@code 1h30m}. Variables are not accepted.
 */
@JsonSerialize(converter = SecondsDurationToStringConverter.class)
@JsonDeserialize(converter = StringToSecondsDurationConverter.class)
public final class SecondsDuration extends DurationValue {
	private SecondsDuration(final Long amount, final String invalidText) {
		super(DurationGrammar.SECONDS, amount, null, invalidText);
	}

	/**
	 * @return a literal of {@code seconds}, or the invalid state when negative.
	 */
	public static SecondsDuration of(final long seconds) {
		return seconds < 0 ? new SecondsDuration(null, String.valueOf(seconds)) : new SecondsDuration(seconds, null);
	}

	/**
	 * @return the parsed duration, or the invalid state holding {@code text} when it follows no grammar or overflows.
	 */
	public static SecondsDuration parse(final String text) {
		final OptionalLong seconds = DurationGrammar.SECONDS.parse(text);
		return seconds.isPresent() ? new SecondsDuration(seconds.getAsLong(), null) : new SecondsDuration(null, text);
	}
}
