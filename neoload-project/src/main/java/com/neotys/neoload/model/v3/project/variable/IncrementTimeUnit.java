package com.neotys.neoload.model.v3.project.variable;

import java.util.Optional;
import java.util.regex.Pattern;

// A single amount and unit, e.g. "-5d", "3h", "2y". Units are not combined so that fixed-length units
// (ms/s/m/h) never mix with calendar units (d/mo/y), which shift by a variable number of milliseconds.
public enum IncrementTimeUnit {
	MILLISECOND("ms"),
	SECOND("s"),
	MINUTE("m"),
	HOUR("h"),
	DAY("d"),
	MONTH("mo"),
	YEAR("y");

	public static final Pattern PATTERN = Pattern.compile("^(-?\\d+)(ms|s|m|h|d|mo|y)$");

	private final String code;

	IncrementTimeUnit(final String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static Optional<IncrementTimeUnit> fromCode(final String code) {
		for (final IncrementTimeUnit unit : values()) {
			if (unit.code.equals(code)) {
				return Optional.of(unit);
			}
		}
		return Optional.empty();
	}

	// NeoLoad legacy XML "inc-type" attribute code for this unit.
	public int getDateIncrementTypeCode() {
		switch (this) {
			case MILLISECOND : return -1;
			case SECOND : return 0;
			case MINUTE : return 1;
			case HOUR : return 2;
			case DAY : return 3;
			case MONTH : return 4;
			case YEAR : return 5;
			default : return 0;
		}
	}
}
