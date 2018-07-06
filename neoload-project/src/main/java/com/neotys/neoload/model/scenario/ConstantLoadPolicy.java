package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;

@Value.Immutable
public interface ConstantLoadPolicy extends LoadPolicy {
    int getLoad();
}
