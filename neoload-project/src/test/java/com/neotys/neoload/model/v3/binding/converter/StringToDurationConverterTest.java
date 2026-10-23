package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration.Type;
import org.junit.Test;

public class StringToDurationConverterTest {
	private static final LoadDuration ERROR = LoadDuration.builder().build();

	private static LoadDuration convertToInvalidTimeDuration(final String input) {
		return LoadDuration.builder()
				.value(SecondsDuration.parse(input))
				.type(Type.TIME)
				.build();
	}

	private static LoadDuration convertToTimeDuration(final int input) {
		return LoadDuration.builder()
				.value(SecondsDuration.of(input))
				.type(Type.TIME)
				.build();
	}
	
	private static LoadDuration convertToIterationDuration(final int input) {
		return LoadDuration.builder()
				.value(input)
				.type(Type.ITERATION)
				.build();
	}

	@Test
	public void shouldConvertCorrectly() {
		final StringToDurationConverter converter = new StringToDurationConverter();
		// Input: NULL - Output: -1
		assertEquals(ERROR, converter.convert(null));
		// Input: EMPTY - Output: -1
		assertEquals(ERROR, converter.convert(""));
		assertEquals(convertToInvalidTimeDuration("-1"), converter.convert("-1"));
		assertEquals(convertToInvalidTimeDuration("xXx"), converter.convert("xXx"));
		assertEquals(convertToInvalidTimeDuration("x48hx30m"), converter.convert("x48hx30m"));

		// TIME
		// Input: 48h30m15s - Output: 48 * 60 * 60 + 30 * 60 + 15
		assertEquals(convertToTimeDuration(48 * 60 * 60 + 30 * 60 + 15), converter.convert("48h30m15s"));
		// Input: 0h30m15s - Output: 30 * 60 + 15
		assertEquals(convertToTimeDuration(30 * 60 + 15), converter.convert("0h30m15s"));
		// Input: 0h0m15s - Output: 15
		assertEquals(convertToTimeDuration(15), converter.convert("0h0m15s"));
		// Input: 0h0m0s - Output: 0
		assertEquals(convertToTimeDuration(0), converter.convert("0h0m0s"));

		// Input: 48h30m - Output: 48 * 60 * 60 + 30 * 60
		assertEquals(convertToTimeDuration(48 * 60 * 60 + 30 * 60), converter.convert("48h30m"));
		// Input: 0h30m - Output: 30 * 60
		assertEquals(convertToTimeDuration(30 * 60), converter.convert("0h30m"));
		// Input: 0h0m - Output: 0
		assertEquals(convertToTimeDuration(0), converter.convert("0h0m"));
	
		// Input: 30m15s - Output: 30 * 60 + 15
		assertEquals(convertToTimeDuration(30 * 60 + 15), converter.convert("30m15s"));
		// Input: 0m15s - Output: 15
		assertEquals(convertToTimeDuration(15), converter.convert("0m15s"));
		// Input: 0m0s - Output: 0
		assertEquals(convertToTimeDuration(0), converter.convert("0m0s"));

		// Input: 48h - Output: 48 * 60 * 60
		assertEquals(convertToTimeDuration(48 * 60 * 60), converter.convert("48h"));
		// Input: 90m - Output: 90 * 60
		assertEquals(convertToTimeDuration(90 * 60), converter.convert("90m"));
		// Input: 15s - Output: 15
		assertEquals(convertToTimeDuration(15), converter.convert("15s"));

		// Input: 0h - Output: 0
		assertEquals(convertToTimeDuration(0), converter.convert("0h"));
		// Input: 0m - Output: 0
		assertEquals(convertToTimeDuration(0), converter.convert("0m"));
		// Input: 0s - Output: 0
		assertEquals(convertToTimeDuration(0), converter.convert("0s"));

		// Input: 900 - Output: 900 (15 minutes)
		assertEquals(convertToTimeDuration(900), converter.convert("900"));

		// Input: 0 - Output: 0 
		assertEquals(convertToTimeDuration(0), converter.convert("0"));
		
		// ITERATION
		// Input: 30 iterations - Output: 30
		assertEquals(convertToIterationDuration(30), converter.convert("30 iterations"));
		// Input: 15iterations - Output: 15
		assertEquals(convertToIterationDuration(15), converter.convert("15iterations"));
		// Input: 30 iteration - Output: 30
		assertEquals(convertToIterationDuration(30), converter.convert("30 iteration"));
		// Input: 15iteration - Output: 15
		assertEquals(convertToIterationDuration(15), converter.convert("15iteration"));

		// Input: 1 iterations - Output: 1
		assertEquals(convertToIterationDuration(30), converter.convert("30 iterations"));
		// Input: 1iterations - Output: 1
		assertEquals(convertToIterationDuration(15), converter.convert("15iterations"));
		// Input: 1 iteration - Output: 1
		assertEquals(convertToIterationDuration(30), converter.convert("30 iteration"));
		// Input: 1iteration - Output: 1
		assertEquals(convertToIterationDuration(15), converter.convert("15iteration"));
	}
}
