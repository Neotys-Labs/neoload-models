package com.neotys.neoload.model.v3.project.variable;

import java.util.Optional;
import java.util.regex.Matcher;

// Shared "<amount><unit>" encoding used by both CurrentDateVariable's offset and
// DateVariable's change_step.
public final class Offset {

	private final int amount;
	private final IncrementTimeUnit unit;

	private Offset(final int amount, final IncrementTimeUnit unit) {
		this.amount = amount;
		this.unit = unit;
	}

	public static Optional<Offset> parse(final String offset) {
		if (offset == null) {
			return Optional.empty();
		}
		final Matcher matcher = IncrementTimeUnit.PATTERN.matcher(offset);
		if (!matcher.matches()) {
			return Optional.empty();
		}
		final int amount;
		try {
			amount = Integer.parseInt(matcher.group(1));
		} catch (final NumberFormatException e) {
			// The regex accepts any number of digits: an amount beyond int range is not a usable offset.
			return Optional.empty();
		}
		return IncrementTimeUnit.fromCode(matcher.group(2)).map(unit -> new Offset(amount, unit));
	}

	public int getAmount() {
		return amount;
	}

	public IncrementTimeUnit getUnit() {
		return unit;
	}
}
