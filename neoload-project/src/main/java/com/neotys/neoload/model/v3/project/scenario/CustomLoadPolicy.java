package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsOrderedCheck;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsSameDurationTypeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.List;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({StartStopPolicy.START_AFTER, CustomLoadPolicy.RAMPUP, StartStopPolicy.STOP_AFTER, CustomLoadPolicy.STEPS})
@JsonSerialize(as = ImmutableCustomLoadPolicy.class)
@JsonDeserialize(as = ImmutableCustomLoadPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@Gson.TypeAdapters
public interface CustomLoadPolicy extends LoadPolicy {
    String STEPS = "steps";
    String RAMPUP = "rampup";

    @CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck(groups={NeoLoad.class})
    @CustomPolicyStepsSameDurationTypeCheck(groups={NeoLoad.class})
    @CustomPolicyStepsOrderedCheck(groups={NeoLoad.class})
    @RequiredCheck(groups={NeoLoad.class})
    @Valid
    @JsonProperty(STEPS)
    List<CustomPolicyStep> getSteps();

    @JsonProperty(RAMPUP)
    Integer getRampup();

    @JsonIgnore
    @Value.Derived
    @Nullable
    default LoadDuration getDuration() {
        if(getSteps()== null || getSteps().isEmpty()){
            return null;
        }
        return getSteps().get(getSteps().size()-1).getWhen();
    }

    class Builder extends ImmutableCustomLoadPolicy.Builder {}
    static Builder builder() { return new Builder(); }
}
