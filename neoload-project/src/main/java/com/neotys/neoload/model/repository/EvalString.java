package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.function.Function;

@Value.Immutable
@JsonDeserialize(as = ImmutableEvalString.class)
@Deprecated
public interface EvalString extends Function {	
}
