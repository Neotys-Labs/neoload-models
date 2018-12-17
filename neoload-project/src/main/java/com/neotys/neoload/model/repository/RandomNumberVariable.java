package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface RandomNumberVariable extends Variable{
	int getMinValue();
	int getMaxValue();
}
