package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({RampupLoadPolicy.MIN_USERS, RampupLoadPolicy.MAX_USERS, RampupLoadPolicy.INCREMENT_USERS, RampupLoadPolicy.INCREMENT_EVERY, DurationPolicy.DURATION, StartStopPolicy.START_AFTER, RampupLoadPolicy.INCREMENT_RAMPUP, StartStopPolicy.STOP_AFTER})
@JsonSerialize(as = ImmutableRampupLoadPolicy.class)
@JsonDeserialize(as = ImmutableRampupLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Gson.TypeAdapters
public interface RampupLoadPolicy extends LoadPolicy {
	String MIN_USERS = "min_users";
	String MAX_USERS = "max_users";
	String INCREMENT_USERS = "increment_users";
	String INCREMENT_EVERY = "increment_every";
	String INCREMENT_RAMPUP = "increment_rampup";

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
	LoadDuration getIncrementEvery();
	@JsonProperty(INCREMENT_RAMPUP)
	Integer getRampup();

	@Override
	@Value.Derived
	default LoadPolicyType getType() {
		return LoadPolicyType.RAMPUP;
	}

    @Value.Check
    default void check() {
        Preconditions.checkState(getType() == LoadPolicyType.RAMPUP);
    }

	class Builder extends ImmutableRampupLoadPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
