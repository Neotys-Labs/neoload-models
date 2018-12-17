package com.neotys.neoload.model.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableCounterNumberVariable.class)
@Deprecated
public interface CounterNumberVariable extends Variable{
	
	int getStartValue();
	int getMaxValue();
	int getIncrement();
	
}
