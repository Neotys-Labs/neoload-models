package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.neotys.neoload.model.v3.binding.io.IO.Format;


public class IOTest {
	@Test
	public void getFormatFromFile() {
		final IO mapper = new IO();
		
		assertNull(mapper.getFormat((File)null));
		
		try {
			mapper.getFormat(new File("./project"));
			fail("The extension of the file must be 'yaml' or 'json'");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The extension of the file must be 'yaml' or 'json'", iae.getMessage());
		}

		try {
			mapper.getFormat(new File("./project.yml"));
			fail("The extension of the file must be 'yaml' or 'json'");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The extension of the file must be 'yaml' or 'json'", iae.getMessage());
		}
		
		assertEquals(Format.YAML, mapper.getFormat(new File("./project.yaml")));
		assertEquals(Format.JSON, mapper.getFormat(new File("./project.json")));
	}

	@Test
	public void getFormatFromString() {
		final IO mapper = new IO();
		
		assertNull(mapper.getFormat((String)null));
		assertNull(mapper.getFormat(""));
		assertNull(mapper.getFormat(" \t\r\n"));
		
		assertEquals(Format.YAML, mapper.getFormat(" \t\r\n--- \t\r\n"));
		assertEquals(Format.YAML, mapper.getFormat("---"));
		assertEquals(Format.YAML, mapper.getFormat(" \t\r\nscenarios: \t\r\n"));
		assertEquals(Format.YAML, mapper.getFormat("scenarios:"));
		
		assertEquals(Format.JSON, mapper.getFormat(" \t\r\n{scenarios:[]} \t\r\n"));
		assertEquals(Format.JSON, mapper.getFormat("{scenarios:[]}"));
	}

	@Test
	public void getMapper() {
		final IO mapper = new IO();
		
		try {
			mapper.getMapper(null);
			fail("The format is unknown");
		}
		catch (final IllegalArgumentException iae) {
			assertEquals("The format is unknown", iae.getMessage());
		}
		
		
		assertEquals(YAMLMapper.class, mapper.getMapper(Format.YAML).getClass());
		assertEquals(ObjectMapper.class, mapper.getMapper(Format.JSON).getClass());
	}
}
