package com.neotys.neoload.model.v3.project.sla;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;


public class SlaThresholdTest {
	@Test
	public void constants() {
		assertEquals(Integer.valueOf(90), SlaThreshold.DEFAULT_PERCENT);
		assertEquals(Scope.PER_TEST, SlaThreshold.DEFAULT_SCOPE);
	}
	
	@Test
	public void keyPerformanceIndicatorOf() {
		boolean throwException = false;
		try {
			KPI.of(null);
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
			KPI.of("");
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
			KPI.of("test");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must be: " + Arrays.asList(KPI.values()).toString() +"."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must be: " + Arrays.asList(KPI.values()).toString() +".");
		}		

		assertEquals(KPI.AVG_ELT_PER_SEC, KPI.of("avg-elt-per-sec"));
		assertEquals(KPI.AVG_PAGE_RESP_TIME, KPI.of("avg-page-resp-time"));
		assertEquals(KPI.AVG_REQUEST_PER_SEC, KPI.of("avg-request-per-sec"));
		assertEquals(KPI.AVG_REQUEST_RESP_TIME, KPI.of("avg-request-resp-time"));
		assertEquals(KPI.AVG_RESP_TIME, KPI.of("avg-resp-time"));
		assertEquals(KPI.AVG_THROUGHPUT_PER_SEC, KPI.of("avg-throughput-per-sec"));
		assertEquals(KPI.AVG_TRANSACTION_RESP_TIME, KPI.of("avg-transaction-resp-time"));
		assertEquals(KPI.COUNT, KPI.of("count"));
		assertEquals(KPI.ERROR_RATE, KPI.of("error-rate"));
		assertEquals(KPI.ERRORS_PER_SEC, KPI.of("errors-per-sec"));
		assertEquals(KPI.ERRORS_COUNT, KPI.of("errors-count"));
		assertEquals(KPI.PERC_TRANSACTION_RESP_TIME, KPI.of("perc-transaction-resp-time"));
		assertEquals(KPI.THROUGHPUT, KPI.of("throughput"));
	}

	@Test
	public void keyPerformanceIndicatorFriendlyName() {
		assertEquals("avg-elt-per-sec", KPI.AVG_ELT_PER_SEC.friendlyName());
		assertEquals("avg-page-resp-time", KPI.AVG_PAGE_RESP_TIME.friendlyName());
		assertEquals("avg-request-per-sec", KPI.AVG_REQUEST_PER_SEC.friendlyName());
		assertEquals("avg-request-resp-time", KPI.AVG_REQUEST_RESP_TIME.friendlyName());
		assertEquals("avg-resp-time", KPI.AVG_RESP_TIME.friendlyName());
		assertEquals("avg-throughput-per-sec", KPI.AVG_THROUGHPUT_PER_SEC.friendlyName());
		assertEquals("avg-transaction-resp-time", KPI.AVG_TRANSACTION_RESP_TIME.friendlyName());
		assertEquals("count", KPI.COUNT.friendlyName());
		assertEquals("error-rate", KPI.ERROR_RATE.friendlyName());
		assertEquals("errors-per-sec", KPI.ERRORS_PER_SEC.friendlyName());
		assertEquals("errors-count", KPI.ERRORS_COUNT.friendlyName());
		assertEquals("perc-transaction-resp-time", KPI.PERC_TRANSACTION_RESP_TIME.friendlyName());
		assertEquals("throughput", KPI.THROUGHPUT.friendlyName());
	}

	@Test
	public void scopeOf() {
		boolean throwException = false;
		try {
			Scope.of(null);
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
			Scope.of("");
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
			Scope.of("test");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must be: 'per test' or 'per interval'."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must be: 'per test' or 'per interval'.");
		}

		assertEquals(Scope.PER_TEST, Scope.of("per test"));
		assertEquals(Scope.PER_TEST, Scope.of("per_test"));
		assertEquals(Scope.PER_TEST, Scope.of("PER TEST"));
		assertEquals(Scope.PER_TEST, Scope.of("PER_TEST"));
		
		assertEquals(Scope.PER_INTERVAL, Scope.of("per interval"));
		assertEquals(Scope.PER_INTERVAL, Scope.of("per_interval"));
		assertEquals(Scope.PER_INTERVAL, Scope.of("PER INTERVAL"));
		assertEquals(Scope.PER_INTERVAL, Scope.of("PER_INTERVAL"));
	}

	@Test
	public void scopeFriendlyName() {
		assertEquals("per test", Scope.PER_TEST.friendlyName());
		assertEquals("per interval", Scope.PER_INTERVAL.friendlyName());
	}

	@Test
	public void scopeGetKeyPerformanceIndicators() {
		Set<KPI> pkis = Scope.PER_TEST.getKpis();
		assertEquals(10, pkis.size());
		assertTrue(pkis.contains(KPI.AVG_REQUEST_RESP_TIME));
		assertTrue(pkis.contains(KPI.AVG_PAGE_RESP_TIME));
		assertTrue(pkis.contains(KPI.AVG_TRANSACTION_RESP_TIME));
		assertTrue(pkis.contains(KPI.PERC_TRANSACTION_RESP_TIME));
		assertTrue(pkis.contains(KPI.AVG_REQUEST_PER_SEC));
		assertTrue(pkis.contains(KPI.AVG_THROUGHPUT_PER_SEC));
		assertTrue(pkis.contains(KPI.THROUGHPUT));
		assertTrue(pkis.contains(KPI.COUNT));
		assertTrue(pkis.contains(KPI.ERRORS_COUNT));
		assertTrue(pkis.contains(KPI.ERROR_RATE));
		
		pkis = Scope.PER_INTERVAL.getKpis();
		assertEquals(5, pkis.size());
		assertTrue(pkis.contains(KPI.AVG_RESP_TIME));
		assertTrue(pkis.contains(KPI.AVG_ELT_PER_SEC));
		assertTrue(pkis.contains(KPI.AVG_THROUGHPUT_PER_SEC));
		assertTrue(pkis.contains(KPI.ERRORS_PER_SEC));
		assertTrue(pkis.contains(KPI.ERROR_RATE));
	}	
}
