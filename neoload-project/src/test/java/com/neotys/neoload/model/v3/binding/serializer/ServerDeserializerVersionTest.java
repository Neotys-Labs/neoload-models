package com.neotys.neoload.model.v3.binding.serializer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ServerDeserializerVersionTest {

	private static final int[] V3_2 = {3, 2};

	@Test
	public void unknownKeysAreRejectedFrom32On() {
		assertFalse(ServerDeserializer.isAtLeast("3.0", V3_2));
		assertFalse(ServerDeserializer.isAtLeast("3.1", V3_2));
		assertTrue(ServerDeserializer.isAtLeast("3.2", V3_2));
		assertTrue(ServerDeserializer.isAtLeast("3.10", V3_2));
		assertTrue(ServerDeserializer.isAtLeast("4", V3_2));
	}

	@Test
	public void unreadableVersionIsNotAtLeast() {
		assertFalse(ServerDeserializer.isAtLeast("next", V3_2));
	}
}
