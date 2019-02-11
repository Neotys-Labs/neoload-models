package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KeyPerformanceIndicator;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;

public class SlaThresholdUtilsTest {
	@Test
	public void toIdentifier() {
		assertEquals("AVERAGE_REQUEST_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME));
		assertEquals("AVERAGE_PAGE_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_PAGE_RESP_TIME));
		assertEquals("AVERAGE_CONTAINER_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_TRANSACTION_RESP_TIME));
		assertEquals("PERCENTILE_CONTAINER_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.PERC_TRANSACTION_RESP_TIME));
		assertEquals("AVERAGE_HITS_PER_SECOND", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_ELT_PER_SEC));
		assertEquals("AVERAGE_HITS_PER_SECOND", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_REQUEST_PER_SEC));
		assertEquals("AVERAGE_THROUGHPUT_PER_SECOND", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC));
		assertEquals("TOTAL_ERRORS", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.ERRORS_COUNT));
		assertEquals("TOTAL_HITS", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.COUNT));
		assertEquals("TOTAL_THROUGHPUT", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.THROUGHPUT));
		assertEquals("AVERAGE_RESPONSE_TIME", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.AVG_RESP_TIME));
		assertEquals("ERRORS_PER_SECOND", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.ERRORS_PER_SEC));
		assertEquals("ERROR_PERCENTILE", SlaThresholdUtils.toIdentifier(KeyPerformanceIndicator.ERROR_RATE));
	}
	
	@Test
	public void toFamily() {
		assertEquals("PER_RUN", SlaThresholdUtils.toFamily(Scope.ON_TEST));
		assertEquals("PER_TIME_INTERVAL", SlaThresholdUtils.toFamily(Scope.ON_INTERVAL));
	}
}