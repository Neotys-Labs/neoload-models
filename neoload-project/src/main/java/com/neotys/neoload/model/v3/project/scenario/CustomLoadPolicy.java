package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.CustomLoadPolicyCheck;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsOrderedCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.Valid;
import java.util.List;

@CustomLoadPolicyCheck(groups={NeoLoad.class})
@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({DurationPolicy.DURATION, StartStopPolicy.START_AFTER, CustomLoadPolicy.RAMPUP, StartStopPolicy.STOP_AFTER, CustomLoadPolicy.STEPS})
@JsonSerialize(as = ImmutableCustomLoadPolicy.class)
@JsonDeserialize(as = ImmutableCustomLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CustomLoadPolicy extends LoadPolicy {
    String STEPS = "steps";
    String RAMPUP = "rampup";

    @CustomPolicyStepsOrderedCheck(groups={NeoLoad.class})
    @RequiredCheck(groups={NeoLoad.class})
    @Valid
    @JsonProperty(STEPS)
    List<CustomPolicyStep> getSteps();

    @JsonProperty(RAMPUP)
    Integer getRampup();

    class Builder extends ImmutableCustomLoadPolicy.Builder {}
    static Builder builder() { return new Builder(); }
}
