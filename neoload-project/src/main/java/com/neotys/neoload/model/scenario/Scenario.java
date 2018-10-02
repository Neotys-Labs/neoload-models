package com.neotys.neoload.model.scenario;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@JsonInclude(value=Include.NON_ABSENT)
@JsonPropertyOrder({"name", "description", "sla_profile", "populations"})
@JsonDeserialize(as = ImmutableScenario.class)
@Value.Immutable
public interface Scenario extends Element {
	@JsonProperty("sla_profile")
	Optional<String> getSlaProfile();
	List<PopulationPolicy> getPopulations();
}
