package com.neotys.neoload.model.v3.project.scenario;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({PeaksLoadPolicy.MINIMUM, PeaksLoadPolicy.MAXIMUM, PeaksLoadPolicy.START, DurationPolicy.DURATION, StartStopPolicy.START_AFTER, PeaksLoadPolicy.STEP_RAMPUP, StartStopPolicy.STOP_AFTER})
@JsonSerialize(as = ImmutablePeaksLoadPolicy.class)
@JsonDeserialize(as = ImmutablePeaksLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Gson.TypeAdapters
public interface PeaksLoadPolicy extends LoadPolicy {
	String MINIMUM = "minimum";
	String MAXIMUM = "maximum";
	String START = "start";
	String STEP_RAMPUP = "step_rampup";

	enum Peak {
		@JsonProperty(PeaksLoadPolicy.MINIMUM)
        MINIMUM,
        @JsonProperty(PeaksLoadPolicy.MAXIMUM)
        MAXIMUM
    }

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
    PeakLoadPolicy getMinimum();
	@RequiredCheck(groups={NeoLoad.class})
    @Valid
    PeakLoadPolicy getMaximum();
	@RequiredCheck(groups={NeoLoad.class})
	@JsonProperty(START)
	Peak getStart();
	@JsonProperty(STEP_RAMPUP)
	Integer getRampup();
	
	class Builder extends ImmutablePeaksLoadPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
