package com.neotys.neoload.model.scenario;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(value=Include.NON_ABSENT)
@JsonPropertyOrder({"min_users", "max_users", "increment_users", "increment_every", "duration", "start_after", "increment_rampup", "stop_after"})
@JsonDeserialize(as = ImmutableRampupLoadPolicy.class)
@Value.Immutable
public interface RampupLoadPolicy extends LoadPolicy {
	@JsonProperty("min_users")
	int getMinUsers();
	@JsonProperty("max_users")
	Optional<Integer> getMaxUsers();
	@JsonProperty("increment_users")
	int getIncrementUsers();
	@JsonProperty("increment_every")
	Duration getIncrementEvery();
	@JsonProperty("increment_rampup")
	Optional<Integer> getRampup();
}
