package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class DurationsTest {

	private static void assertMilliseconds(final long expected, final Optional<BigDecimal> actual) {
		assertTrue(actual.isPresent());
		assertEquals(0, BigDecimal.valueOf(expected).compareTo(actual.get()));
	}

	@Test
	public void plainNumberIsExpressedInTheGivenUnit() {
		assertMilliseconds(1_500, Durations.toMilliseconds("1500", TimeUnit.MILLISECONDS));
		assertMilliseconds(2_000, Durations.toMilliseconds("2", TimeUnit.SECONDS));
		assertMilliseconds(1_500, Durations.toMilliseconds("1.5", TimeUnit.SECONDS));
	}

	@Test
	public void unitSegmentsAreSummed() {
		assertMilliseconds(500, Durations.toMilliseconds("500ms", TimeUnit.MILLISECONDS));
		assertMilliseconds(3_000, Durations.toMilliseconds("3s", TimeUnit.MILLISECONDS));
		assertMilliseconds(90_000, Durations.toMilliseconds("1m30s", TimeUnit.MILLISECONDS));
		assertMilliseconds(5_400_000, Durations.toMilliseconds("1h30m", TimeUnit.SECONDS));
		assertMilliseconds(3_723_004, Durations.toMilliseconds("1h2m3s4ms", TimeUnit.MILLISECONDS));
	}

	@Test
	public void variableOrMalformedDurationHasNoValue() {
		assertFalse(Durations.toMilliseconds(null, TimeUnit.MILLISECONDS).isPresent());
		assertFalse(Durations.toMilliseconds("", TimeUnit.MILLISECONDS).isPresent());
		assertFalse(Durations.toMilliseconds("${max_pacing}", TimeUnit.MILLISECONDS).isPresent());
		assertFalse(Durations.toMilliseconds("abc", TimeUnit.MILLISECONDS).isPresent());
		assertFalse(Durations.toMilliseconds("1.5s", TimeUnit.MILLISECONDS).isPresent());
		assertFalse(Durations.toMilliseconds("1m30", TimeUnit.MILLISECONDS).isPresent());
		assertFalse(Durations.toMilliseconds("x1m", TimeUnit.MILLISECONDS).isPresent());
	}
}
