package com.neotys.neoload.model.scenario;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.converter.TimeDurationToStringConverter;
import com.neotys.neoload.model.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface StartStopPolicy {
	public static final String START_AFTER = "start_after";
	public static final String STOP_AFTER = "stop_after";
	
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
