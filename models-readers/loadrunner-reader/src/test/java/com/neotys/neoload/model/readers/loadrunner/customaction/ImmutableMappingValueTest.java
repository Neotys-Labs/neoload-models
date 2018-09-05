package com.neotys.neoload.model.readers.loadrunner.customaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ImmutableMappingValueTest {

	@Test
	public void testBuild() {
		assertImmutableMappingValuEquals(new ImmutableMappingValue(""), "");
		assertImmutableMappingValuEquals(new ImmutableMappingValue("1"), "1");
		assertImmutableMappingValuEquals(new ImmutableMappingValue("a"), "a");
		assertImmutableMappingValuEquals(new ImmutableMappingValue("arg"), "arg");
		assertImmutableMappingValuEquals(new ImmutableMappingValue("argX"), "argX");
		assertImmutableMappingValuEquals(new ImmutableMappingValue("arg0a"), "arg0a");
		assertImmutableMappingValuEquals(new ImmutableMappingValue(0), "arg0");
		assertImmutableMappingValuEquals(new ImmutableMappingValue(1), "arg1");
		assertImmutableMappingValuEquals(new ImmutableMappingValue(2), "arg2");
	}

	private static void assertImmutableMappingValuEquals(final ImmutableMappingValue expectedImmutableMappingValue, final String string) {
		assertTrue(expectedImmutableMappingValue.getValue().equals(ImmutableMappingValue.build(string).getValue()));
	}
}
