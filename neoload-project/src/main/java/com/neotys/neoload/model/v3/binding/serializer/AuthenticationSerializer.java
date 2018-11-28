package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.server.Authentication;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegociateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;

import java.io.IOException;

import static com.neotys.neoload.model.v3.project.server.Authentication.BASIC_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Authentication.NEGOCIATE_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Authentication.NTLM_AUTHENTICATION;

public class AuthenticationSerializer extends StdSerializer<Authentication> {

	private static final long serialVersionUID = -2705308321121458973L;

	public AuthenticationSerializer() {
		super(Authentication.class);
	}

	@Override
	public void serialize(final Authentication authentication, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeStartObject();

		if (authentication instanceof BasicAuthentication) {
			jsonGenerator.writeObjectField(BASIC_AUTHENTICATION, authentication);
		} else if (authentication instanceof NtlmAuthentication) {
			jsonGenerator.writeObjectField(NTLM_AUTHENTICATION, authentication);
		} else if (authentication instanceof NegociateAuthentication) {
			jsonGenerator.writeObjectField(NEGOCIATE_AUTHENTICATION, authentication);
		}

		jsonGenerator.writeEndObject();
	}
}
