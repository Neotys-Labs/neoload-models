package com.neotys.neoload.model.v3.project.userpath.assertion;

import java.util.Optional;

import javax.validation.constraints.PositiveOrZero;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.ValidSizeAssertionCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * Response size constraint in bytes, mirroring the designer's "Content length" panel:
 * either an exact size ({@code equals}), or a range built from {@code greaterThan} and/or
 * {@code lessThan}. Bounds are mutually exclusive with the exact size.
 */
@ValidSizeAssertionCheck(groups = {NeoLoad.class})
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({SizeAssertion.EQUALS, SizeAssertion.GREATER_THAN, SizeAssertion.LESS_THAN})
@JsonSerialize(as = ImmutableSizeAssertion.class)
@JsonDeserialize(as = ImmutableSizeAssertion.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface SizeAssertion {
	String EQUALS = "equals";
	String GREATER_THAN = "greater_than";
	String LESS_THAN = "less_than";

	@JsonProperty(EQUALS)
	Optional<@PositiveOrZero(groups = {NeoLoad.class}) Long> getEquals();

	@JsonProperty(GREATER_THAN)
	Optional<@PositiveOrZero(groups = {NeoLoad.class}) Long> getGreaterThan();

	@JsonProperty(LESS_THAN)
	Optional<@PositiveOrZero(groups = {NeoLoad.class}) Long> getLessThan();

	class Builder extends ImmutableSizeAssertion.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
