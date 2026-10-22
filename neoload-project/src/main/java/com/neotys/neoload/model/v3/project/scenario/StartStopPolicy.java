package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.constraints.SecondsDurationCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.Valid;

public interface StartStopPolicy {
	String START_AFTER = "start_after";
	String STOP_AFTER = "stop_after";
	
	@Valid
	@JsonProperty(START_AFTER)
	StartAfter getStartAfter();
	@SecondsDurationCheck(groups={NeoLoad.class})
	@PositiveCheck(unit="second", groups={NeoLoad.class})
	SecondsDuration getRampup();
	
	@Valid
	@JsonProperty(STOP_AFTER)
	StopAfter getStopAfter();
}
