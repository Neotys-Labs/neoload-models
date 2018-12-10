package com.neotys.neoload.model.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableDelay.class)
@Deprecated
public interface Delay extends Element {
    String getDelay();
    boolean isThinkTime();
}
