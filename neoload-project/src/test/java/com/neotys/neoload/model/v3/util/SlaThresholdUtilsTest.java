package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;

public class SlaThresholdUtilsTest {
	@Test
	public void toIdentifier() {
		assertEquals("AVERAGE_REQUEST_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KPI.AVG_REQUEST_RESP_TIME));
		assertEquals("AVERAGE_PAGE_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KPI.AVG_PAGE_RESP_TIME));
		assertEquals("AVERAGE_CONTAINER_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KPI.AVG_TRANSACTION_RESP_TIME));
		assertEquals("PERCENTILE_CONTAINER_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KPI.PERC_TRANSACTION_RESP_TIME));
		assertEquals("AVERAGE_HITS_PER_SECOND", SlaThresholdUtils.toIdentifier(KPI.AVG_ELT_PER_SEC));
		assertEquals("AVERAGE_HITS_PER_SECOND", SlaThresholdUtils.toIdentifier(KPI.AVG_REQUEST_PER_SEC));
		assertEquals("AVERAGE_THROUGHPUT_PER_SECOND", SlaThresholdUtils.toIdentifier(KPI.AVG_THROUGHPUT_PER_SEC));
		assertEquals("TOTAL_ERRORS", SlaThresholdUtils.toIdentifier(KPI.ERRORS_COUNT));
		assertEquals("TOTAL_HITS", SlaThresholdUtils.toIdentifier(KPI.COUNT));
		assertEquals("TOTAL_THROUGHPUT", SlaThresholdUtils.toIdentifier(KPI.THROUGHPUT));
		assertEquals("AVERAGE_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KPI.AVG_RESP_TIME));
		assertEquals("ERRORS_PER_SECOND", SlaThresholdUtils.toIdentifier(KPI.ERRORS_PER_SEC));
		assertEquals("ERROR_PERCENTILE", SlaThresholdUtils.toIdentifier(KPI.ERROR_RATE));
	}
	
	@Test
	public void toFamily() {
		assertEquals("PER_RUN", SlaThresholdUtils.toFamily(Scope.PER_TEST));
		assertEquals("PER_TIME_INTERVAL", SlaThresholdUtils.toFamily(Scope.PER_INTERVAL));
	}
}