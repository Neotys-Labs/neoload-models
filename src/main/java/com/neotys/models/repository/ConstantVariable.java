package com.neotys.models.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface ConstantVariable extends Variable {
	String getConstantValue();
}
