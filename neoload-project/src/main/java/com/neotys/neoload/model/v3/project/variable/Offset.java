package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.OffsetToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToOffsetConverter;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * Shared "&lt;amount&gt;&lt;unit&gt;" encoding used by both CurrentDateVariable's offset and
 * DateVariable's change_step. Text that does not match the pattern, or whose amount overflows
 * {@code int}, is the invalid state: it keeps the raw text so that validation reports it and writing
 * gives it back unchanged.
 */
@JsonSerialize(converter = OffsetToStringConverter.class)
@JsonDeserialize(converter = StringToOffsetConverter.class)
public final class Offset {
	private final int amount;
	private final IncrementTimeUnit unit;
	private final String invalidText;

	private Offset(final int amount, final IncrementTimeUnit unit, final String invalidText) {
		this.amount = amount;
		this.unit = unit;
		this.invalidText = invalidText;
	}

	public static Offset of(final int amount, final IncrementTimeUnit unit) {
		return new Offset(amount, Objects.requireNonNull(unit), null);
	}

	/**
	 * @return the parsed offset, or the invalid state holding {@code offset} when it does not match the
	 *     "&lt;amount&gt;&lt;unit&gt;" pattern or the amount overflows {@code int}.
	 */
	public static Offset parse(final String offset) {
		final Matcher matcher = IncrementTimeUnit.PATTERN.matcher(offset);
		if (!matcher.matches()) {
			return invalid(offset);
		}
		try {
			final int amount = Integer.parseInt(matcher.group(1));
			return IncrementTimeUnit.fromCode(matcher.group(2)).map(unit -> of(amount, unit)).orElseGet(() -> invalid(offset));
		} catch (final NumberFormatException e) {
			return invalid(offset);
		}
	}

	public boolean isInvalid() {
		return invalidText != null;
	}

	/**
	 * @throws IllegalStateException if the offset is invalid.
	 */
	public int getAmount() {
		requireValid();
		return amount;
	}

	/**
	 * @throws IllegalStateException if the offset is invalid.
	 */
	public IncrementTimeUnit getUnit() {
		requireValid();
		return unit;
	}

	/**
	 * @return e.g. {@code -5d}, or the raw text when invalid.
	 */
	public String toText() {
		return isInvalid() ? invalidText : amount + unit.getCode();
	}

	private void requireValid() {
		if (isInvalid()) {
			throw new IllegalStateException("Invalid offset: " + invalidText);
		}
	}

	private static Offset invalid(final String text) {
		return new Offset(0, null, text);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Offset)) {
			return false;
		}
		final Offset that = (Offset) other;
		return amount == that.amount && unit == that.unit && Objects.equals(invalidText, that.invalidText);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, unit, invalidText);
	}

	@Override
	public String toString() {
		return toText();
	}
}
