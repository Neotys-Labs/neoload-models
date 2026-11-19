package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.settings.GlobalAssertionsSettings;
import com.neotys.neoload.model.v3.project.settings.ProjectSettings;
import com.neotys.neoload.model.v3.project.settings.QTestSettings;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class ProjectSettingsWritterTest {

    @Test
    public void writeSettingsXMLTest() throws IOException {
        File tmpDir = Files.createTempDir();
        Map<String, String> settings = ImmutableMap.<String, String>builder()
                .put("dynatrace.enabled", "true")
                .put("restapi.identification.enabled", "true")
                .build();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), settings);

        String settingsContent = new String(java.nio.file.Files.readAllBytes(Paths.get(tmpDir.getAbsolutePath(), NeoLoadWriter.ConfigFiles.SETTINGS.getFileName())));
        // Default
        Assertions.assertThat(settingsContent).contains("<dynaTraceSettings dynaTraceEnabled=\"true\" logicalNamesEnabled=\"false\" url=\"\" token=\"\"/>");
        Assertions.assertThat(settingsContent).contains("<identification isEnabled=\"true\"/>");
    }

    @Test
    public void writeSettingsDynatraceHeadersXMLTest() throws IOException {
        File tmpDir = Files.createTempDir();
        Map<String, String> settings = ImmutableMap.<String, String>builder()
                .put("dynatrace.enabled", "true")
                .put("dynatrace.type", "Dynatrace")
                .put("dynatrace.header", "X-Dynatrace-MyHeader(should not be taken into account)")
                .put("dynatrace.url", "http://urlto.dynatrace.com")
                .put("dynatrace.token", "myApiTokenInClear")
                .build();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), settings);

        String settingsContent = new String(java.nio.file.Files.readAllBytes(Paths.get(tmpDir.getAbsolutePath(), NeoLoadWriter.ConfigFiles.SETTINGS.getFileName())));
        // Overrides
        Assertions.assertThat(settingsContent).contains("<dynaTraceSettings dynaTraceEnabled=\"true\" logicalNamesEnabled=\"false\" url=\"http://urlto.dynatrace.com\" token=\"myApiTokenInClear\"/>");
    }

    @Test
    public void writeTypedSettingsXMLTest() throws IOException {
        File tmpDir = java.nio.file.Files.createTempDirectory("project-settings").toFile();
        ProjectSettings settings = ProjectSettings.builder()
                .qtest(QTestSettings.builder()
                        .enabled(true)
                        .url("https://myCompany.qtestnet.com")
                        .authMethod(QTestSettings.AuthMethod.LOGIN_PWD)
                        .projectId(101762L)
                        .cycleId(3761630L)
                        .build())
                .globalAssertions(GlobalAssertionsSettings.builder().contentTypes("application/json").applyOnCustomActions(true).build())
                .build();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), Optional.of(settings));

        String settingsContent = new String(java.nio.file.Files.readAllBytes(Paths.get(tmpDir.getAbsolutePath(), NeoLoadWriter.ConfigFiles.SETTINGS.getFileName()))).replace("\r\n", "\n");
        Assertions.assertThat(settingsContent).contains("<qTestSettings authMethod=\"LOGIN_PWD\" contextTreePathIds=\"3761630\" enable=\"true\" password=\"\" projectId=\"101762\" token=\"\" url=\"https://myCompany.qtestnet.com\" username=\"\"/>");
        Assertions.assertThat(settingsContent).contains("<globalAssertionSettings applyGlobalAssertionsOnCA=\"true\">\n"
                + "<contentType value=\"application/json\"/>\n"
                + "</globalAssertionSettings>");
        // Absent settings keep the defaults
        Assertions.assertThat(settingsContent).contains("<dynaTraceSettings dynaTraceEnabled=\"false\" logicalNamesEnabled=\"false\" url=\"\" token=\"\"/>");
    }

    @Test
    public void writeSettingsGlobalAssertionsDefaultXMLTest() throws IOException {
        File tmpDir = java.nio.file.Files.createTempDirectory("project-settings").toFile();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), ImmutableMap.of());

        String settingsContent = new String(java.nio.file.Files.readAllBytes(Paths.get(tmpDir.getAbsolutePath(), NeoLoadWriter.ConfigFiles.SETTINGS.getFileName()))).replace("\r\n", "\n");
        // Default
        Assertions.assertThat(settingsContent).contains("<globalAssertionSettings applyGlobalAssertionsOnCA=\"false\">\n"
                + "<contentType value=\"text/html\"/>\n"
                + "<contentType value=\"text/xhtml\"/>\n"
                + "</globalAssertionSettings>");
    }

    @Test
    public void writeSettingsGlobalAssertionsXMLTest() throws IOException {
        File tmpDir = java.nio.file.Files.createTempDirectory("project-settings").toFile();
        Map<String, String> settings = ImmutableMap.<String, String>builder()
                .put("global_assertions.content_types", "text/html, application/json,,")
                .put("global_assertions.apply_on_custom_actions", "true")
                .build();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), settings);

        String settingsContent = new String(java.nio.file.Files.readAllBytes(Paths.get(tmpDir.getAbsolutePath(), NeoLoadWriter.ConfigFiles.SETTINGS.getFileName()))).replace("\r\n", "\n");
        // Overrides
        Assertions.assertThat(settingsContent).contains("<globalAssertionSettings applyGlobalAssertionsOnCA=\"true\">\n"
                + "<contentType value=\"text/html\"/>\n"
                + "<contentType value=\"application/json\"/>\n"
                + "</globalAssertionSettings>");
    }
}
