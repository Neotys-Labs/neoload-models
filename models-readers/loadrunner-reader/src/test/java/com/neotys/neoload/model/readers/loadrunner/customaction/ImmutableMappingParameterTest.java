package com.neotys.neoload.model.readers.loadrunner.customaction;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.neotys.neoload.model.repository.CustomActionParameter.Type;

public class ImmutableMappingParameterTest {
	
	@Test
	public void testBuild() {		
		final Map<String, Map<String, String>> parameters = new HashMap<>();
		parameters.put("connectionString", getMap("type", "TEXT", "value", "toto"));
		parameters.put("param2", getMap("type", "PASSWORD", "value", "arg1"));
		final ImmutableMappingParameter actualParam1 = ImmutableMappingParameter.build((Entry<?, ?>)parameters.entrySet().toArray()[0]);
		assertEquals("connectionString", actualParam1.getName());
		assertEquals("toto", actualParam1.getValue());
		assertEquals(Type.TEXT, actualParam1.getType());
		
		final ImmutableMappingParameter actualParam2 = ImmutableMappingParameter.build((Entry<?, ?>)parameters.entrySet().toArray()[1]);
		assertEquals("param2", actualParam2.getName());
		assertEquals("arg1", actualParam2.getValue());
		assertEquals(Type.PASSWORD, actualParam2.getType());
	}

	private static Map<String, String> getMap(final String key1, final String value1, final String key2, final String value2){
		final Map<String, String> map = new HashMap<>();
		map.put(key1, value1);
		map.put(key2, value2);
		return map;		
	}
	
}
