package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.BASIC_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.NEGOTIATE_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication.NTLM_AUTHENTICATION;
import static com.neotys.neoload.model.v3.project.server.Server.AUTHENTICATIONS;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTPS_PORT;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTP_PORT;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_SCHEME;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_TYPE;
import static com.neotys.neoload.model.v3.project.server.Server.HOST;
import static com.neotys.neoload.model.v3.project.server.Server.NAME;
import static com.neotys.neoload.model.v3.project.server.Server.PORT;
import static com.neotys.neoload.model.v3.project.server.Server.SCHEME;
import static com.neotys.neoload.model.v3.project.server.Server.TYPE;
import static com.neotys.neoload.model.v3.project.server.Server.URL_REWRITING;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.Authentication;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.server.ServerAuthentication;
import com.neotys.neoload.model.v3.project.server.UrlRewriting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Reads a server. From schema 3.2 on, an unknown key is rejected, as everywhere else in as-code. Before 3.2 it is
 * ignored, as NeoLoad always did, so that the projects written for those versions still load. The schema version of
 * the file is the {@link #SCHEMA_VERSION_ATTRIBUTE} attribute of the reading, set by {@code IO}. Without it, the server
 * is read as a 3.0 one.
 * <p>
 * The rules that tie several keys together are checked here, while the file is read: one authentication form, the
 * keys a {@code tcp} server accepts, a realm per authentication, and the URL rewriting keys that need {@code in_path}.
 */
public class ServerDeserializer extends StdDeserializer<Server> {

	private static final long serialVersionUID = 3661407425897246832L;

	/** The attribute of the reading that holds the schema version of the file, such as {@code "3.2"}. */
	public static final String SCHEMA_VERSION_ATTRIBUTE = Project.SCHEMA_VERSION;
	private static final int[] FIRST_VERSION_REJECTING_UNKNOWN_KEYS = {3, 2};

	private static final List<String> KNOWN_FIELDS = ImmutableList.of(NAME, TYPE, HOST, SCHEME, PORT,
			BASIC_AUTHENTICATION, NTLM_AUTHENTICATION, NEGOTIATE_AUTHENTICATION, AUTHENTICATIONS, URL_REWRITING);
	private static final List<String> TCP_FIELDS = ImmutableList.of(NAME, TYPE, HOST, PORT);
	private static final List<String> AUTHENTICATION_FIELDS = ImmutableList.of(BASIC_AUTHENTICATION,
			NTLM_AUTHENTICATION, NEGOTIATE_AUTHENTICATION, AUTHENTICATIONS);

	protected ServerDeserializer() {
		super(Server.class);
	}

	@Override
	public Server deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode jsonNode = codec.readTree(jsonParser);

		if (rejectsUnknownKeys(context)) {
			checkKnownFields(jsonParser, jsonNode);
		}
		checkMandatoryFieldsForServer(jsonNode);
		final String name = jsonNode.get(NAME).asText();
		final Server.Type type = DeserializerHelper.asObject(codec, jsonNode, TYPE, Server.Type.class).orElse(DEFAULT_TYPE);
		if (type == Server.Type.TCP) {
			checkTcpServer(name, jsonNode);
		}

		final Server.Scheme scheme = getScheme(codec, jsonNode);
		final Server.Builder builder = Server.builder()
				.type(type)
				.scheme(scheme)
				.port(getPort(jsonNode, scheme))
				.authentication(getAuthentication(name, codec, jsonNode))
				.authentications(getAuthentications(name, codec, jsonNode))
				.urlRewriting(getUrlRewriting(name, codec, jsonNode));
		DeserializerHelper.asText(jsonNode, NAME).ifPresent(builder::name);
		DeserializerHelper.asText(jsonNode, HOST).ifPresent(builder::host);
		return builder.build();
	}

	static boolean rejectsUnknownKeys(final DeserializationContext context) {
		final Object attribute = context.getAttribute(SCHEMA_VERSION_ATTRIBUTE);
		final String version = attribute == null ? Project.DEFAULT_SCHEMA_VERSION : attribute.toString();
		return isAtLeast(version, FIRST_VERSION_REJECTING_UNKNOWN_KEYS);
	}

	/** Compares the numbers of a version such as {@code 3.2} with a minimum. An unreadable version is not at least. */
	static boolean isAtLeast(final String version, final int[] minimum) {
		final String[] parts = version.trim().split("\\.");
		try {
			for (int i = 0; i < minimum.length; i++) {
				final int part = i < parts.length ? Integer.parseInt(parts[i]) : 0;
				if (part != minimum[i]) {
					return part > minimum[i];
				}
			}
			return true;
		} catch (final NumberFormatException e) {
			return false;
		}
	}

	private static void checkKnownFields(final JsonParser jsonParser, final JsonNode jsonNode) throws IOException {
		final Iterator<String> fieldNames = jsonNode.fieldNames();
		while (fieldNames.hasNext()) {
			final String fieldName = fieldNames.next();
			if (!KNOWN_FIELDS.contains(fieldName)) {
				throw UnrecognizedPropertyException.from(jsonParser, Server.class, fieldName, new ArrayList<>(KNOWN_FIELDS));
			}
		}
	}

	private static void checkMandatoryFieldsForServer(final JsonNode jsonNode) throws IOException {
		for (final String field : ImmutableList.of(NAME, HOST)) {
			if (!DeserializerHelper.asText(jsonNode, field).isPresent()) {
				throw new IOException("'" + field + "'" + " field is mandatory for Server");
			}
		}
	}

	private static void checkTcpServer(final String name, final JsonNode jsonNode) throws IOException {
		final Iterator<String> fieldNames = jsonNode.fieldNames();
		while (fieldNames.hasNext()) {
			final String fieldName = fieldNames.next();
			if (!TCP_FIELDS.contains(fieldName)) {
				throw new IOException("The tcp server '" + name + "' only accepts name, type, host and port, not '" + fieldName + "'.");
			}
		}
		if (!DeserializerHelper.asText(jsonNode, PORT).isPresent()) {
			throw new IOException("'port' field is mandatory for the tcp server '" + name + "'.");
		}
	}

	private static String getPort(final JsonNode jsonNode, final Server.Scheme scheme) {
		return DeserializerHelper.asText(jsonNode, PORT).orElse(scheme == Server.Scheme.HTTPS ? DEFAULT_HTTPS_PORT : DEFAULT_HTTP_PORT);
	}

	private static Server.Scheme getScheme(final ObjectCodec codec, final JsonNode jsonNode) throws JsonProcessingException {
		return DeserializerHelper.asObject(codec, jsonNode, SCHEME, Server.Scheme.class).orElse(DEFAULT_SCHEME);
	}

	private static Optional<Authentication> getAuthentication(final String name, final ObjectCodec objectCodec, final JsonNode jsonNode) throws IOException {
		checkNoOrOnlyOneAuthentication(name, jsonNode);
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

	private static void checkNoOrOnlyOneAuthentication(final String name, final JsonNode jsonNode) throws IOException {
		final long authenticationsCount = AUTHENTICATION_FIELDS.stream().filter(jsonNode::has).count();
		if (authenticationsCount > 1) {
			throw new IOException("Only one authentication can be defined in a Server: use either one of "
					+ BASIC_AUTHENTICATION + ", " + NTLM_AUTHENTICATION + ", " + NEGOTIATE_AUTHENTICATION
					+ ", or the " + AUTHENTICATIONS + " list (server '" + name + "').");
		}
	}

	/** Two authentications of a server can't share a realm: NeoLoad keeps only the first one. A missing realm counts as one. */
	private static List<ServerAuthentication> getAuthentications(final String name, final ObjectCodec objectCodec, final JsonNode jsonNode) throws IOException {
		final JsonNode authentications = jsonNode.get(AUTHENTICATIONS);
		if (authentications == null || authentications.isNull()) {
			return ImmutableList.of();
		}
		if (!authentications.isArray()) {
			throw new IOException("'" + AUTHENTICATIONS + "' of the server '" + name + "' must be a list.");
		}
		final List<ServerAuthentication> result = new ArrayList<>();
		final Set<String> realms = new HashSet<>();
		for (final JsonNode item : authentications) {
			final ServerAuthentication authentication = objectCodec.treeToValue(item, ServerAuthentication.class);
			final String realm = authentication.getRealm().orElse("");
			if (!realms.add(realm)) {
				throw new IOException("Two authentications of the server '" + name + "' use the "
						+ (realm.isEmpty() ? "same missing realm" : "realm '" + realm + "'") + ": each authentication needs its own realm.");
			}
			result.add(authentication);
		}
		return result;
	}

	/** {@code path_separator} and {@code equals_sign} only apply to a session id in the path, as in the GUI. */
	private static Optional<UrlRewriting> getUrlRewriting(final String name, final ObjectCodec objectCodec, final JsonNode jsonNode) throws IOException {
		final JsonNode urlRewriting = jsonNode.get(URL_REWRITING);
		if (urlRewriting == null || urlRewriting.isNull()) {
			return Optional.empty();
		}
		final boolean inPath = urlRewriting.path(UrlRewriting.IN_PATH).asBoolean(false);
		for (final String field : ImmutableList.of(UrlRewriting.PATH_SEPARATOR, UrlRewriting.EQUALS_SIGN)) {
			if (!inPath && urlRewriting.has(field)) {
				throw new IOException("'" + field + "' of the URL rewriting of the server '" + name + "' needs '"
						+ UrlRewriting.IN_PATH + ": true'.");
			}
		}
		return Optional.of(objectCodec.treeToValue(urlRewriting, UrlRewriting.class));
	}
}
