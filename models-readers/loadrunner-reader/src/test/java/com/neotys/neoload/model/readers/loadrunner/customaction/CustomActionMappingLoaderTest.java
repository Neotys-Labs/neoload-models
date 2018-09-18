package com.neotys.neoload.model.readers.loadrunner.customaction;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
@SuppressWarnings("squid:S2699")
public class CustomActionMappingLoaderTest {

	@Test
	public void testGetMapping() {
		assertTrue((new CustomActionMappingLoader("")).getMethod("sapgui_open_connection_ex") != null);
	}

}
