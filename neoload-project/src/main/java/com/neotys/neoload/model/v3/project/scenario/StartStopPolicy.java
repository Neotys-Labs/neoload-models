package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.JavaDurationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.JavaDurationSerializer;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
import javax.validation.Valid;

public interface StartStopPolicy {
	String START_AFTER = "start_after";
	String STOP_AFTER = "stop_after";
	
	@Valid
	@JsonProperty(START_AFTER)
	StartAfter getStartAfter();
	@PositiveCheck(unit="second", groups={NeoLoad.class})	
	@WholeSecondsCheck(groups={NeoLoad.class})
	@JsonSerialize(using=JavaDurationSerializer.class)
	@JsonDeserialize(using=JavaDurationDeserializer.class)
	Duration getRampup();
	
	@Valid
	@JsonProperty(STOP_AFTER)
	StopAfter getStopAfter();
}
