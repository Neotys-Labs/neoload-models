package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.immutables.value.Value;

import java.util.Optional;

public interface MonitoringParameters {

	String BEFORE_FIRST = "before";
	String AFTER_ALL = "after";

	@JsonProperty(BEFORE_FIRST)
	@Value.Default
	Optional<Integer> getBeforeFirstVu();


	@JsonProperty(AFTER_ALL)
	@Value.Default
	Optional<Integer> getAfterAllVus();
}
