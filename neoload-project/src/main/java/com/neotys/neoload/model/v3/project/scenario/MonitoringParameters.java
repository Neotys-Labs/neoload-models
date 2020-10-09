package com.neotys.neoload.model.v3.project.scenario;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.v3.binding.converter.TimeDurationToStringConverter;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

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
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter= TimeDurationToStringConverter.class)
	@JsonDeserialize(converter= StringToTimeDurationConverter.class)
	Integer getBeforeFirstVu();


	@JsonProperty(AFTER_LAST)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter=TimeDurationToStringConverter.class)
	@JsonDeserialize(converter=StringToTimeDurationConverter.class)
	Integer getAfterLastVus();


	static ImmutableMonitoringParameters.Builder builder() {
		return ImmutableMonitoringParameters.builder();
	}
}
