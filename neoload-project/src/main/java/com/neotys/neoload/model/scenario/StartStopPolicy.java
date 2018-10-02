package com.neotys.neoload.model.scenario;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.StringToTimeDurationConverter;
import com.neotys.neoload.model.converter.TimeDurationToStringConverter;

public interface StartStopPolicy {
	@JsonProperty("start_after")
	Optional<StartAfter> getStartAfter();
	@JsonSerialize(converter=TimeDurationToStringConverter.class)
	@JsonDeserialize(converter=StringToTimeDurationConverter.class)
	Optional<Integer> getRampup();
	
	@JsonProperty("stop_after")
	Optional<StopAfter> getStopAfter();
}
