package com.neotys.neoload.model.core;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface Composite<V, T extends Enum<?>> {
	V getValue();
	T getType();
}
