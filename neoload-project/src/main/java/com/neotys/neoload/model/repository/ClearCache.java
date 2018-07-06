package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@Value.Immutable
@JsonDeserialize(as = ImmutableClearCache.class)
public interface ClearCache extends Element {
	
}
