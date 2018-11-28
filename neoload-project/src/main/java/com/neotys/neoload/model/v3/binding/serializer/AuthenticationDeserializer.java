package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.project.server.Authentication;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegociateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;

import java.io.IOException;

import static com.neotys.neoload.model.v3.project.server.Authentication.BASIC_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Authentication.NEGOCIATE_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Authentication.NTLM_AUTHENTICATION;

public class AuthenticationDeserializer extends StdDeserializer<Authentication> {
	private static final long serialVersionUID = -7910759025327322366L;

	public AuthenticationDeserializer() {
		super(Authentication.class);
	}

	public AuthenticationDeserializer(Class<Authentication> t) {
		super(t);
	}

	@Override
	public Authentication deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode node = codec.readTree(jsonParser);

		return getAuthentication(codec, node);
	}

	private Authentication getAuthentication(final ObjectCodec objectCodec, final JsonNode jsonNode) throws JsonProcessingException {
		JsonNode authentication = jsonNode.get(BASIC_AUTHENTICATION);
		if (authentication != null) {
			return objectCodec.treeToValue(authentication, BasicAuthentication.class);
		}

		authentication = jsonNode.get(NTLM_AUTHENTICATION);
		if (authentication != null) {
			return objectCodec.treeToValue(authentication, NtlmAuthentication.class);
		}

		authentication = jsonNode.get(NEGOCIATE_AUTHENTICATION);
		if (authentication != null) {
			return objectCodec.treeToValue(authentication, NegociateAuthentication.class);
		}

		return null;
	}
}
