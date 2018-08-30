package com.neotys.neoload.model.readers.loadrunner.customaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CustomActionMappingLoaderTest {

	@Test
	public void testGetMapping() {
		assertTrue(CustomActionMappingLoader.getMapping().size() > 0);
	}

}
