package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class IOProjectSettingsTest extends AbstractIOElementsTest {

	@Test
	public void readGlobalAssertionsProjectSettings() throws IOException {
		assertRead("test-project-settings-global-assertions", getProjectWithGlobalAssertionsProjectSettings());
	}

	@Test
	public void writeGlobalAssertionsProjectSettings() throws IOException {
		assertWrite("test-project-settings-global-assertions", getProjectWithGlobalAssertionsProjectSettings());
	}

	@Test
	public void readNestedProjectSettings() throws IOException {
		assertRead("test-project-settings-nested", getProjectWithAllProjectSettings());
	}

	@Test
	public void writeProjectSettingsAsNestedMaps() throws IOException {
		assertWrite("test-project-settings-nested", getProjectWithAllProjectSettings());
	}

	@Test
	public void readDeprecatedDottedProjectSettings() throws IOException {
		assertRead("test-project-settings-dotted-deprecated", getProjectWithAllProjectSettings());
	}

	@Test
	public void writeNeverEmitsDeprecatedDottedKeys() throws IOException {
		final IO io = new IO();
		for (final Format format : Format.values()) {
			final String written = io.write(getProjectWithAllProjectSettings(), format);
			for (final String dottedKey : new String[] { "dynatrace.enabled", "qtest.enabled", "global_assertions.content_types" }) {
				assertFalse(format + " must not contain " + dottedKey + ": " + written, written.contains(dottedKey));
			}
		}
	}

	@Test
	public void readBothProjectSettingFormsFails() throws IOException {
		for (final String extension : new String[] { "yaml", "json" }) {
			final String content = getContent(getFile("test-project-settings-keys-conflict", extension), StandardCharsets.UTF_8);
			try {
				new IO().read(content);
				fail("Reading the " + extension + " fixture must fail");
			} catch (final JsonMappingException e) {
				assertTrue(e.getMessage(), e.getMessage().contains("qtest.enabled") && e.getMessage().contains("nested"));
			}
		}
	}

	@Test
	public void readDottedGlobalAssertionsProjectSettingFails() throws IOException {
		for (final String extension : new String[] { "yaml", "json" }) {
			final String content = getContent(getFile("test-project-settings-global-assertions-dotted", extension), StandardCharsets.UTF_8);
			try {
				new IO().read(content);
				fail("Reading the " + extension + " fixture must fail");
			} catch (final JsonMappingException e) {
				assertTrue(e.getMessage(), e.getMessage().contains("global_assertions.content_types")
						&& e.getMessage().contains("global_assertions: content_types"));
			}
		}
	}

	@Test
	public void readNonBooleanApplyGlobalAssertionsOnCustomActionsFails() throws IOException {
		for (final String extension : new String[] { "yaml", "json" }) {
			final String content = getContent(getFile("test-project-settings-global-assertions-invalid", extension), StandardCharsets.UTF_8);
			try {
				new IO().read(content);
				fail("Reading the " + extension + " fixture must fail");
			} catch (final JsonMappingException e) {
				assertTrue(e.getMessage(), e.getMessage().contains("global_assertions.apply_on_custom_actions")
						&& e.getMessage().contains("must be a boolean"));
			}
		}
	}

	private static Project getProjectWithGlobalAssertionsProjectSettings() {
		return Project.builder()
				.name("MyProject")
				.putProjectSettings("global_assertions.content_types", "text/html,text/xhtml,application/json")
				.putProjectSettings("global_assertions.apply_on_custom_actions", "true")
				.build();
	}

	private static Project getProjectWithAllProjectSettings() {
		return Project.builder()
				.name("MyProject")
				.putProjectSettings("dynatrace.enabled", "true")
				.putProjectSettings("dynatrace.url", "https://abc12345.live.dynatrace.com")
				.putProjectSettings("dynatrace.token", "myDynatraceToken")
				.putProjectSettings("qtest.enabled", "true")
				.putProjectSettings("qtest.url", "https://myCompany.qtestnet.com")
				.putProjectSettings("qtest.token", "myQTestToken")
				.putProjectSettings("qtest.username", "john.doe")
				.putProjectSettings("qtest.password", "myQTestPassword")
				.putProjectSettings("qtest.auth_method", "TOKEN")
				.putProjectSettings("qtest.project_id", "101762")
				.putProjectSettings("qtest.cycle_id", "3761630")
				.putProjectSettings("qtest.create_defects", "false")
				.putProjectSettings("global_assertions.content_types", "text/html,text/xhtml,application/json")
				.putProjectSettings("global_assertions.apply_on_custom_actions", "true")
				.build();
	}

}
