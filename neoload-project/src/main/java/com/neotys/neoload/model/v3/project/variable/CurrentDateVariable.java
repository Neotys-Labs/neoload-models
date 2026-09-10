package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.DatePatternCheck;
import com.neotys.neoload.model.v3.validation.constraints.OffsetCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import java.util.regex.Matcher;
import org.immutables.value.Value;

// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableCurrentDateVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, CurrentDateVariable.PATTERN, CurrentDateVariable.OFFSET})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CurrentDateVariable extends Variable {

	String PATTERN = "pattern";
	String OFFSET = "offset";

	String DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss";

	// Written only when it differs from its default value.
	@JsonProperty(PATTERN)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultPatternFilter.class)
	@Value.Default
	@DatePatternCheck(groups = {NeoLoad.class})
	default String getPattern() {
		return DEFAULT_PATTERN;
	}

	@JsonProperty(OFFSET)
	@OffsetCheck(groups = {NeoLoad.class})
	Optional<String> getOffset();

	@JsonIgnore
	default Optional<Offset> getParsedOffset() {
		return getOffset().flatMap(Offset::parse);
	}

	final class Offset {

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

	class DefaultPatternFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_PATTERN.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_PATTERN.hashCode();
		}
	}

	class Builder extends ImmutableCurrentDateVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
