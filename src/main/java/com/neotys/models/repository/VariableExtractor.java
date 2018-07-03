package com.neotys.models.repository;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface VariableExtractor {
	enum ExtractType {
        BODY,
        HEADERS
    }
	String getName();
	String getStartExpression();
	String getEndExpression();
	ExtractType getExtractType();
	Optional<Integer> getNbOccur();
	boolean getExitOnError();
}
