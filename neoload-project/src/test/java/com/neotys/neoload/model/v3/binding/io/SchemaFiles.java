package com.neotys.neoload.model.v3.binding.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The as-code JSON schemas of the repository, loaded relative to the module root.
 */
final class SchemaFiles {
	private static final YAMLMapper YAML_MAPPER = new YAMLMapper();

	private final JsonSchema schema;

	private SchemaFiles(final JsonSchema schema) {
		this.schema = schema;
	}

	static SchemaFiles version(final String version) throws URISyntaxException {
		final Path moduleDir = Paths.get(SchemaFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent();
		final Path schemaPath = moduleDir.resolve("../schemas/" + version + "/as-code.schema.json").normalize();
		return new SchemaFiles(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909).getSchema(schemaPath.toUri()));
	}

	boolean accepts(final String yaml) throws IOException {
		final JsonNode node = YAML_MAPPER.readTree(yaml);
		return schema.validate(node).isEmpty();
	}
}
