package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface RandomNumberVariable extends Variable{
	int getMinValue();
	int getMaxValue();
}
