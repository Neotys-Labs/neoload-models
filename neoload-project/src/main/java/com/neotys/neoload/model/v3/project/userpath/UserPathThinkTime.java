package com.neotys.neoload.model.v3.project.userpath;

import java.util.Optional;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.NonNegativePercentageCheck;
import com.neotys.neoload.model.v3.validation.constraints.PositivePercentageCheck;
import com.neotys.neoload.model.v3.validation.constraints.UserPathThinkTimeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * Think time policy of a User Path, mirroring the "Waiting time" settings of the designer's User Path
 * "Runtime parameters" panel: either {@code override} the think times of the pages and delays with a
 * duration, or scale them by a {@code factor}, optionally adding a {@code random} delay of +/- a percentage.
 * Without {@code override} nor {@code factor}, the think times defined on pages and delays apply, and without
 * {@code random} no random delay is added.
 * <p>
 * {@code override} is a constant {@link Duration}, in milliseconds or a variable. {@code factor} and
 * {@code random} are a {@link PercentageOrVariable}: an integer percentage with an optional {@code %}, or a variable.
 * {@code factor} is at least 0 and {@code random} at least 1.
 */
@UserPathThinkTimeCheck(groups = {NeoLoad.class})
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({UserPathThinkTime.OVERRIDE, UserPathThinkTime.FACTOR, UserPathThinkTime.RANDOM})
@JsonSerialize(as = ImmutableUserPathThinkTime.class)
@JsonDeserialize(as = ImmutableUserPathThinkTime.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UserPathThinkTime {
	String OVERRIDE = "override";
	String FACTOR = "factor";
	String RANDOM = "random";

	@JsonProperty(OVERRIDE)
	@Valid
	Optional<DurationConstant> getOverride();

	@JsonProperty(FACTOR)
	Optional<@NonNegativePercentageCheck(groups = {NeoLoad.class}) PercentageOrVariable> getFactor();

	@JsonProperty(RANDOM)
	Optional<@PositivePercentageCheck(groups = {NeoLoad.class}) PercentageOrVariable> getRandom();

	class Builder extends ImmutableUserPathThinkTime.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
