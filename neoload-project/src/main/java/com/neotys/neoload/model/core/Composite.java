package com.neotys.neoload.model.core;

@Deprecated
public interface Composite<V, T extends Enum<?>> {
	V getValue();
	T getType();
}
