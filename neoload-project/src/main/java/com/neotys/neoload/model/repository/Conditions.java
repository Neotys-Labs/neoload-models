package com.neotys.neoload.model.repository;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Conditions {
	MatchType getMatchType();
	List<Condition> getConditions();
	
	enum MatchType{ALL, ANY}
}
