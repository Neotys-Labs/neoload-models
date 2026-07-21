package com.neotys.neoload.model.v3.binding.converter;


import com.neotys.neoload.model.v3.project.userpath.Match;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class MatchToStringConverterTest {

	@Test
	public void shouldConvertCorrectly() {
		final MatchToStringConverter converter = new MatchToStringConverter();
		// Input: NULL - Output: NULL
		assertNull(converter.convert(null));

		assertEquals("any", converter.convert(Match.ANY));
		assertEquals("all", converter.convert(Match.ALL));
	}
}
