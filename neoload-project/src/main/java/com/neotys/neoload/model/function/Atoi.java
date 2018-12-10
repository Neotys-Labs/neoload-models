package com.neotys.neoload.model.function;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableAtoi.class)
@Deprecated
public interface Atoi extends Function {
	
}
