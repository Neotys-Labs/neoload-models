package com.neotys.neoload.model.v3.binding.serializer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;


public class ServerSerializerTest {

	@Test
	public void serializeWithoutAuthentication() throws IOException {
		// Default scheme (http) and default port (80 for http) are omitted.
		assertEquals("{\"name\":\"myserver\",\"host\":\"localhost\"}",
				serialize(Server.builder().name("myserver").host("localhost").port("80").build()));
	}

	@Test
	public void serializeWithBasicAuthentication() throws IOException {
		// https is not the default scheme so it is written; 443 is the default port for https so it is omitted.
		assertEquals("{\"name\":\"myserver\",\"host\":\"localhost\",\"scheme\":\"https\","
						+ "\"basic_authentication\":{\"login\":\"user\",\"password\":\"pwd\",\"realm\":\"realm-value\"}}",
				serialize(Server.builder()
						.name("myserver").host("localhost").port("443").scheme(Server.Scheme.HTTPS)
						.authentication(BasicAuthentication.builder().login("user").password("pwd").realm("realm-value").build())
						.build()));
	}

	@Test
	public void serializeWritesNonDefaultPort() throws IOException {
		// A non-default port is written; the default scheme (http) is still omitted.
		assertEquals("{\"name\":\"myserver\",\"host\":\"localhost\",\"port\":\"8080\"}",
				serialize(Server.builder().name("myserver").host("localhost").port("8080").build()));
	}

	@Test
	public void serializeWritesNonDefaultPortForHttps() throws IOException {
		assertEquals("{\"name\":\"myserver\",\"host\":\"localhost\",\"scheme\":\"https\",\"port\":\"8443\"}",
				serialize(Server.builder().name("myserver").host("localhost").scheme(Server.Scheme.HTTPS).port("8443").build()));
	}

	@Test
	public void serializeWithNtlmAuthentication() throws IOException {
		assertEquals("{\"name\":\"myserver\",\"host\":\"localhost\","
						+ "\"ntlm_authentication\":{\"login\":\"user\",\"password\":\"pwd\"}}",
				serialize(Server.builder()
						.name("myserver").host("localhost").port("80")
						.authentication(NtlmAuthentication.builder().login("user").password("pwd").build())
						.build()));
	}

	@Test
	public void serializeWithNegotiateAuthentication() throws IOException {
		assertEquals("{\"name\":\"myserver\",\"host\":\"localhost\","
						+ "\"negotiate_authentication\":{\"login\":\"user\",\"password\":\"pwd\",\"domain\":\"mydomain\"}}",
				serialize(Server.builder()
						.name("myserver").host("localhost").port("80")
						.authentication(NegotiateAuthentication.builder().login("user").password("pwd").domain("mydomain").build())
						.build()));
	}

	private static String serialize(final Server server) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new GuavaModule());
		final Jdk8Module jdk8Module = new Jdk8Module();
		jdk8Module.configureAbsentsAsNulls(true);
		mapper.registerModule(jdk8Module);

		final StringWriter writer = new StringWriter();
		final JsonGenerator generator = mapper.getFactory().createGenerator(writer);
		new ServerSerializer().serialize(server, generator, mapper.getSerializerProviderInstance());
		generator.flush();
		return writer.toString();
	}
}
