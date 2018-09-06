package com.neotys.neoload.model.readers.loadrunner.customaction;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.neotys.neoload.model.repository.CustomActionParameter.Type;

public class ImmutableMappingMethodTest {

	@Test
	public void testBuild() {
		final Map<String, Map<String, String>> parameters = new HashMap<>();
		parameters.put("connectionString", getMap("type", "TEXT", "value", "toto"));
		parameters.put("param2", getMap("type", "PASSWORD", "value", "arg1"));		
		final Map<String, Object> methodMapping = new HashMap<>();
		methodMapping.put("type", "SapConnect");
		methodMapping.put("isHit", true);
		methodMapping.put("name", "Connect");
		methodMapping.put("parameters", parameters);
		methodMapping.put("ignoreArgs", "arg1, arg2");

		final ImmutableMappingMethod actualImmutableMappingMethod = ImmutableMappingMethod.build(methodMapping);
		assertEquals("SapConnect", actualImmutableMappingMethod.getType());
		assertEquals(true, actualImmutableMappingMethod.isHit());
		assertEquals("Connect", actualImmutableMappingMethod.getName());
		assertEquals(2, actualImmutableMappingMethod.getParameters().size());
		assertEquals("connectionString", actualImmutableMappingMethod.getParameters().get(0).getName());
		assertEquals("toto", actualImmutableMappingMethod.getParameters().get(0).getValue().getLeft().get());
		assertEquals(Type.TEXT, actualImmutableMappingMethod.getParameters().get(0).getType());
		assertEquals("param2", actualImmutableMappingMethod.getParameters().get(1).getName());
		assertEquals(1, actualImmutableMappingMethod.getParameters().get(1).getValue().getRight().get().getIndex());
		assertEquals(Type.PASSWORD, actualImmutableMappingMethod.getParameters().get(1).getType());
		assertEquals(2, actualImmutableMappingMethod.getIgnoreArgs().size());
		assertEquals(1, actualImmutableMappingMethod.getIgnoreArgs().get(0).intValue());
		assertEquals(2, actualImmutableMappingMethod.getIgnoreArgs().get(1).intValue());
	}

	private static Map<String, String> getMap(final String key1, final String value1, final String key2, final String value2) {
		final Map<String, String> map = new HashMap<>();
		map.put(key1, value1);
		map.put(key2, value2);
		return map;
	}
}
