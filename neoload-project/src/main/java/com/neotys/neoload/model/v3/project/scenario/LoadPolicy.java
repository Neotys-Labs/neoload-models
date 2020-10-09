package com.neotys.neoload.model.v3.project.scenario;

import org.immutables.gson.Gson;

@Gson.ExpectedSubtypes({CustomLoadPolicy.class, ConstantLoadPolicy.class, PeaksLoadPolicy.class, RampupLoadPolicy.class})
public interface LoadPolicy extends DurationPolicy, StartStopPolicy {
}
