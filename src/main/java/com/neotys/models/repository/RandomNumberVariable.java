package com.neotys.models.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface RandomNumberVariable extends Variable{
	int getMinValue();
	int getMaxValue();
}
