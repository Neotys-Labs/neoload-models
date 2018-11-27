package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({ConstantLoadPolicy.USERS, DurationPolicy.DURATION, StartStopPolicy.START_AFTER, ConstantLoadPolicy.RAMPUP, StartStopPolicy.STOP_AFTER})
@JsonDeserialize(as = ImmutableConstantLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ConstantLoadPolicy extends LoadPolicy {
	public static final String USERS = "users";
	public static final String RAMPUP = "rampup";

	@PositiveCheck(unit="user", groups={NeoLoad.class})
	int getUsers();
	
	class Builder extends ImmutableConstantLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
