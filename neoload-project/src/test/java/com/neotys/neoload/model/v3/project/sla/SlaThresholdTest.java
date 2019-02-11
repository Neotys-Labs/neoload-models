package com.neotys.neoload.model.v3.project.sla;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KeyPerformanceIndicator;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;


public class SlaThresholdTest {
	@Test
	public void constants() {
		assertEquals(Integer.valueOf(90), SlaThreshold.DEFAULT_PERCENT);
		assertEquals(Scope.ON_TEST, SlaThreshold.DEFAULT_SCOPE);
	}
	
	@Test
	public void keyPerformanceIndicatorOf() {
		boolean throwException = false;
		try {
			KeyPerformanceIndicator.of(null);
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
			KeyPerformanceIndicator.of("");
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
			KeyPerformanceIndicator.of("test");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'name' must be: " + Arrays.asList(KeyPerformanceIndicator.values()).toString() +"."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must be: " + Arrays.asList(KeyPerformanceIndicator.values()).toString() +".");
		}		

		assertEquals(KeyPerformanceIndicator.AVG_ELT_PER_SEC, KeyPerformanceIndicator.of("avg-elt-per-sec"));
		assertEquals(KeyPerformanceIndicator.AVG_PAGE_RESP_TIME, KeyPerformanceIndicator.of("avg-page-resp-time"));
		assertEquals(KeyPerformanceIndicator.AVG_REQUEST_PER_SEC, KeyPerformanceIndicator.of("avg-request-per-sec"));
		assertEquals(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME, KeyPerformanceIndicator.of("avg-request-resp-time"));
		assertEquals(KeyPerformanceIndicator.AVG_RESP_TIME, KeyPerformanceIndicator.of("avg-resp-time"));
		assertEquals(KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC, KeyPerformanceIndicator.of("avg-throughput-per-sec"));
		assertEquals(KeyPerformanceIndicator.AVG_TRANSACTION_RESP_TIME, KeyPerformanceIndicator.of("avg-transaction-resp-time"));
		assertEquals(KeyPerformanceIndicator.COUNT, KeyPerformanceIndicator.of("count"));
		assertEquals(KeyPerformanceIndicator.ERROR_RATE, KeyPerformanceIndicator.of("error-rate"));
		assertEquals(KeyPerformanceIndicator.ERRORS_PER_SEC, KeyPerformanceIndicator.of("errors-per-sec"));
		assertEquals(KeyPerformanceIndicator.ERRORS_COUNT, KeyPerformanceIndicator.of("errors-count"));
		assertEquals(KeyPerformanceIndicator.PERC_TRANSACTION_RESP_TIME, KeyPerformanceIndicator.of("perc-transaction-resp-time"));
		assertEquals(KeyPerformanceIndicator.THROUGHPUT, KeyPerformanceIndicator.of("throughput"));
	}

	@Test
	public void keyPerformanceIndicatorFriendlyName() {
		assertEquals("avg-elt-per-sec", KeyPerformanceIndicator.AVG_ELT_PER_SEC.friendlyName());
		assertEquals("avg-page-resp-time", KeyPerformanceIndicator.AVG_PAGE_RESP_TIME.friendlyName());
		assertEquals("avg-request-per-sec", KeyPerformanceIndicator.AVG_REQUEST_PER_SEC.friendlyName());
		assertEquals("avg-request-resp-time", KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME.friendlyName());
		assertEquals("avg-resp-time", KeyPerformanceIndicator.AVG_RESP_TIME.friendlyName());
		assertEquals("avg-throughput-per-sec", KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC.friendlyName());
		assertEquals("avg-transaction-resp-time", KeyPerformanceIndicator.AVG_TRANSACTION_RESP_TIME.friendlyName());
		assertEquals("count", KeyPerformanceIndicator.COUNT.friendlyName());
		assertEquals("error-rate", KeyPerformanceIndicator.ERROR_RATE.friendlyName());
		assertEquals("errors-per-sec", KeyPerformanceIndicator.ERRORS_PER_SEC.friendlyName());
		assertEquals("errors-count", KeyPerformanceIndicator.ERRORS_COUNT.friendlyName());
		assertEquals("perc-transaction-resp-time", KeyPerformanceIndicator.PERC_TRANSACTION_RESP_TIME.friendlyName());
		assertEquals("throughput", KeyPerformanceIndicator.THROUGHPUT.friendlyName());
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
			assertTrue(e.getMessage().contains("The parameter 'name' must be: 'on test' or 'on interval'."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'name' must be: 'on test' or 'on interval'.");
		}

		assertEquals(Scope.ON_TEST, Scope.of("on test"));
		assertEquals(Scope.ON_TEST, Scope.of("on_test"));
		assertEquals(Scope.ON_TEST, Scope.of("ON TEST"));
		assertEquals(Scope.ON_TEST, Scope.of("ON_TEST"));
		
		assertEquals(Scope.ON_INTERVAL, Scope.of("on interval"));
		assertEquals(Scope.ON_INTERVAL, Scope.of("on_interval"));
		assertEquals(Scope.ON_INTERVAL, Scope.of("ON INTERVAL"));
		assertEquals(Scope.ON_INTERVAL, Scope.of("ON_INTERVAL"));
	}

	@Test
	public void scopeFriendlyName() {
		assertEquals("on test", Scope.ON_TEST.friendlyName());
		assertEquals("on interval", Scope.ON_INTERVAL.friendlyName());
	}

	@Test
	public void scopeGetKeyPerformanceIndicators() {
		Set<KeyPerformanceIndicator> pkis = Scope.ON_TEST.getKeyPerformanceIndicators();
		assertEquals(10, pkis.size());
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME));
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_PAGE_RESP_TIME));
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_TRANSACTION_RESP_TIME));
		assertTrue(pkis.contains(KeyPerformanceIndicator.PERC_TRANSACTION_RESP_TIME));
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_REQUEST_PER_SEC));
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC));
		assertTrue(pkis.contains(KeyPerformanceIndicator.THROUGHPUT));
		assertTrue(pkis.contains(KeyPerformanceIndicator.COUNT));
		assertTrue(pkis.contains(KeyPerformanceIndicator.ERRORS_COUNT));
		assertTrue(pkis.contains(KeyPerformanceIndicator.ERROR_RATE));
		
		pkis = Scope.ON_INTERVAL.getKeyPerformanceIndicators();
		assertEquals(5, pkis.size());
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_RESP_TIME));
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_ELT_PER_SEC));
		assertTrue(pkis.contains(KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC));
		assertTrue(pkis.contains(KeyPerformanceIndicator.ERRORS_PER_SEC));
		assertTrue(pkis.contains(KeyPerformanceIndicator.ERROR_RATE));
	}	
}
