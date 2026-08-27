package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neotys.neoload.model.v3.project.Project;
import org.junit.Test;

public class IOSchemaVersionFailFastTest {

	private static final String UNSUPPORTED_YAML =
			"schemaVersion: \"99.0\"\n"
			+ "name: future\n"
			+ "unknownFutureConstruct: true\n";

	private static final String UNSUPPORTED_JSON =
			"{\"schemaVersion\":\"99.0\",\"name\":\"future\",\"unknownFutureConstruct\":true}";

	private final IO mapper = new IO();

	@Test
	public void project_descriptor_yaml_rejects_schema_version_before_unknown_fields() {
		final UnsupportedSchemaVersionException exception = assertThrows(
				UnsupportedSchemaVersionException.class,
				() -> mapper.read(UNSUPPORTED_YAML));

		assertUnsupportedVersion(exception);
	}

	@Test
	public void project_descriptor_json_rejects_schema_version_before_unknown_fields() {
		final UnsupportedSchemaVersionException exception = assertThrows(
				UnsupportedSchemaVersionException.class,
				() -> mapper.read(UNSUPPORTED_JSON));

		assertUnsupportedVersion(exception);
	}

	@Test
	public void generic_project_descriptor_read_applies_fail_fast_check() {
		final UnsupportedSchemaVersionException exception = assertThrows(
				UnsupportedSchemaVersionException.class,
				() -> mapper.read(UNSUPPORTED_YAML, ProjectDescriptor.class));

		assertUnsupportedVersion(exception);
	}

	@Test
	public void generic_project_read_applies_fail_fast_check() {
		final UnsupportedSchemaVersionException exception = assertThrows(
				UnsupportedSchemaVersionException.class,
				() -> mapper.read(UNSUPPORTED_YAML, Project.class));

		assertUnsupportedVersion(exception);
	}

	@Test
	public void missing_schema_version_uses_legacy_default() throws Exception {
		final ProjectDescriptor descriptor = mapper.read("name: legacy\n");

		assertEquals(Project.DEFAULT_SCHEMA_VERSION, descriptor.getProject().getSchemaVersion());
	}

	@Test
	public void malformed_yaml_preserves_jackson_parse_error() {
		assertThrows(JsonProcessingException.class,
				() -> mapper.read("schemaVersion: \"3.0\"\nname: ["));
	}

	private static void assertUnsupportedVersion(final UnsupportedSchemaVersionException exception) {
		assertEquals("99.0", exception.getDeclaredSchemaVersion());
		assertTrue(exception.getSupportedSchemaVersions().contains("3.0"));
		assertTrue(exception.getSupportedSchemaVersions().contains("3.1"));
		assertTrue(exception.getMessage().contains("99.0"));
		assertTrue(exception.getMessage().contains("compatibility.json"));
	}
}
