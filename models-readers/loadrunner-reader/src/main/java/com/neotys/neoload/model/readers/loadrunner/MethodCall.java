package com.neotys.neoload.model.readers.loadrunner;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface MethodCall {
    String getName();
    List<String> getParameters();
}
