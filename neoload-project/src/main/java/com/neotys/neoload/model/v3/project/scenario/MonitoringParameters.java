package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

@JsonSerialize(as = ImmutableMonitoringParameters.class)
@JsonDeserialize(as = ImmutableMonitoringParameters.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface MonitoringParameters {

	String BEFORE_FIRST = "before";
	String AFTER_LAST = "after";

	@JsonProperty(BEFORE_FIRST)
	Optional<Integer> getBeforeFirstVu();


	@JsonProperty(AFTER_LAST)
	Optional<Integer> getAfterLastVus();
}
