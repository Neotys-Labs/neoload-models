package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface RandomNumberVariable extends Variable{
	int getMinValue();
	int getMaxValue();
}
