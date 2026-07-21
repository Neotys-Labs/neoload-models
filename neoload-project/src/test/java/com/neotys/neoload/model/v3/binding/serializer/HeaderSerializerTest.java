package com.neotys.neoload.model.v3.binding.serializer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.userpath.Header;


public class HeaderSerializerTest {

	@Test
	public void serializeNameAndValue() throws IOException {
		assertEquals("{\"Accept\":\"application/json\"}",
				serialize(Header.builder().name("Accept").value("application/json").build()));
	}

	@Test
	public void serializeNameWithoutValue() throws IOException {
		assertEquals("{\"X-Custom\":null}",
				serialize(Header.builder().name("X-Custom").build()));
	}

	private static String serialize(final Header header) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final StringWriter writer = new StringWriter();
		final JsonGenerator generator = mapper.getFactory().createGenerator(writer);
		new HeaderSerializer().serialize(header, generator, mapper.getSerializerProviderInstance());
		generator.flush();
		return writer.toString();
	}
}
