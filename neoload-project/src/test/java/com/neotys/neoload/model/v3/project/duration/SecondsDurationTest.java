package com.neotys.neoload.model.v3.project.duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SecondsDurationTest {
	private static void assertLiteral(final long seconds, final String text) {
		final SecondsDuration duration = SecondsDuration.parse(text);
		assertTrue(text, duration.isLiteral());
		assertEquals(text, seconds, duration.getAmount());
		assertEquals(SecondsDuration.of(seconds), duration);
	}

	private static void assertInvalid(final String text) {
		final SecondsDuration duration = SecondsDuration.parse(text);
		assertTrue(text, duration.isInvalid());
		assertFalse(text, duration.isLiteral());
		assertFalse(text, duration.isVariable());
		assertEquals(text, duration.toText());
	}

	@Test
	public void plainNumberMeansSeconds() {
		assertLiteral(90, "90");
		assertLiteral(0, "0");
		assertLiteral(7200, " 7200 ");
	}

	@Test
	public void orderedUnitsAreAccepted() {
		assertLiteral(90, "90s");
		assertLiteral(5400, "1h30m");
		assertLiteral(7200, "120m");
		assertLiteral(360000, "100h");
		assertLiteral(3661, "1h1m1s");
		assertLiteral(3601, "1h1s");
		assertLiteral(5400, "1h 30m");
	}

	@Test
	public void millisecondsOutOfOrderVariablesAndGarbageAreInvalid() {
		assertInvalid("500ms");
		assertInvalid("1s1h");
		assertInvalid("30s1m");
		assertInvalid("${v}");
		assertInvalid("abc");
		assertInvalid("5x");
		assertInvalid("-5");
		assertInvalid("1.5");
		assertInvalid("");
	}

	@Test
	public void overflowIsInvalidInsteadOfThrowing() {
		assertInvalid("99999999999999999999");
		assertInvalid("99999999999999999h");
		assertInvalid("9223372036854775807h");
	}

	@Test
	public void negativeLiteralIsInvalid() {
		final SecondsDuration duration = SecondsDuration.of(-1);
		assertTrue(duration.isInvalid());
		assertEquals("-1", duration.toText());
	}

	@Test
	public void formatsCompactly() {
		assertEquals("1m30s", SecondsDuration.of(90).toText());
		assertEquals("1h", SecondsDuration.of(3600).toText());
		assertEquals("1m1s", SecondsDuration.of(61).toText());
		assertEquals("1h1m1s", SecondsDuration.of(3661).toText());
		assertEquals("100h", SecondsDuration.of(360000).toText());
		assertEquals("0", SecondsDuration.of(0).toText());
	}

	@Test
	public void formattingThenParsingGivesTheSameValue() {
		for (final long seconds : new long[] {0, 1, 59, 60, 61, 3599, 3600, 3661, 86400, 360000}) {
			assertEquals(SecondsDuration.of(seconds), SecondsDuration.parse(SecondsDuration.of(seconds).toText()));
		}
	}

	@Test
	public void equalityIsByValueAndKind() {
		assertEquals(SecondsDuration.of(90), SecondsDuration.parse("1m30s"));
		assertEquals(SecondsDuration.of(90).hashCode(), SecondsDuration.parse("90").hashCode());
		assertNotEquals(SecondsDuration.of(90), SecondsDuration.of(91));
		assertNotEquals(SecondsDuration.of(5), SecondsDuration.parse("5x"));
	}

	@Test(expected = IllegalStateException.class)
	public void invalidDurationHasNoAmount() {
		SecondsDuration.parse("abc").getAmount();
	}
}
