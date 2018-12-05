package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.v3.binding.converter.TimeDurationToStringConverter;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

import javax.validation.Valid;

public interface StartStopPolicy {
	String START_AFTER = "start_after";
	String STOP_AFTER = "stop_after";
	
	@Valid
	@JsonProperty(START_AFTER)
	StartAfter getStartAfter();
	@PositiveCheck(unit="second", groups={NeoLoad.class})	
	@JsonSerialize(converter=TimeDurationToStringConverter.class)
	@JsonDeserialize(converter=StringToTimeDurationConverter.class)
	Integer getRampup();
	
	@Valid
	@JsonProperty(STOP_AFTER)
	StopAfter getStopAfter();
}
