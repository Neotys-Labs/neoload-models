package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class ProjectSettingsWritterTest {

    @Test
    public void writeSettingsXMLTest() throws IOException {
        File tmpDir = Files.createTempDir();
        Map<String, String> settings = ImmutableMap.<String, String>builder()
                .put("dynatrace.enabled", "true")
                .put("restapi.identification.enabled", "true")
                .put("dynatrace.login", "myLogin")
                .build();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), settings);

        String settingsContent = new String(java.nio.file.Files.readAllBytes(Paths.get(tmpDir.getAbsolutePath(), NeoLoadWriter.ConfigFiles.SETTINGS.getFileName())));
        // Overrides
        Assertions.assertThat(settingsContent).contains("<dynaTraceSettings dynaTraceEnabled=\"true\" logicalNamesEnabled=\"false\" login=\"myLogin\"");
        Assertions.assertThat(settingsContent).contains("<identification isEnabled=\"true\"/>");

        // Defaults
        Assertions.assertThat(settingsContent).contains("port=\"2020\" recordSessionEnabled=\"false\"");

    }
}
