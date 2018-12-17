package com.neotys.neoload.model.scenario;


import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({PeaksLoadPolicy.MINIMUM, PeaksLoadPolicy.MAXIMUM, PeaksLoadPolicy.START, DurationPolicy.DURATION, StartStopPolicy.START_AFTER, PeaksLoadPolicy.STEP_RAMPUP, StartStopPolicy.STOP_AFTER})
@JsonDeserialize(as = ImmutablePeaksLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
public interface PeaksLoadPolicy extends LoadPolicy {
	public static final String MINIMUM = "minimum";
	public static final String MAXIMUM = "maximum";
	public static final String START = "start";
	public static final String STEP_RAMPUP = "step_rampup";

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
    Peak getStart();
	@JsonProperty(STEP_RAMPUP)
	Integer getRampup();
	
	class Builder extends ImmutablePeaksLoadPolicy.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
