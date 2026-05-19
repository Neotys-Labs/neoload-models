package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Comparison operator for {@link SizeAssertion}.
 * Mirrors the designer's {@code SizeAssertion} comparator constants (EQUAL=1, GREATERTHAN=3, LESSTHAN=4).
 */
public enum SizeOperator {
	@JsonProperty(SizeOperator.EQUALS_VALUE)
	EQUALS,
	@JsonProperty(SizeOperator.LESS_THAN_VALUE)
	LESS_THAN,
	@JsonProperty(SizeOperator.GREATER_THAN_VALUE)
	GREATER_THAN;

	public static final String EQUALS_VALUE = "equals";
	public static final String LESS_THAN_VALUE = "less_than";
	public static final String GREATER_THAN_VALUE = "greater_than";
}