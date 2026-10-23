package com.neotys.neoload.model.v3.binding.converter;


import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter.Type;
import org.junit.Test;

public class StringToStopAfterConverterTest {
	private static final StopAfter ERROR = StopAfter.builder().build();
	
	private static final StopAfter CURRENT_ITERATION = StopAfter.builder()
			.type(Type.CURRENT_ITERATION)
			.build();

	private static StopAfter convertToInvalidStopAfter(final String input) {
		return StopAfter.builder()
				.value(SecondsDuration.parse(input))
				.type(StopAfter.Type.TIME)
				.build();
	}

	private static StopAfter convertToStopAfter(final int input) {
		return StopAfter.builder()
				.value(SecondsDuration.of(input))
				.type(Type.TIME)
				.build();
	}
	
	@Test
	public void shouldConvertCorrectly() {
		final StringToStopAfterConverter converter = new StringToStopAfterConverter();
		// Input: NULL - Output: -1
		assertEquals(ERROR, converter.convert(null));
		// Input: EMPTY - Output: -1
		assertEquals(ERROR, converter.convert(""));

		// TIME
		// Input: 48h30m15s - Output: 48 * 60 * 60 + 30 * 60 + 15
		assertEquals(convertToStopAfter(48 * 60 * 60 + 30 * 60 + 15), converter.convert("48h30m15s"));
		// Input: 0h30m15s - Output: 30 * 60 + 15
		assertEquals(convertToStopAfter(30 * 60 + 15), converter.convert("0h30m15s"));
		// Input: 0h0m15s - Output: 15
		assertEquals(convertToStopAfter(15), converter.convert("0h0m15s"));
		// Input: 0h0m0s - Output: 0
		assertEquals(convertToStopAfter(0), converter.convert("0h0m0s"));

		// Input: 48h30m - Output: 48 * 60 * 60 + 30 * 60
		assertEquals(convertToStopAfter(48 * 60 * 60 + 30 * 60), converter.convert("48h30m"));
		// Input: 0h30m - Output: 30 * 60
		assertEquals(convertToStopAfter(30 * 60), converter.convert("0h30m"));
		// Input: 0h0m - Output: 0
		assertEquals(convertToStopAfter(0), converter.convert("0h0m"));
	
		// Input: 30m15s - Output: 30 * 60 + 15
		assertEquals(convertToStopAfter(30 * 60 + 15), converter.convert("30m15s"));
		// Input: 0m15s - Output: 15
		assertEquals(convertToStopAfter(15), converter.convert("0m15s"));
		// Input: 0m0s - Output: 0
		assertEquals(convertToStopAfter(0), converter.convert("0m0s"));

		// Input: 48h - Output: 48 * 60 * 60
		assertEquals(convertToStopAfter(48 * 60 * 60), converter.convert("48h"));
		// Input: 90m - Output: 90 * 60
		assertEquals(convertToStopAfter(90 * 60), converter.convert("90m"));
		// Input: 15s - Output: 15
		assertEquals(convertToStopAfter(15), converter.convert("15s"));

		// Input: 0h - Output: 0
		assertEquals(convertToStopAfter(0), converter.convert("0h"));
		// Input: 0m - Output: 0
		assertEquals(convertToStopAfter(0), converter.convert("0m"));
		// Input: 0s - Output: 0
		assertEquals(convertToStopAfter(0), converter.convert("0s"));

		// Input: 900 - Output: 900 (15 minutes)
		assertEquals(convertToStopAfter(900), converter.convert("900"));

		// Input: 0 - Output: 0 
		assertEquals(convertToStopAfter(0), converter.convert("0"));
		
		assertEquals(convertToInvalidStopAfter("-1"), converter.convert("-1"));

		// CURRENT_ITERATION
		assertEquals(convertToInvalidStopAfter("xXx"), converter.convert("xXx"));
		
		// Input: current_iteration - Output: current_iteration
		assertEquals(CURRENT_ITERATION, converter.convert("current_iteration"));

	}
}
