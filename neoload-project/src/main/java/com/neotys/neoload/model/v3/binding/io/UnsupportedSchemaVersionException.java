package com.neotys.neoload.model.v3.binding.io;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Raised before project binding when the input declares a schema version
 * that is not supported by the current neoload-models artifact.
 */
public final class UnsupportedSchemaVersionException extends IOException {

	private static final long serialVersionUID = 1L;

	private static final String COMPATIBILITY_MATRIX_URL =
			"https://raw.githubusercontent.com/Neotys-Labs/neoload-models/v3/schemas/compatibility.json";

	private final String declaredSchemaVersion;
	private final Set<String> supportedSchemaVersions;

	UnsupportedSchemaVersionException(final String declaredSchemaVersion,
			final Set<String> supportedSchemaVersions) {
		super(buildMessage(declaredSchemaVersion, supportedSchemaVersions));
		this.declaredSchemaVersion = declaredSchemaVersion;
		this.supportedSchemaVersions = Collections.unmodifiableSet(
				new LinkedHashSet<>(supportedSchemaVersions));
	}

	public String getDeclaredSchemaVersion() {
		return declaredSchemaVersion;
	}

	public Set<String> getSupportedSchemaVersions() {
		return supportedSchemaVersions;
	}

	private static String buildMessage(final String declaredSchemaVersion,
			final Set<String> supportedSchemaVersions) {
		return "Schema version '" + declaredSchemaVersion
				+ "' is not supported by this product version. Supported schema versions: ["
				+ String.join(", ", supportedSchemaVersions)
				+ "]. See " + COMPATIBILITY_MATRIX_URL + " for product compatibility information.";
	}
}
