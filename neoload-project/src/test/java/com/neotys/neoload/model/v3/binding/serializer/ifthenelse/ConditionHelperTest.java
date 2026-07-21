package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import com.neotys.neoload.model.v3.project.userpath.Condition;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class ConditionHelperTest {
	public final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Test
	public void convertToConditionNull() {
		try {
			ConditionHelper.convertToCondition(null);
			fail("The value is not a valid condition.");
		} catch (final IOException e) {
			assertEquals(" is not a valid condition: " + LINE_SEPARATOR +
					"Position 0 mismatched input '<EOF>' expecting STRING", e.getMessage());
		}
	}

	@Test
	public void convertToConditionEmpty() {
		try {
			ConditionHelper.convertToCondition("");
			fail("The value is not a valid condition.");
		} catch (final IOException e) {
			assertEquals(" is not a valid condition: " + LINE_SEPARATOR +
					"Position 0 mismatched input '<EOF>' expecting STRING", e.getMessage());
		}
	}

	@Test
	public void convertToConditionInvalid() {
		try {
			ConditionHelper.convertToCondition("xxxxxxxx");
			fail("The value is not a valid condition.");
		} catch (final IOException e) {
			assertEquals("xxxxxxxx is not a valid condition: " + LINE_SEPARATOR +
					"Position 8 missing {'equals', '==', 'not_equals', '!=', 'contains', 'not_contains', 'starts_with', 'not_starts_with', 'ends_with', 'not_ends_with', 'match_regexp', 'not_match_regexp', 'greater', '>', 'greater_equal', '>=', 'less', '<', 'less_equal', '<=', 'exists', 'not_exists', 'find_regexp'} at '<EOF>'", e.getMessage());
		}
	}

	@Test
	public void convertToCondition2Operands() throws IOException {
		assertEquals(getCondition("operand1", Condition.Operator.EQUALS, "operand2"),
				ConditionHelper.convertToCondition("'operand1' == 'operand2'"));
	}


	@Test
	public void convertToCondition1Operand() throws IOException {
		assertEquals(getCondition("operand1", Condition.Operator.EXISTS),
				ConditionHelper.convertToCondition("'operand1' exists"));
	}

	@Test
	public void convertToConditionEmptyOperand() throws IOException {
		assertEquals(getCondition("operand1", Condition.Operator.EQUALS, ""),
				ConditionHelper.convertToCondition("'operand1' equals ''"));
	}

	@Test
	public void convertToConditionEmptyOperand2() throws IOException {
		assertEquals(getCondition("", Condition.Operator.EQUALS, "operand1"),
				ConditionHelper.convertToCondition("'' == 'operand1'"));
	}

	@Test
	public void convertToConditionOperandWithSimpleQuote() throws IOException {
		assertEquals(getCondition("operand1", Condition.Operator.EQUALS, "oper'and1"),
				ConditionHelper.convertToCondition("'operand1' equals \"oper'and1\""));
	}

	@Test
	public void convertToConditionOperandWithDoubleQuote() throws IOException {
		assertEquals(getCondition("operand1", Condition.Operator.EQUALS, "oper\"and1"),
				ConditionHelper.convertToCondition("'operand1' equals 'oper\"and1'"));
	}

	@Test
	public void convertToConditionOperandEscapeMixQuote() throws IOException {
		assertEquals(getCondition("ope\"rand1", Condition.Operator.EQUALS, "oper'an\"d1"),
				ConditionHelper.convertToCondition("\"ope\\\"rand1\" equals 'oper\\'an\"d1'"));
	}

	@Test
	public void convertToStringNull() {
		assertNull(ConditionHelper.convertToString(null));
	}

	@Test
	public void convertToString2Operands() {
		assertEquals("operand1 equals operand2",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.EQUALS, "operand2")));
	}

	@Test
	public void convertToString1Operand() {
		assertEquals("operand1 exists",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.EXISTS)));
	}

	@Test
	public void convertToStringUsesWordFormOfOperator() {
		// GREATER accepts both "greater" and ">"; the word form is emitted.
		assertEquals("operand1 greater operand2",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.GREATER, "operand2")));
	}

	@Test
	public void convertToStringVariableOperandStaysBare() {
		assertEquals("${variable} equals 2",
				ConditionHelper.convertToString(getCondition("${variable}", Condition.Operator.EQUALS, "2")));
	}

	@Test
	public void convertToStringEmptyOperandIsQuoted() {
		assertEquals("operand1 equals \"\"",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.EQUALS, "")));
	}

	@Test
	public void convertToStringOperandWithSpaceIsQuoted() {
		assertEquals("operand1 equals \"value with space\"",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.EQUALS, "value with space")));
	}

	@Test
	public void convertToStringReservedWordOperandIsQuoted() {
		// The operand equals an operator keyword, so it must be quoted otherwise the parser would
		// read it as the operator instead of a value.
		assertEquals("${parameter} contains \"contains\"",
				ConditionHelper.convertToString(getCondition("${parameter}", Condition.Operator.CONTAINS, "contains")));
	}

	@Test
	public void convertToStringOperandWithDoubleQuoteUsesSingleQuotes() {
		assertEquals("operand1 equals 'oper\"and1'",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.EQUALS, "oper\"and1")));
	}

	@Test
	public void convertToStringOperandWithSimpleQuoteUsesDoubleQuotes() {
		assertEquals("operand1 equals \"oper'and1\"",
				ConditionHelper.convertToString(getCondition("operand1", Condition.Operator.EQUALS, "oper'and1")));
	}

	@Test
	public void convertToStringThenBackIsIdentity() throws IOException {
		final Condition[] conditions = {
				getCondition("operand1", Condition.Operator.EQUALS, "operand2"),
				getCondition("operand1", Condition.Operator.EXISTS),
				getCondition("${variable}", Condition.Operator.NOT_EQUALS, "5"),
				getCondition("operand1", Condition.Operator.EQUALS, ""),
				getCondition("${parameter}", Condition.Operator.CONTAINS, "contains"),
				getCondition("operand1", Condition.Operator.EQUALS, "value with space"),
				getCondition("operand1", Condition.Operator.EQUALS, "oper\"and1"),
				getCondition("operand1", Condition.Operator.EQUALS, "oper'and1")
		};
		for (final Condition condition : conditions) {
			assertEquals(condition, ConditionHelper.convertToCondition(ConditionHelper.convertToString(condition)));
		}
	}

	private static Condition getCondition(final String operand1, final Condition.Operator operator,
	                                            final String operand2) {
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.operand2(operand2)
				.build();
	}
	private static Condition getCondition(final String operand1, final Condition.Operator operator) {
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.build();
	}
}
