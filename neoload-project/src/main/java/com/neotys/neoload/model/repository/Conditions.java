package com.neotys.neoload.model.repository;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface Conditions {
	MatchType getMatchType();
	List<Condition> getConditions();
	Optional<String> getDescription();
	
	enum MatchType{ALL, ANY}
}
