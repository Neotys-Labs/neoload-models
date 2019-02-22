package com.neotys.neoload.model.v3.binding.serializer.sla;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;


public class ThresholdHelperTest {
	
	@Test
	public void convertToThreshold() throws IOException {
		boolean throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold(null);
		}
		catch (final IOException e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append(" is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("Position 0 mismatched input '<EOF>' expecting {");
			for (int i = 0, ilength = KPI.values().length; i < ilength; i++) {
				final KPI kpi = KPI.values()[i];
				expectedMessage.append("'").append(kpi.friendlyName()).append("'");
				if (i != (ilength -1)) {
					expectedMessage.append(", ");
				}
			}
			expectedMessage.append("}");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}
		
		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("");
		}
		catch (final IOException e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append(" is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("Position 0 mismatched input '<EOF>' expecting {");
			for (int i = 0, ilength = KPI.values().length; i < ilength; i++) {
				final KPI kpi = KPI.values()[i];
				expectedMessage.append("'").append(kpi.friendlyName()).append("'");
				if (i != (ilength -1)) {
					expectedMessage.append(", ");
				}
			}
			expectedMessage.append("}");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}
		
		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time");
		}
		catch (final Exception e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append("avg-request-resp-time is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("Position 21 mismatched input '<EOF>' expecting {'(p', ");
			//expectedMessage.append("Position 21 mismatched input '<EOF>' expecting {");
			for (int i = 0, ilength = Severity.values().length; i < ilength; i++) {
				final Severity severity = Severity.values()[i];
				expectedMessage.append("'").append(severity.friendlyName()).append("'");
				if (i != (ilength -1)) {
					expectedMessage.append(", ");
				}
			}
			expectedMessage.append("}");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}

		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn");
		}
		catch (final Exception e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append("avg-request-resp-time warn is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("Position 26 mismatched input '<EOF>' expecting {");
			for (int i = 0, ilength = Operator.values().length; i < ilength; i++) {
				final Operator operator = Operator.values()[i];
				expectedMessage.append("'").append(operator.friendlyName()).append("'");
				if (i != (ilength -1)) {
					expectedMessage.append(", ");
				}
			}
			expectedMessage.append("}");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}

		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn >=");
		}
		catch (final Exception e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append("avg-request-resp-time warn >= is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("Position 29 missing {INTEGER, DOUBLE} at '<EOF>'");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}

		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn >= 1.0 fail >= 2.0 warn");
		}
		catch (final Exception e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append("avg-request-resp-time warn >= 1.0 fail >= 2.0 warn is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("Position 46 extraneous input 'warn' expecting {<EOF>, ");
			for (int i = 0, ilength = Scope.values().length; i < ilength; i++) {
				final Scope scope = Scope.values()[i];
				expectedMessage.append("'").append(scope.friendlyName()).append("'");
				if (i != (ilength -1)) {
					expectedMessage.append(", ");
				}
			}
			expectedMessage.append("}");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}
	
		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn >= 1.0 fail >= 2.0 pertest");
		}
		catch (final Exception e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append("avg-request-resp-time warn >= 1.0 fail >= 2.0 pertest is not a valid threshold: ");
			expectedMessage.append(System.lineSeparator());
			expectedMessage.append("String index out of range: 54");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}

		throwException = false;
		try {
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn >= 1.0 MB fail >= 2.0 Mbps per test");
		}
		catch (final Exception e) {
			final StringBuilder expectedMessage = new StringBuilder();
			expectedMessage.append("avg-request-resp-time does not accept this unit: MB. Possible units: [s, ms].");
			assertEquals(expectedMessage.toString(), e.getMessage());
			throwException = true;
		}
		if (!throwException) {
			fail("The value is not a valid threshold.");
		}

		assertEquals(SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.build()
			, 
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn >= 1"));
		
		assertEquals(SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.GREATER_THAN)
						.value(1.0)
						.build())
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.FAIL)
						.operator(Operator.GREATER_THAN)
						.value(2.0)
						.build())
				.build()
			, 
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn >= 1.0 fail >= 2.0"));

		assertEquals(SlaThreshold.builder()
				.kpi(KPI.AVG_REQUEST_RESP_TIME)
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.WARN)
						.operator(Operator.LESS_THAN)
						.value(1.0)
						.build())
				.addConditions(SlaThresholdCondition.builder()
						.severity(Severity.FAIL)
						.operator(Operator.LESS_THAN)
						.value(2.0)
						.build())
				.scope(Scope.PER_INTERVAL)
				.build()
			, 
			SlaThresholdHelper.convertToThreshold("avg-request-resp-time warn <= 1.0 fail <= 2.0 per interval"));
	}
}
