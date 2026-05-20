package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ThinkTimeMode {

	CONSTANT, RANDOM;

	@JsonCreator
	public static ThinkTimeMode of(final String name) {
		if (!Strings.isNullOrEmpty(name)) {
			try {
				return ThinkTimeMode.valueOf(name.toUpperCase());
			} catch (final IllegalArgumentException iae) {
				LOGGER.warn("ThinkTimeMode value not supported");
			}
		}
		throw new IllegalArgumentException("The think_time_mode must be: 'constant' or 'random'.");
	}

	@JsonValue
	public String getName() {
		return name().toLowerCase();
	}

	public final static String THINK_TIME_MODE = "think_time_mode";

	private static final Logger LOGGER = LoggerFactory.getLogger(ThinkTimeMode.class);
}