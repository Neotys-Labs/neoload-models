package com.neotys.neoload.model.function;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableStrcmp.class)
public interface Strcmp extends Function {
	
}
