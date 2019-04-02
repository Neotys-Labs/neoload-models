package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import com.neotys.neoload.model.v3.project.userpath.Condition;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


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
					"Position 8 mismatched input '<EOF>' expecting STRING", e.getMessage());
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

	private static final Condition getCondition(final String operand1, final Condition.Operator operator,
	                                            final String operand2) {
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.operand2(operand2)
				.build();
	}
	private static final Condition getCondition(final String operand1, final Condition.Operator operator) {
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.build();
	}
}
