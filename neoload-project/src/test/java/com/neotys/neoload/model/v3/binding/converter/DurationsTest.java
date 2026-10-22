package com.neotys.neoload.model.v3.binding.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DurationsTest {

	@Test
	public void parseSecondsGrammar() {
		assertEquals(ParsedDuration.literal(5400, "s"), Durations.parse("1h30m", DurationGrammar.SECONDS));
		assertEquals(ParsedDuration.literal(90, "s"), Durations.parse("90", DurationGrammar.SECONDS));
		assertEquals(ParsedDuration.literal(360000, "s"), Durations.parse("100h", DurationGrammar.SECONDS));
		assertEquals(ParsedDuration.literal(7200, "s"), Durations.parse("120m", DurationGrammar.SECONDS));
	}

	@Test
	public void parseMillisecondsGrammar() {
		assertEquals(ParsedDuration.literal(1500, "ms"), Durations.parse("1s500ms", DurationGrammar.MILLISECONDS));
		assertEquals(ParsedDuration.literal(1500, "ms"), Durations.parse("1500", DurationGrammar.MILLISECONDS));
		assertEquals(ParsedDuration.literal(3723004, "ms"), Durations.parse("1h2m3s4ms", DurationGrammar.MILLISECONDS));
		assertEquals(ParsedDuration.literal(250, "ms"), Durations.parse("250ms", DurationGrammar.MILLISECONDS));
		assertEquals(ParsedDuration.variable("x"), Durations.parse("${x}", DurationGrammar.MILLISECONDS));
	}

	@Test
	public void parseOffsetGrammar() {
		assertEquals(ParsedDuration.literal(-5, "d"), Durations.parse("-5d", DurationGrammar.OFFSET));
	}

	@Test
	public void secondsGrammarRejectsMillisecondsAndVariables() {
		assertFalse(Durations.validate("500ms", DurationGrammar.SECONDS));
		assertFalse(Durations.validate("${x}", DurationGrammar.SECONDS));
	}

	@Test
	public void rejectsOutOfOrderOverflowAndNegative() {
		for (final DurationGrammar grammar : new DurationGrammar[] {DurationGrammar.SECONDS, DurationGrammar.MILLISECONDS, DurationGrammar.OFFSET}) {
			for (final String text : new String[] {"1s1h", "5ms1s", "99999999999999999999", "99999999999999999999h", "-1", "abc", "5x", "", null}) {
				assertThrows(text, InvalidDurationException.class, () -> Durations.parse(text, grammar));
				assertFalse(Durations.validate(text, grammar));
			}
		}
	}

	@Test
	public void secondsGrammarRejectsIntegerOverflow() {
		assertFalse(Durations.validate("2147483648", DurationGrammar.SECONDS));
		assertTrue(Durations.validate("2147483647", DurationGrammar.SECONDS));
	}

	@Test
	public void format() {
		assertEquals("1m30s", Durations.format(90, DurationGrammar.SECONDS));
		assertEquals("1h", Durations.format(3600, DurationGrammar.SECONDS));
		assertEquals("1m1s", Durations.format(61, DurationGrammar.SECONDS));
		assertEquals("1s500ms", Durations.format(1500, DurationGrammar.MILLISECONDS));
		assertEquals("0", Durations.format(0, DurationGrammar.MILLISECONDS));
	}
}
