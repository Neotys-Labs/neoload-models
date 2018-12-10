package com.neotys.neoload.model.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableCounterNumberVariable.class)
@Deprecated
public interface CounterNumberVariable extends Variable{
	
	int getStartValue();
	int getMaxValue();
	int getIncrement();
	
}
