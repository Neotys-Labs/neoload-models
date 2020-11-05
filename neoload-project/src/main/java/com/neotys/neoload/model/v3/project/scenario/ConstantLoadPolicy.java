package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({ConstantLoadPolicy.USERS, DurationPolicy.DURATION, StartStopPolicy.START_AFTER, ConstantLoadPolicy.RAMPUP, StartStopPolicy.STOP_AFTER})
@JsonSerialize(as = ImmutableConstantLoadPolicy.class)
@JsonDeserialize(as = ImmutableConstantLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Gson.TypeAdapters
public interface ConstantLoadPolicy extends LoadPolicy {
	String USERS = "users";
	String RAMPUP = "rampup";

	@PositiveCheck(unit="user", groups={NeoLoad.class})
	@JsonProperty(USERS)
	int getUsers();

	@JsonProperty(RAMPUP)
	Integer getRampup();

    @Value.Check
    default void check() {
        Preconditions.checkState(getType() == LoadPolicyType.CONSTANT);
    }

    @Override
	@Value.Default
    default LoadPolicyType getType() {
        return LoadPolicyType.CONSTANT;
    }

	class Builder extends ImmutableConstantLoadPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
