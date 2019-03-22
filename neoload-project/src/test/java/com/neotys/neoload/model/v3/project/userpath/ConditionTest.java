package com.neotys.neoload.model.v3.project.userpath;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ConditionTest {
	@Test
	public void testOperatorConstants() {
		assertEquals(Condition.Operator.EQUALS, Condition.Operator.of("equals"));
		assertEquals(Condition.Operator.EQUALS, Condition.Operator.of("=="));
		assertEquals(Condition.Operator.NOT_EQUALS, Condition.Operator.of("not_equals"));
		assertEquals(Condition.Operator.NOT_EQUALS, Condition.Operator.of("!="));
		assertEquals(Condition.Operator.CONTAINS, Condition.Operator.of("contains"));
		assertEquals(Condition.Operator.NOT_CONTAINS, Condition.Operator.of("not_contains"));
		assertEquals(Condition.Operator.STARTS_WITH, Condition.Operator.of("starts_with"));
		assertEquals(Condition.Operator.NOT_STARTS_WITH, Condition.Operator.of("not_starts_with"));
		assertEquals(Condition.Operator.ENDS_WITH, Condition.Operator.of("ends_with"));
		assertEquals(Condition.Operator.NOT_ENDS_WITH, Condition.Operator.of("not_ends_with"));
		assertEquals(Condition.Operator.MATCH_REGEXP, Condition.Operator.of("match_regexp"));
		assertEquals(Condition.Operator.NOT_MATCH_REGEXP, Condition.Operator.of("not_match_regexp"));
		assertEquals(Condition.Operator.GREATER, Condition.Operator.of("greater"));
		assertEquals(Condition.Operator.GREATER, Condition.Operator.of(">"));
		assertEquals(Condition.Operator.GREATER_EQUAL, Condition.Operator.of("greater_equal"));
		assertEquals(Condition.Operator.GREATER_EQUAL, Condition.Operator.of(">="));
		assertEquals(Condition.Operator.LESS, Condition.Operator.of("less"));
		assertEquals(Condition.Operator.LESS, Condition.Operator.of("<"));
		assertEquals(Condition.Operator.LESS_EQUAL, Condition.Operator.of("less_equal"));
		assertEquals(Condition.Operator.LESS_EQUAL, Condition.Operator.of("<="));
		assertEquals(Condition.Operator.EXISTS, Condition.Operator.of("exists"));
		assertEquals(Condition.Operator.NOT_EXISTS, Condition.Operator.of("not_exists"));
	}

	@Test
	public void testExceptionMessage() {
		final String operatorMustBe = "The operator must be: [==, <=, not_match_regexp, less, starts_with, greater_equal, not_equals, not_exists, contains, ends_with, not_contains, not_ends_with, equals, match_regexp, exists, !=, <, find_regexp, greater, >, less_equal, not_starts_with, >=].";
		final String operatorNotNullOrEmpty = "The operator must not be null or empty.";

		try{
			Condition.Operator.of("unexisting operator");
			fail();
		} catch(final IllegalArgumentException iae){
			assertEquals(operatorMustBe, iae.getMessage());
		}
		try{
			Condition.Operator.of("");
			fail();
		} catch(final IllegalArgumentException iae){
			assertEquals(operatorNotNullOrEmpty, iae.getMessage());
		}
		try{
			Condition.Operator.of(null);
			fail();
		} catch(final IllegalArgumentException iae){
			assertEquals(operatorNotNullOrEmpty, iae.getMessage());
		}
	}
}
