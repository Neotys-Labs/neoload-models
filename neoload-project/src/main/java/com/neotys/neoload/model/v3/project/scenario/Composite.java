package com.neotys.neoload.model.v3.project.scenario;

public interface Composite<V, T extends Enum<?>> {
	V getValue();
	T getType();
}
