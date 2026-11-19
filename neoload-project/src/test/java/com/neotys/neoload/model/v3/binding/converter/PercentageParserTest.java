package com.neotys.neoload.model.v3.binding.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class PercentageParserTest {

	@Test
	public void percentSignIsOptional() {
		assertEquals(150, PercentageParser.parse("150%"));
		assertEquals(150, PercentageParser.parse("150"));
		assertEquals(0, PercentageParser.parse("0%"));
		assertEquals(0, PercentageParser.parse("0"));
	}

	@Test
	public void negativeValuesAreReadAndLeftToValidation() {
		assertEquals(-10, PercentageParser.parse("-10%"));
		assertEquals(-1, PercentageParser.parse("-1"));
	}

	@Test
	public void invalidTextsAreRejected() {
		for (final String text : new String[] {null, "", " ", "abc", "1.5", "10%%", "%", "5 %", "%10", "${name}", "${my_factor}%", "99999999999", "-99999999999%", " 50%", "50% ", " 50 "}) {
			assertThrows(text, InvalidPercentageException.class, () -> PercentageParser.parse(text));
		}
	}

	@Test
	public void invalidPercentageMessageNamesTheRawValue() {
		final InvalidPercentageException exception = assertThrows(InvalidPercentageException.class, () -> PercentageParser.parse("10%%"));

		assertEquals("Incorrect percentage: '10%%' is not a valid percentage.", exception.getMessage());
	}

	@Test
	public void formatAlwaysWritesThePercentSign() {
		assertEquals("150%", PercentageParser.format(150));
		assertEquals("0%", PercentageParser.format(0));
		assertEquals("-10%", PercentageParser.format(-10));
	}

	@Test
	public void formatThenParseGivesTheSamePercentage() {
		for (final int percentage : new int[] {0, 1, 100, 150, 100000}) {
			assertEquals(percentage, PercentageParser.parse(PercentageParser.format(percentage)));
		}
	}
}
