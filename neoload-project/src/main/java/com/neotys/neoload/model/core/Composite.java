package com.neotys.neoload.model.core;

public interface Composite<V, T extends Enum<?>> {
	V getValue();
	T getType();
}
