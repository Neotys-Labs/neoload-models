package com.neotys.neoload.model.scenario;

@Deprecated
public interface Composite<V, T extends Enum<?>> {
	V getValue();
	T getType();
}
