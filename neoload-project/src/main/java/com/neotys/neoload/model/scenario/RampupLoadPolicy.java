package com.neotys.neoload.model.scenario;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({RampupLoadPolicy.MIN_USERS, RampupLoadPolicy.MAX_USERS, RampupLoadPolicy.INCREMENT_USERS, RampupLoadPolicy.INCREMENT_EVERY, DurationPolicy.DURATION, StartStopPolicy.START_AFTER, RampupLoadPolicy.INCREMENT_RAMPUP, StartStopPolicy.STOP_AFTER})
@JsonDeserialize(as = ImmutableRampupLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
public interface RampupLoadPolicy extends LoadPolicy {
	public static final String MIN_USERS = "min_users";
	public static final String MAX_USERS = "max_users";
	public static final String INCREMENT_USERS = "increment_users";
	public static final String INCREMENT_EVERY = "increment_every";
	public static final String INCREMENT_RAMPUP = "increment_rampup";

	@PositiveCheck(unit="user", groups={NeoLoad.class})
	@JsonProperty(MIN_USERS)
	int getMinUsers();
	@PositiveCheck(unit="user", groups={NeoLoad.class})
	@JsonProperty(MAX_USERS)
	Integer getMaxUsers();
	@PositiveCheck(unit="user", groups={NeoLoad.class})
	@JsonProperty(INCREMENT_USERS)
	int getIncrementUsers();
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	@JsonProperty(INCREMENT_EVERY)
	Duration getIncrementEvery();
	@JsonProperty(INCREMENT_RAMPUP)
	Integer getRampup();
	
	class Builder extends ImmutableRampupLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
