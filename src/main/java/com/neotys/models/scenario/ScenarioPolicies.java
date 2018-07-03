package com.neotys.models.scenario;

import org.immutables.value.Value;

@Value.Immutable
public interface ScenarioPolicies {

    DurationPolicy getDurationPolicy();
    LoadPolicy getLoadPolicy();

}
