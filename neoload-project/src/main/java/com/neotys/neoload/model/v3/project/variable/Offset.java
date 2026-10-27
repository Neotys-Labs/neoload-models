package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.OffsetDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.OffsetSerializer;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Shared "&lt;amount&gt;&lt;unit&gt;" encoding used by both CurrentDateVariable's offset and
 * DateVariable's change_step.
 */
@JsonSerialize(using = OffsetSerializer.class)
@JsonDeserialize(using = OffsetDeserializer.class)
public final class Offset {

	private final int amount;
	private final IncrementTimeUnit unit;

	private Offset(final int amount, final IncrementTimeUnit unit) {
		this.amount = amount;
		this.unit = unit;
	}

	/**
	 * @return empty if {@code offset} does not match the "&lt;amount&gt;&lt;unit&gt;" pattern, or if
	 *     the amount overflows {@code int}.
	 */
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

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Offset)) {
			return false;
		}
		final Offset offset = (Offset) other;
		return amount == offset.amount && unit == offset.unit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, unit);
	}

	@Override
	public String toString() {
		return amount + unit.getCode();
	}
}
