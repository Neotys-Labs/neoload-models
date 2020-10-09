package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.CustomStepDurationCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import javax.validation.constraints.PositiveOrZero;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({CustomPolicyStep.WHEN, CustomPolicyStep.USERS})
@JsonSerialize(as = ImmutableCustomPolicyStep.class)
@JsonDeserialize(as = ImmutableCustomPolicyStep.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@Gson.TypeAdapters
public interface CustomPolicyStep {

    String WHEN = "when";
    String USERS = "users";

    @CustomStepDurationCheck(groups={NeoLoad.class})
    @RequiredCheck(groups={NeoLoad.class})
    @JsonProperty(WHEN)
    LoadDuration getWhen();

    @RequiredCheck(groups={NeoLoad.class})
    @PositiveOrZero(groups = {NeoLoad.class})
    @JsonProperty(USERS)
    Integer getUsers();

    class Builder extends ImmutableCustomPolicyStep.Builder {}
    static ImmutableCustomPolicyStep.Builder builder() { return new CustomPolicyStep.Builder(); }
}
