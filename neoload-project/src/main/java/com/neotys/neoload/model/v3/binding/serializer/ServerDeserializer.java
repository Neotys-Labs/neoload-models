package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.server.Authentication;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.BASIC_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.NEGOTIATE_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.NTLM_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTPS_PORT;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTP_PORT;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_SCHEME;
import static com.neotys.neoload.model.v3.project.server.Server.HOST;
import static com.neotys.neoload.model.v3.project.server.Server.NAME;
import static com.neotys.neoload.model.v3.project.server.Server.PORT;
import static com.neotys.neoload.model.v3.project.server.Server.SCHEME;

public class ServerDeserializer extends StdDeserializer<Server> {
	private static final long serialVersionUID = 3661407425897246832L;

	protected ServerDeserializer() {
		super(Server.class);
	}

	@Override
	public Server deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		checkMandatoryFieldsForServer(jsonNode);

		final String name = jsonNode.get(NAME).asText();
		final Server.Scheme scheme = getScheme(codec, jsonNode);
		final String host = jsonNode.findValue(HOST).asText();
		final long port = getPort(jsonNode, scheme);

		return Server.builder()
				.name(name)
				.scheme(scheme)
				.host(host)
				.port(port)
				.authentication(getAuthentication(codec, jsonNode))
				.build();
	}

	private void checkMandatoryFieldsForServer(final JsonNode jsonNode) throws IOException {
		for (String field : newArrayList(NAME, HOST)) {
			if (!jsonNode.hasNonNull(field)) {
				throw new IOException("'" + field + "'" + " field is mandatory for Server");
			}
		}
	}

	private long getPort(final JsonNode jsonNode, final Server.Scheme scheme) {
		final JsonNode jsonNodePort = jsonNode.get(PORT);
		if (jsonNodePort != null) {
			return jsonNodePort.asLong();
		}
		return scheme == Server.Scheme.HTTPS ? DEFAULT_HTTPS_PORT : DEFAULT_HTTP_PORT;
	}

	private Server.Scheme getScheme(final ObjectCodec codec, final JsonNode jsonNode) throws JsonProcessingException {
		Server.Scheme scheme = DeserializerHelper.asObject(codec, jsonNode, SCHEME, Server.Scheme.class);
		if (scheme == null) {
			scheme = DEFAULT_SCHEME;
		}
		return scheme;
	}

	private Optional<Authentication> getAuthentication(final ObjectCodec objectCodec, final JsonNode jsonNode) throws IOException {
		checkNoOrOnlyOneAuthentication(jsonNode);

		JsonNode authentication = jsonNode.get(BASIC_AUTHENTICATION);
		if (authentication != null) {
			return Optional.of(objectCodec.treeToValue(authentication, BasicAuthentication.class));
		}

		authentication = jsonNode.get(NTLM_AUTHENTICATION);
		if (authentication != null) {
			return Optional.of(objectCodec.treeToValue(authentication, NtlmAuthentication.class));
		}

		authentication = jsonNode.get(NEGOTIATE_AUTHENTICATION);
		if (authentication != null) {
			return Optional.of(objectCodec.treeToValue(authentication, NegotiateAuthentication.class));
		}

		return Optional.empty();
	}

	private void checkNoOrOnlyOneAuthentication(final JsonNode jsonNode) throws IOException {
		final int authenticationsCount = (jsonNode.has(BASIC_AUTHENTICATION) ? 1 : 0)
				+ (jsonNode.has(NTLM_AUTHENTICATION) ? 1 : 0)
				+ (jsonNode.has(NEGOTIATE_AUTHENTICATION) ? 1 : 0);
		if (authenticationsCount > 1) {
			throw new IOException("Only one authentication can be defined in a Server.");
		}
	}
}
