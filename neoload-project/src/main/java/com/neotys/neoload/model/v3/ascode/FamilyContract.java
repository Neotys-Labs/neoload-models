package com.neotys.neoload.model.v3.ascode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/** A custom-action family's published as-code contract. The family name is data, not code. */
public final class FamilyContract {
	private final String schemaKey;
	private final String documentation;
	private final JsonNode schema;
	private final Map<String, String> steps;

	private FamilyContract(final String schemaKey, final String documentation, final JsonNode schema, final Map<String, String> steps) {
		this.schemaKey = schemaKey;
		this.documentation = documentation;
		this.schema = schema;
		this.steps = steps;
	}

	public static FamilyContract read(final Path directory) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode manifest = mapper.readTree(directory.resolve("family.json").toFile());
		final String schemaKey = requiredText(manifest, "schemaKey");
		final String documentation = requiredText(manifest, "documentation");
		final String schemaFile = requiredText(manifest, "schema");
		final JsonNode schema = mapper.readTree(directory.resolve(schemaFile).toFile());
		final Map<String, String> steps = new LinkedHashMap<>();
		final Iterator<String> names = manifest.path("steps").fieldNames();
		while (names.hasNext()) {
			final String step = names.next();
			steps.put(step, manifest.path("steps").path(step).asText());
		}
		return new FamilyContract(schemaKey, documentation, schema, steps);
	}

	private static String requiredText(final JsonNode manifest, final String field) {
		final JsonNode value = manifest.get(field);
		if (value == null || value.asText().isBlank()) {
			throw new IllegalArgumentException("Contract manifest requires " + field);
		}
		return value.asText();
	}

	public String schemaKey() {
		return schemaKey;
	}

	public String documentation() {
		return documentation;
	}

	public JsonNode schema() {
		return schema;
	}

	public Map<String, String> steps() {
		return steps;
	}
}
