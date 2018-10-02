package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(value=Include.NON_ABSENT)
@JsonDeserialize(as = ImmutablePeakLoadPolicy.class)
@Value.Immutable
public interface PeakLoadPolicy {
    int getUsers();
    Duration getDuration();
}
