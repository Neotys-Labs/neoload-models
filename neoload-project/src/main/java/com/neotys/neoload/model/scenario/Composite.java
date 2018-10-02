package com.neotys.neoload.model.scenario;

public interface Composite<V, T extends Enum<?>> {
	V getValue();
	T getType();
}
