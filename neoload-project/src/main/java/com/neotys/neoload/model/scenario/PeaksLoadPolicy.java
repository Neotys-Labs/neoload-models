package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface PeaksLoadPolicy extends LoadPolicy {

    enum StartPolicy {
        MINIMUM_LOAD,
        MAXIMUM_LOAD
    }

    int getMinimumLoad();
    Optional<Integer> getMinimumTime();
    Optional<Integer> getMinimumIteration();
    int getMaximumLoad();
    Optional<Integer> getMaximumTime();
    Optional<Integer> getMaximumIteration();
    StartPolicy getStartPolicy();
}
