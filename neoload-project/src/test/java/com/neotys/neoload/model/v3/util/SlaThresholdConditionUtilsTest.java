package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;

public class SlaThresholdConditionUtilsTest {
	@Test
	public void toSeverity() {
		assertEquals("HIGH", SlaThresholdConditionUtils.toSeverity(Severity.FAIL));
		assertEquals("LOW", SlaThresholdConditionUtils.toSeverity(Severity.WARN));
	}
	
	@Test
	public void toOperator() {
		assertEquals("LESS_THAN", SlaThresholdConditionUtils.toOperator(Operator.LESS_THAN));
		assertEquals("GREATER_THAN", SlaThresholdConditionUtils.toOperator(Operator.GREATER_THAN));
		assertEquals("EQUALS", SlaThresholdConditionUtils.toOperator(Operator.EQUALS));
	}
}