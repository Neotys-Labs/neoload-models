package com.neotys.neoload.model.v3.project.userpath;

import java.util.Optional;

import javax.validation.constraints.Pattern;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.UserPathThinkTimeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * Think time policy of a User Path, mirroring the "Waiting time" settings of the designer's User Path
 * "Runtime parameters" panel: either {@code override} the think times of the pages and delays with a
 * duration, or scale them by a {@code factor}, optionally adding a {@code random} delay of +/- a percentage.
 * Without {@code override} nor {@code factor}, the think times defined on pages and delays apply.
 * <p>
 * Values are kept as written: a duration in the {@code think_time} step format, a non-negative integer
 * percentage with an optional {@code %}, or a variable.
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

	String DURATION_PATTERN = "(\\d+|(?=\\d)(\\d+h\\s*)?(\\d+m\\s*)?(\\d+s\\s*)?(\\d+ms\\s*)?|\\$\\{[^}]+\\})";
	String PERCENTAGE_PATTERN = "(\\d+%?|\\$\\{[^}]+\\})";

	String DURATION_MESSAGE = "{com.neotys.neoload.model.v3.validation.constraints.ThinkTimeDurationPattern.message}";
	String PERCENTAGE_MESSAGE = "{com.neotys.neoload.model.v3.validation.constraints.ThinkTimePercentagePattern.message}";

	@JsonProperty(OVERRIDE)
	Optional<@Pattern(regexp = DURATION_PATTERN, message = DURATION_MESSAGE, groups = {NeoLoad.class}) String> getOverride();

	@JsonProperty(FACTOR)
	Optional<@Pattern(regexp = PERCENTAGE_PATTERN, message = PERCENTAGE_MESSAGE, groups = {NeoLoad.class}) String> getFactor();

	@JsonProperty(RANDOM)
	Optional<@Pattern(regexp = PERCENTAGE_PATTERN, message = PERCENTAGE_MESSAGE, groups = {NeoLoad.class}) String> getRandom();

	class Builder extends ImmutableUserPathThinkTime.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
