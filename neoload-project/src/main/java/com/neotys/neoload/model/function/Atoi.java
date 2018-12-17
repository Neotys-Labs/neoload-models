package com.neotys.neoload.model.function;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableAtoi.class)
@Deprecated
public interface Atoi extends Function {
	
}
