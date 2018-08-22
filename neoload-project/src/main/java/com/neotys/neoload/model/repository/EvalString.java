package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableEvalString.class)
public interface EvalString extends Element {
	String getContent();
}
