package com.neotys.neoload.model.v3.project.duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;

public class MillisecondsDurationTest {
	private static void assertLiteral(final long milliseconds, final String text) {
		final MillisecondsDuration duration = MillisecondsDuration.parse(text);
		assertTrue(text, duration.isLiteral());
		assertEquals(text, milliseconds, duration.getAmount());
		assertEquals(MillisecondsDuration.of(milliseconds), duration);
	}

	private static void assertVariable(final String name, final String text) {
		final MillisecondsDuration duration = MillisecondsDuration.parse(text);
		assertTrue(text, duration.isVariable());
		assertEquals(text, Optional.of(name), duration.getVariable());
		assertEquals(MillisecondsDuration.ofVariable(name), duration);
		assertEquals(text, duration.toText());
	}

	private static void assertInvalid(final String text) {
		final MillisecondsDuration duration = MillisecondsDuration.parse(text);
		assertTrue(text, duration.isInvalid());
		assertFalse(text, duration.isLiteral());
		assertFalse(text, duration.isVariable());
		assertEquals(text, duration.toText());
	}

	@Test
	public void plainNumberMeansMilliseconds() {
		assertLiteral(1500, "1500");
		assertLiteral(0, "0");
	}

	@Test
	public void orderedUnitsAreAccepted() {
		assertLiteral(1500, "1s500ms");
		assertLiteral(250, "250ms");
		assertLiteral(3723004, "1h2m3s4ms");
		assertLiteral(120000, "2m");
		assertLiteral(1000, "1s");
	}

	@Test
	public void variablesAreAccepted() {
		assertVariable("x", "${x}");
		assertVariable("timeout", "${timeout}");
		assertVariable("file.column", "${file.column}");
		assertVariable("my-var", "${my-var}");
	}

	@Test
	public void outOfOrderUnitsMalformedVariablesAndGarbageAreInvalid() {
		assertInvalid("1s1h");
		assertInvalid("5ms1s");
		assertInvalid("${}");
		assertInvalid("${a$b}");
		assertInvalid("${a.}");
		assertInvalid("${a");
		assertInvalid("abc");
		assertInvalid("5x");
		assertInvalid("-1");
		assertInvalid("");
	}

	@Test
	public void overflowIsInvalidInsteadOfThrowing() {
		assertInvalid("99999999999999999999");
		assertInvalid("99999999999999999h");
	}

	@Test
	public void formatsCompactly() {
		assertEquals("1s500ms", MillisecondsDuration.of(1500).toText());
		assertEquals("1m30s", MillisecondsDuration.of(90000).toText());
		assertEquals("250ms", MillisecondsDuration.of(250).toText());
		assertEquals("1h2m3s4ms", MillisecondsDuration.of(3723004).toText());
		assertEquals("0", MillisecondsDuration.of(0).toText());
		assertEquals("${x}", MillisecondsDuration.ofVariable("x").toText());
	}

	@Test
	public void formattingThenParsingGivesTheSameValue() {
		for (final long milliseconds : new long[] {0, 1, 999, 1000, 1001, 60000, 3600000, 3723004, 86400000}) {
			assertEquals(MillisecondsDuration.of(milliseconds), MillisecondsDuration.parse(MillisecondsDuration.of(milliseconds).toText()));
		}
	}

	@Test
	public void convertsToSeconds() {
		assertEquals("1.5", MillisecondsDuration.of(1500).toSeconds());
		assertEquals("2", MillisecondsDuration.of(2000).toSeconds());
		assertEquals("0.25", MillisecondsDuration.of(250).toSeconds());
		assertEquals("0", MillisecondsDuration.of(0).toSeconds());
		assertEquals("${x}", MillisecondsDuration.ofVariable("x").toSeconds());
		assertEquals("abc", MillisecondsDuration.parse("abc").toSeconds());
	}

	@Test
	public void convertsFromSeconds() {
		assertEquals(MillisecondsDuration.of(1500), MillisecondsDuration.fromSeconds("1.5"));
		assertEquals(MillisecondsDuration.of(2000), MillisecondsDuration.fromSeconds("2"));
		assertEquals(MillisecondsDuration.of(250), MillisecondsDuration.fromSeconds("0.25"));
		assertEquals(MillisecondsDuration.ofVariable("x"), MillisecondsDuration.fromSeconds("${x}"));
		assertTrue(MillisecondsDuration.fromSeconds("abc").isInvalid());
		assertTrue(MillisecondsDuration.fromSeconds("0.0005").isInvalid());
		assertTrue(MillisecondsDuration.fromSeconds("-1").isInvalid());
		assertTrue(MillisecondsDuration.fromSeconds("99999999999999999999").isInvalid());
	}

	@Test
	public void equalityIsByValueAndKind() {
		assertEquals(MillisecondsDuration.of(1500), MillisecondsDuration.parse("1s500ms"));
		assertEquals(MillisecondsDuration.of(1500).hashCode(), MillisecondsDuration.parse("1500").hashCode());
		assertNotEquals(MillisecondsDuration.of(1500), MillisecondsDuration.of(1501));
		assertNotEquals(MillisecondsDuration.of(1500), SecondsDuration.of(1500));
	}
}
