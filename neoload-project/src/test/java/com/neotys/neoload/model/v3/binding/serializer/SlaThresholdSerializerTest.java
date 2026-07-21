package com.neotys.neoload.model.v3.binding.serializer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;


public class SlaThresholdSerializerTest {

	@Test
	public void serializeSingleCondition() throws IOException {
		assertEquals("\"avg-request-resp-time warn >= 1 per test\"",
				serialize(SlaThreshold.builder()
						.kpi(KPI.AVG_REQUEST_RESP_TIME)
						.addConditions(condition(Severity.WARN, Operator.GREATER_THAN, 1.0))
						.build()));
	}

	@Test
	public void serializePercentileTwoConditionsAndInterval() throws IOException {
		assertEquals("\"perc-transaction-resp-time (p50) warn == 0.15 fail == 1.25 per interval\"",
				serialize(SlaThreshold.builder()
						.kpi(KPI.PERC_TRANSACTION_RESP_TIME)
						.percent(50)
						.addConditions(condition(Severity.WARN, Operator.EQUALS, 0.15))
						.addConditions(condition(Severity.FAIL, Operator.EQUALS, 1.25))
						.scope(Scope.PER_INTERVAL)
						.build()));
	}

	private static SlaThresholdCondition condition(final Severity severity, final Operator operator, final double value) {
		return SlaThresholdCondition.builder().severity(severity).operator(operator).value(value).build();
	}

	private static String serialize(final SlaThreshold threshold) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final StringWriter writer = new StringWriter();
		final JsonGenerator generator = mapper.getFactory().createGenerator(writer);
		new SlaThresholdSerializer().serialize(threshold, generator, mapper.getSerializerProviderInstance());
		generator.flush();
		return writer.toString();
	}
}
