package com.neotys.neoload.model.writers.neoload.settings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.common.io.Files;
import com.neotys.neoload.model.Project;

public class ProjectSettingsWriterTest {

	@Test
	public void testWriteSettingsXMLWithoutOverwrite() throws IOException {
		final Project project = Project.builder()
                .name("Test project")
                .build();
		File tmpDir = Files.createTempDir();       
		ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), project.getProjectSettings());
		final String settingsXMLContent = readFile(tmpDir + File.separator + "settings.xml");
		Assertions.assertThat(settingsXMLContent).doesNotContain("@");
		Assertions.assertThat(settingsXMLContent).doesNotContain("dynaTraceEnabled=\"true\"");
		Assertions.assertThat(settingsXMLContent).contains("dynaTraceEnabled=\"false\"");
	}
	
	@Test
	public void testWriteSettingsXMLWithOverwrite() throws IOException {
		final Project project = Project.builder()
                .name("Test project")
                .putProjectSettings("dynatrace.enabled", "true")
                .build();
		File tmpDir = Files.createTempDir();       
		ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), project.getProjectSettings());
		final String settingsXMLContent = readFile(tmpDir + File.separator + "settings.xml");
		Assertions.assertThat(settingsXMLContent).doesNotContain("@");
		Assertions.assertThat(settingsXMLContent).doesNotContain("dynaTraceEnabled=\"false\"");
		Assertions.assertThat(settingsXMLContent).contains("dynaTraceEnabled=\"true\"");
	}
	
	public static String readFile(final String file) throws IOException {
		return com.google.common.io.Files.asCharSource(new File(file), Charset.defaultCharset()).read();
	}

}
