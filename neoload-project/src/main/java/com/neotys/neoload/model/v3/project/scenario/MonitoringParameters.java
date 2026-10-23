package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.SecondsDurationCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({MonitoringParameters.BEFORE_FIRST, MonitoringParameters.AFTER_LAST})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@JsonSerialize(as = ImmutableMonitoringParameters.class)
@JsonDeserialize(as = ImmutableMonitoringParameters.class)
@Gson.TypeAdapters
public interface MonitoringParameters {

	String BEFORE_FIRST = "before";
	String AFTER_LAST = "after";

	@JsonProperty(BEFORE_FIRST)
	@SecondsDurationCheck(groups={NeoLoad.class})
	SecondsDuration getBeforeFirstVu();


	@JsonProperty(AFTER_LAST)
	@SecondsDurationCheck(groups={NeoLoad.class})
	SecondsDuration getAfterLastVus();


	static ImmutableMonitoringParameters.Builder builder() {
		return ImmutableMonitoringParameters.builder();
	}
}
