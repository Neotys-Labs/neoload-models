package com.neotys.models.scenario;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface RampupLoadPolicy extends LoadPolicy {

    int getInitialLoad();
    int getIncrementLoad();
    Optional<Integer> getIncrementTime();
    Optional<Integer> getIncrementIteration();
    Optional<Integer> getMaximumVirtualUsers();
}
