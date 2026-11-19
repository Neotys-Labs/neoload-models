package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.settings.DynatraceSettings;
import com.neotys.neoload.model.v3.project.settings.GlobalAssertionsSettings;
import com.neotys.neoload.model.v3.project.settings.ProjectSettings;
import com.neotys.neoload.model.v3.project.settings.QTestSettings;
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
	public void readDottedProjectSettingsWrittenAsStrings() throws IOException {
		assertRead("test-project-settings-dotted-quoted-values", Project.builder()
				.name("MyProject")
				.projectSettings(ProjectSettings.builder()
						.dynatrace(DynatraceSettings.builder().enabled(true).url("https://abc12345.live.dynatrace.com").build())
						.qtest(QTestSettings.builder()
								.enabled(true)
								.authMethod(QTestSettings.AuthMethod.TOKEN)
								.mappingMode(QTestSettings.MappingMode.PER_USER_PATH)
								.projectId(101762L)
								.cycleId(3761630L)
								.createDefects(false)
								.build())
						.build())
				.build());
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
				assertTrue(e.getMessage(), e.getMessage().contains("apply_on_custom_actions") && e.getMessage().contains("\"yes\""));
			}
		}
	}

	private static GlobalAssertionsSettings getGlobalAssertions() {
		return GlobalAssertionsSettings.builder()
				.contentTypes("text/html,text/xhtml,application/json")
				.applyOnCustomActions(true)
				.build();
	}

	private static Project getProjectWithGlobalAssertionsProjectSettings() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.projectSettings(ProjectSettings.builder().globalAssertions(getGlobalAssertions()).build())
				.build();
	}

	private static Project getProjectWithAllProjectSettings() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.projectSettings(ProjectSettings.builder()
						.dynatrace(DynatraceSettings.builder()
								.enabled(true)
								.url("https://abc12345.live.dynatrace.com")
								.token("myDynatraceToken")
								.build())
						.qtest(QTestSettings.builder()
								.enabled(true)
								.url("https://myCompany.qtestnet.com")
								.token("myQTestToken")
								.username("john.doe")
								.password("myQTestPassword")
								.authMethod(QTestSettings.AuthMethod.TOKEN)
								.projectId(101762L)
								.cycleId(3761630L)
								.createDefects(false)
								.build())
						.globalAssertions(getGlobalAssertions())
						.build())
				.build();
	}

}
