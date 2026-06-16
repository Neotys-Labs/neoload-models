package com.neotys.neoload.model.v3.compatibility;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

/**
 * Jackson POJO mirroring the {@code supported-schemas.json} resource embedded
 * in this neoload-models build. The file lists only the schema versions the
 * current build can parse.
 *
 * Generated at Maven build time (later phase) from the {@code schemas/vX.Y/}
 * directory listing at the repo root. For now the file is maintained manually.
 *
 * See <a href="https://tricentis.atlassian.net/wiki/spaces/NeoLoad/pages/3429171242">RFC-3</a>.
 */
public final class SupportedSchemas {

	private final List<String> supported;

	@JsonCreator
	public SupportedSchemas(@JsonProperty("supported") final List<String> supported) {
		this.supported = supported == null ? ImmutableList.of() : ImmutableList.copyOf(supported);
	}

	public List<String> getSupported() {
		return supported;
	}
}
