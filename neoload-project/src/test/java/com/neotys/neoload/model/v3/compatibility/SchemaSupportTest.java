package com.neotys.neoload.model.v3.compatibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SchemaSupportTest {

	@Test
	public void default_instance_supports_3_0() {
		assertTrue(SchemaSupport.getDefault().isSupported("3.0"));
	}

	@Test
	public void default_instance_rejects_unknown_version() {
		assertFalse(SchemaSupport.getDefault().isSupported("3.999"));
		assertFalse(SchemaSupport.getDefault().isSupported("4.0"));
		assertFalse(SchemaSupport.getDefault().isSupported("bogus"));
	}

	@Test
	public void null_is_not_supported() {
		assertFalse(SchemaSupport.getDefault().isSupported(null));
	}

	@Test
	public void of_accepts_explicit_supported_list() {
		final SchemaSupport instance = SchemaSupport.of(
				new SupportedSchemas(Arrays.asList("3.0", "3.1", "3.2")));
		assertTrue(instance.isSupported("3.0"));
		assertTrue(instance.isSupported("3.1"));
		assertTrue(instance.isSupported("3.2"));
		assertFalse(instance.isSupported("3.3"));
		assertEquals(3, instance.listSupported().size());
	}

	@Test
	public void list_supported_is_immutable_view() {
		final SchemaSupport instance = SchemaSupport.of(
				new SupportedSchemas(List.of("3.0")));
		try {
			instance.listSupported().add("3.1");
			fail("listSupported() must return an immutable set");
		} catch (final UnsupportedOperationException expected) {
			assertEquals(UnsupportedOperationException.class, expected.getClass());
		}
	}
}
