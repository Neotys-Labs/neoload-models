package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.v3.binding.converter.TimeDurationToStringConverter;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface MonitoringParameters {

	String BEFORE_FIRST = "before";
	String AFTER_LAST = "after";

	@JsonProperty(BEFORE_FIRST)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter= TimeDurationToStringConverter.class)
	@JsonDeserialize(converter= StringToTimeDurationConverter.class)
	Optional<Integer> getBeforeFirstVu();


	@JsonProperty(AFTER_LAST)
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	@JsonSerialize(converter=TimeDurationToStringConverter.class)
	@JsonDeserialize(converter=StringToTimeDurationConverter.class)
	Optional<Integer> getAfterLastVus();
}
