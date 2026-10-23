package com.neotys.neoload.model.v3.binding.converter;

import java.util.Objects;

/**
 * The result of parsing a duration text: either a literal amount in the unit of the field, or a
 * reference to a variable.
 */
public final class ParsedDuration {
	private final long amount;
	private final String unit;
	private final String variableName;

	private ParsedDuration(final long amount, final String unit, final String variableName) {
		this.amount = amount;
		this.unit = unit;
		this.variableName = variableName;
	}

	public static ParsedDuration literal(final long amount, final String unit) {
		return new ParsedDuration(amount, unit, null);
	}

	public static ParsedDuration variable(final String variableName) {
		return new ParsedDuration(0, null, variableName);
	}

	public boolean isVariable() {
		return variableName != null;
	}

	public long getAmount() {
		return amount;
	}

	public String getUnit() {
		return unit;
	}

	public String getVariableName() {
		return variableName;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ParsedDuration)) {
			return false;
		}
		final ParsedDuration that = (ParsedDuration) other;
		return amount == that.amount && Objects.equals(unit, that.unit) && Objects.equals(variableName, that.variableName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, unit, variableName);
	}

	@Override
	public String toString() {
		return isVariable() ? "${" + variableName + "}" : amount + unit;
	}
}
