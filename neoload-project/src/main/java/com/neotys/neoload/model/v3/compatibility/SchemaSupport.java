package com.neotys.neoload.model.v3.compatibility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;

/**
 * Answers the question: "does this neoload-models build know how to parse
 * a YAML declaring {@code schemaVersion} X.Y?".
 *
 * Reads the {@code supported-schemas.json} resource once at startup and
 * exposes an immutable view of the supported versions. Each product
 * (NLG, NLW, neoload-cli, ...) is self-consistent with the list embedded
 * in its bundled neoload-models version — there is no global lookup
 * against the cross-product compatibility matrix from here.
 *
 * The default instance is loaded from the classpath. Tests can build
 * a dedicated instance via {@link #of(SupportedSchemas)} to exercise
 * version selection without touching the resource file.
 *
 * See <a href="https://tricentis.atlassian.net/wiki/spaces/NeoLoad/pages/3429171242">RFC-3</a>
 * and ticket
 * <a href="https://tricentis.atlassian.net/browse/LOAD-37860">LOAD-37860</a>.
 */
public final class SchemaSupport {

	/** Classpath location of the embedded resource. */
	public static final String RESOURCE_PATH = "/supported-schemas.json";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final SchemaSupport DEFAULT_INSTANCE = of(loadFromClasspath());

	private final Set<String> supported;

	private SchemaSupport(final Set<String> supported) {
		this.supported = supported;
	}

	/** Default instance loaded from {@value #RESOURCE_PATH} on the classpath. */
	public static SchemaSupport getDefault() {
		return DEFAULT_INSTANCE;
	}

	/** Build an instance from an explicit data object (test seam). */
	public static SchemaSupport of(final SupportedSchemas data) {
		return new SchemaSupport(ImmutableSet.copyOf(data.getSupported()));
	}

	/** {@code true} if this build can parse YAML declaring the given schema version. */
	public boolean isSupported(final String version) {
		return version != null && supported.contains(version);
	}

	/** Immutable view of supported schema versions. */
	public Set<String> listSupported() {
		return supported;
	}

	private static SupportedSchemas loadFromClasspath() {
		try (InputStream in = SchemaSupport.class.getResourceAsStream(RESOURCE_PATH)) {
			if (in == null) {
				throw new IllegalStateException(
						"Missing classpath resource " + RESOURCE_PATH
						+ " (generated at Maven build time from schemas/vX.Y/).");
			}
			return MAPPER.readValue(in, SupportedSchemas.class);
		} catch (final IOException e) {
			throw new IllegalStateException(
					"Failed to read " + RESOURCE_PATH + " from classpath", e);
		}
	}
}
