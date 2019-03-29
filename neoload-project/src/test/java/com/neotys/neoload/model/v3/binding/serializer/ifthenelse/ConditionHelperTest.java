package com.neotys.neoload.model.v3.binding.serializer.ifthenelse;

import com.neotys.neoload.model.v3.project.userpath.Condition;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ConditionHelperTest {
	
	@Test
	public void convertToConditionNull() {
		try {
			ConditionHelper.convertToCondition(null);
			fail("The value is not a valid condition.");
		} catch (final IOException e) {
			assertEquals(" is not a valid condition: \r\n" +
					"Position 0 mismatched input '<EOF>' expecting STRING", e.getMessage());
		}
	}

	@Test
	public void convertToConditionEmpty() {
		try {
			ConditionHelper.convertToCondition("");
			fail("The value is not a valid condition.");
		} catch (final IOException e) {
			assertEquals(" is not a valid condition: \r\n" +
					"Position 0 mismatched input '<EOF>' expecting STRING", e.getMessage());
		}
	}

	@Test
	public void convertToConditionInvalid() {
		try {
			ConditionHelper.convertToCondition("xxxxxxxx");
			fail("The value is not a valid condition.");
		} catch (final IOException e) {
			assertEquals("xxxxxxxx is not a valid condition: \r\n" +
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
		assertEquals(getCondition("operand1", Condition.Operator.EXISTS, ""),
				ConditionHelper.convertToCondition("'operand1' exists"));
	}

	@Test
	public void convertToConditionEmptyOperand() throws IOException {
		assertEquals(getCondition("operand1", Condition.Operator.EQUALS, ""),
				ConditionHelper.convertToCondition("'operand1' equals ''"));

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

	private static final Condition getCondition(final String operand1, final Condition.Operator operator, final String operand2){
		return Condition
				.builder()
				.operand1(operand1)
				.operator(operator)
				.operand2(operand2)
				.build();
	}
}
