package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface ConstantVariable extends Variable {
	String getConstantValue();
}
