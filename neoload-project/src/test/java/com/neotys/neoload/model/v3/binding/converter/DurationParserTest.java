package com.neotys.neoload.model.v3.binding.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.Test;

public class DurationParserTest {

	@Test
	public void plainNumberIsInTheFieldUnit() {
		assertEquals(Duration.ofMillis(1500), DurationParser.parse("1500", ChronoUnit.MILLIS));
		assertEquals(Duration.ofSeconds(90), DurationParser.parse("90", ChronoUnit.SECONDS));
	}

	@Test
	public void orderedUnitsAreTheSameWhateverTheField() {
		for (final ChronoUnit unit : new ChronoUnit[] {ChronoUnit.MILLIS, ChronoUnit.SECONDS}) {
			assertEquals(Duration.ofMillis(3723004), DurationParser.parse("1h2m3s4ms", unit));
			assertEquals(Duration.ofMillis(250), DurationParser.parse("250ms", unit));
			assertEquals(Duration.ofMillis(3661001), DurationParser.parse(" 1h 1m 1s 1ms ", unit));
			assertEquals(Duration.ofSeconds(5400), DurationParser.parse("1h30m", unit));
			assertEquals(Duration.ofHours(100), DurationParser.parse("100h", unit));
			assertEquals(Duration.ZERO, DurationParser.parse("0h0m0s", unit));
		}
	}

	@Test
	public void invalidTextsAreRejected() {
		for (final String text : new String[] {null, "", " ", "abc", "5x", "1s1h", "1s1s", "1s5", "-1", "1.5", "5ms1s", "1 h", "99999999999999999999", "99999999999999999h"}) {
			assertThrows(text, InvalidDurationException.class, () -> DurationParser.parse(text, ChronoUnit.MILLIS));
			assertThrows(text, InvalidDurationException.class, () -> DurationParser.parse(text, ChronoUnit.SECONDS));
		}
	}

	@Test
	public void invalidDurationMessageNamesTheRawValue() {
		final InvalidDurationException exception = assertThrows(InvalidDurationException.class, () -> DurationParser.parse("1s1h", ChronoUnit.SECONDS));

		assertEquals("Incorrect duration: '1s1h' is not a valid duration.", exception.getMessage());
	}

	@Test
	public void formatIsCompact() {
		assertEquals("1m30s", DurationParser.format(Duration.ofSeconds(90)));
		assertEquals("1h", DurationParser.format(Duration.ofSeconds(3600)));
		assertEquals("100h", DurationParser.format(Duration.ofHours(100)));
		assertEquals("1s500ms", DurationParser.format(Duration.ofMillis(1500)));
		assertEquals("0", DurationParser.format(Duration.ZERO));
	}

	@Test
	public void formatThenParseGivesTheSameDuration() {
		for (final long milliseconds : new long[] {1, 999, 1000, 90000, 3601001, 360000000}) {
			final Duration duration = Duration.ofMillis(milliseconds);

			assertEquals(duration, DurationParser.parse(DurationParser.format(duration), ChronoUnit.SECONDS));
		}
	}
}
