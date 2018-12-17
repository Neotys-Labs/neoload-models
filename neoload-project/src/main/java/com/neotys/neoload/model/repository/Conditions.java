package com.neotys.neoload.model.repository;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface Conditions {
	MatchType getMatchType();
	List<Condition> getConditions();
	Optional<String> getDescription();
	
	enum MatchType{ALL, ANY}
}
