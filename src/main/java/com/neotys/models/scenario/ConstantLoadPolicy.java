package com.neotys.models.scenario;

import org.immutables.value.Value;

@Value.Immutable
public interface ConstantLoadPolicy extends LoadPolicy {
    int getLoad();
}
