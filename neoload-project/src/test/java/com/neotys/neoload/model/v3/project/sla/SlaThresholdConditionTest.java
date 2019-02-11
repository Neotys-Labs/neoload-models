package com.neotys.neoload.model.v3.project.sla;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;


public class SlaThresholdConditionTest {
	@Test
	public void severityOf() {
		boolean throwException = false;
		try {
			Severity.of(null);
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}
		
		throwException = false;
		try {
			Severity.of("");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}

		throwException = false;
		try {
			Severity.of("info");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must be: 'warn' or 'fail'."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must be: 'warn' or 'fail'.");
		}

		assertEquals(Severity.WARN, Severity.of("warn"));
		assertEquals(Severity.WARN, Severity.of("WARN"));
		
		assertEquals(Severity.FAIL, Severity.of("fail"));
		assertEquals(Severity.FAIL, Severity.of("FAIL"));
	}

	@Test
	public void operatorOf() {
		boolean throwException = false;
		try {
			Operator.of(null);
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}
		
		throwException = false;
		try {
			Operator.of("");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must not be null or empty.");
		}
		
		throwException = false;
		try {
			Operator.of("<>");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must be: '<=', '>=' or '=='."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must be: '<=', '>=' or '=='.");
		}

		assertEquals(Operator.LESS_THAN, Operator.of("<="));
		assertEquals(Operator.GREATER_THAN, Operator.of(">="));
		assertEquals(Operator.EQUALS, Operator.of("=="));
		
	}
}
