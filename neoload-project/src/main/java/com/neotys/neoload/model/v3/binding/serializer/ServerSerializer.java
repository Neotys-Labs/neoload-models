package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.BASIC_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.NEGOTIATE_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.NTLM_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Server.AUTHENTICATIONS;
import static com.neotys.neoload.model.v3.project.server.Server.HOST;
import static com.neotys.neoload.model.v3.project.server.Server.NAME;
import static com.neotys.neoload.model.v3.project.server.Server.PORT;
import static com.neotys.neoload.model.v3.project.server.Server.SCHEME;
import static com.neotys.neoload.model.v3.project.server.Server.TYPE;
import static com.neotys.neoload.model.v3.project.server.Server.URL_REWRITING;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.server.Authentication;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.server.ServerAuthentication;
import com.neotys.neoload.model.v3.project.server.UrlRewriting;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Serializes a {@link Server}, writing the authentication under its wrapper key
 * ({@code basic_authentication}, {@code ntlm_authentication} or {@code negotiate_authentication}),
 * mirroring {@link ServerDeserializer}. The server description has no as-code key: it is not written.
 */
public final class ServerSerializer extends StdSerializer<Server> {
	private static final long serialVersionUID = 3661407425897246833L;

	public ServerSerializer() {
		super(Server.class);
	}

	@Override
	public void serialize(final Server server, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartObject();
		SerializerHelper.writeStringIfPresent(generator, NAME, server.getName());
		final boolean tcp = server.getType() == Server.Type.TCP;
		if (tcp) {
			generator.writeObjectField(TYPE, server.getType());
		}
		SerializerHelper.writeStringIfPresent(generator, HOST, server.getHost());

		if (tcp) {
			// A tcp server has no scheme, hence no default port.
			SerializerHelper.writeStringIfPresent(generator, PORT, server.getPort());
			generator.writeEndObject();
			return;
		}

		// Port and scheme are written only when they differ from their defaults (the default port
		// depends on the scheme: 80 for http, 443 for https), mirroring ServerDeserializer.
		final Server.Scheme scheme = server.getScheme();
		if (scheme != Server.DEFAULT_SCHEME) {
			generator.writeObjectField(SCHEME, scheme);
		}
		if (!isDefaultPort(server.getPort(), scheme)) {
			SerializerHelper.writeStringIfPresent(generator, PORT, server.getPort());
		}

		final Optional<Authentication> authentication = server.getAuthentication();
		if (authentication.isPresent()) {
			generator.writeObjectField(authenticationKey(authentication.get()), authentication.get());
		}
		final List<ServerAuthentication> authentications = server.getAuthentications();
		if (!authentications.isEmpty()) {
			generator.writeObjectField(AUTHENTICATIONS, authentications);
		}
		final Optional<UrlRewriting> urlRewriting = server.getUrlRewriting();
		if (urlRewriting.isPresent()) {
			generator.writeObjectField(URL_REWRITING, urlRewriting.get());
		}
		generator.writeEndObject();
	}

	private static boolean isDefaultPort(final String port, final Server.Scheme scheme) {
		final String defaultPort = (scheme == Server.Scheme.HTTPS) ? Server.DEFAULT_HTTPS_PORT : Server.DEFAULT_HTTP_PORT;
		return defaultPort.equals(port);
	}

	private static String authenticationKey(final Authentication authentication) {
		if (authentication instanceof BasicAuthentication) {
			return BASIC_AUTHENTICATION;
		}
		if (authentication instanceof NtlmAuthentication) {
			return NTLM_AUTHENTICATION;
		}
		if (authentication instanceof NegotiateAuthentication) {
			return NEGOTIATE_AUTHENTICATION;
		}
		throw new IllegalArgumentException("Unsupported authentication type: " + authentication.getClass().getName());
	}
}
