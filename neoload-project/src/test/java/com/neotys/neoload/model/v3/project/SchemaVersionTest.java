package com.neotys.neoload.model.v3.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import org.junit.Test;

/**
 * Behavioral tests for the {@code schemaVersion} field on {@link Project}.
 *
 * Locks down:
 * <ul>
 *   <li>default value when absent in YAML ({@value Project#DEFAULT_SCHEMA_VERSION})</li>
 *   <li>round-trip of a legacy YAML (no {@code schemaVersion}) is non-polluting</li>
 *   <li>non-default version is serialized and round-trips losslessly</li>
 *   <li>explicit default version round-trips to implicit (acceptable loss)</li>
 *   <li>{@code schemaVersion} appears first in serialization order</li>
 * </ul>
 *
 * See <a href="https://tricentis.atlassian.net/wiki/spaces/NeoLoad/pages/3429171242">RFC-3</a>
 * and ticket
 * <a href="https://tricentis.atlassian.net/browse/LOAD-37860">LOAD-37860</a>.
 */
public class SchemaVersionTest {

	private static final IO IO_MAPPER = new IO();

	private static Project readYaml(final String yaml) throws Exception {
		return IO_MAPPER.read(yaml, Project.class);
	}

	private static String writeYaml(final Project project) throws Exception {
		return IO_MAPPER.write(project, Format.YAML);
	}

	private static String writeJson(final Project project) throws Exception {
		return IO_MAPPER.write(project, Format.JSON);
	}

	// ----- Builder defaults -----

	@Test
	public void builder_defaults_to_3_0_when_not_set() {
		final Project project = Project.builder().name("foo").build();
		assertEquals("3.0", project.getSchemaVersion());
	}

	@Test
	public void builder_keeps_explicit_value() {
		final Project project = Project.builder()
				.name("foo")
				.schemaVersion("3.1")
				.build();
		assertEquals("3.1", project.getSchemaVersion());
	}

	// ----- Read (deserialization) -----

	@Test
	public void read_yaml_without_schemaVersion_returns_default() throws Exception {
		final Project project = readYaml("name: foo\n");
		assertEquals("3.0", project.getSchemaVersion());
	}

	@Test
	public void read_yaml_with_explicit_3_0_returns_3_0() throws Exception {
		final Project project = readYaml("schemaVersion: \"3.0\"\nname: foo\n");
		assertEquals("3.0", project.getSchemaVersion());
	}

	@Test
	public void read_yaml_with_3_1_returns_3_1() throws Exception {
		final Project project = readYaml("schemaVersion: \"3.1\"\nname: foo\n");
		assertEquals("3.1", project.getSchemaVersion());
	}

	@Test
	public void read_yaml_with_unquoted_3_0_coerces_to_string() throws Exception {
		// YAML 1.1 parses bare "3.0" as a float; Jackson coerces to the target String field.
		final Project project = readYaml("schemaVersion: 3.0\nname: foo\n");
		assertEquals("3.0", project.getSchemaVersion());
	}

	// ----- Write (serialization) -----

	@Test
	public void write_default_3_0_is_omitted_from_yaml() throws Exception {
		final Project project = Project.builder().name("foo").build();
		final String yaml = writeYaml(project);
		assertFalse("default schemaVersion should not leak into YAML: " + yaml,
				yaml.contains("schemaVersion"));
	}

	@Test
	public void write_non_default_appears_in_yaml() throws Exception {
		final Project project = Project.builder().name("foo").schemaVersion("3.1").build();
		final String yaml = writeYaml(project);
		assertTrue("non-default schemaVersion should appear in YAML: " + yaml,
				yaml.contains("schemaVersion"));
		assertTrue("value 3.1 should be present: " + yaml, yaml.contains("3.1"));
	}

	@Test
	public void write_default_3_0_is_omitted_from_json() throws Exception {
		final Project project = Project.builder().name("foo").build();
		final String json = writeJson(project);
		assertFalse("default schemaVersion should not leak into JSON: " + json,
				json.contains("schemaVersion"));
	}

	// ----- Round-trip -----

	@Test
	public void roundtrip_legacy_yaml_does_not_introduce_schemaVersion() throws Exception {
		final String input = "name: foo\n";
		final Project project = readYaml(input);
		final String output = writeYaml(project);
		assertFalse("round-trip must not introduce schemaVersion in legacy YAML: " + output,
				output.contains("schemaVersion"));
	}

	@Test
	public void roundtrip_3_1_is_lossless() throws Exception {
		final String input = "schemaVersion: \"3.1\"\nname: foo\n";
		final Project project = readYaml(input);
		final String output = writeYaml(project);
		assertTrue("3.1 should still be present after round-trip: " + output,
				output.contains("schemaVersion") && output.contains("3.1"));
	}

	// ----- Bean Validation integration (@ValidSchemaVersion) -----

	private static final Validator BEAN_VALIDATOR = new Validator();

	@Test
	public void bean_validation_passes_for_default_schemaVersion() {
		final Project project = Project.builder().name("foo").build();
		final Validation validation = BEAN_VALIDATOR.validate(project, NeoLoad.class);
		assertTrue("default 3.0 must pass: " + validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void bean_validation_passes_for_explicit_supported_schemaVersion() {
		final Project project = Project.builder().name("foo").schemaVersion("3.0").build();
		final Validation validation = BEAN_VALIDATOR.validate(project, NeoLoad.class);
		assertTrue("explicit 3.0 must pass: " + validation.getMessage().orElse(""), validation.isValid());
	}

	@Test
	public void bean_validation_fails_for_unsupported_schemaVersion() {
		final Project project = Project.builder().name("foo").schemaVersion("3.999").build();
		final Validation validation = BEAN_VALIDATOR.validate(project, NeoLoad.class);
		final String message = validation.getMessage().orElse("");
		assertFalse("3.999 must not pass: " + message, validation.isValid());
		assertTrue("violation must reference schemaVersion: " + message,
				message.contains("schemaVersion") || message.contains("schema_version"));
	}

	// ----- Property order -----

	@Test
	public void schemaVersion_appears_before_name_when_serialized() throws Exception {
		final Project project = Project.builder().name("foo").schemaVersion("3.1").build();
		final String yaml = writeYaml(project);
		final int schemaIdx = yaml.indexOf("schemaVersion");
		final int nameIdx = yaml.indexOf("name");
		assertTrue("schemaVersion must appear before name: " + yaml,
				schemaIdx >= 0 && schemaIdx < nameIdx);
	}
}
