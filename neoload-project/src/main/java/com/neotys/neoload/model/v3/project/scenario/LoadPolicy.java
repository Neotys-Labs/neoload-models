package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.immutables.gson.Gson;

@Gson.ExpectedSubtypes({CustomLoadPolicy.class, ConstantLoadPolicy.class, PeaksLoadPolicy.class, RampupLoadPolicy.class})
public interface LoadPolicy extends DurationPolicy, StartStopPolicy {
    @JsonIgnore
    LoadPolicyType getType();
}
