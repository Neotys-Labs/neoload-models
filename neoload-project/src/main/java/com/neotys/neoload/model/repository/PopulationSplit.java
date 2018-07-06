package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface PopulationSplit {
    String getUserPath();
    int getPercentage();
}
