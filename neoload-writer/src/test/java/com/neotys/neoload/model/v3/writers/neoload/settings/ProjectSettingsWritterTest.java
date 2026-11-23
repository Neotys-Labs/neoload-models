package com.neotys.neoload.model.v3.writers.neoload.settings;

import static org.assertj.core.api.Assertions.assertThat;

import com.neotys.neoload.model.v3.project.settings.ContentAssertionsSettings;
import com.neotys.neoload.model.v3.project.settings.DynatraceSettings;
import com.neotys.neoload.model.v3.project.settings.ProjectSettings;
import com.neotys.neoload.model.v3.project.settings.QTestSettings;
import com.neotys.neoload.model.v3.project.settings.ResponsesSettings;
import com.neotys.neoload.model.v3.project.settings.RuntimeSettings;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProjectSettingsWritterTest {

    @Test
    public void writeDefaultSettingsXMLTest() throws Exception {
        final File settingsFile = write(Optional.empty());
        final Document document = parse(settingsFile);

        assertThat(new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8).trim()).startsWith("<!DOCTYPE settings [");
        assertThat(document.getDoctype()).isNotNull();
        final Element dynatrace = element(document, "dynaTraceSettings");
        assertThat(dynatrace.getAttribute("dynaTraceEnabled")).isEqualTo("false");
        assertThat(dynatrace.getAttribute("url")).isEmpty();
        assertThat(dynatrace.getAttribute("token")).isEmpty();
        final Element qtest = element(document, "qTestSettings");
        assertThat(qtest.getAttribute("enable")).isEqualTo("false");
        assertThat(qtest.getAttribute("authMethod")).isEqualTo("TOKEN");
        final Element globalAssertions = element(document, "globalAssertionSettings");
        assertThat(globalAssertions.getAttribute("applyGlobalAssertionsOnCA")).isEqualTo("false");
        assertThat(contentTypes(globalAssertions)).containsExactly("text/html", "text/xhtml");
        assertThat(element(document, "identification").getAttribute("isEnabled")).isEqualTo("false");
        // The written elements are at their place in the DTD order of the settings children
        assertThat(childElementNames(document.getDocumentElement())).containsSubsequence("errorHandleSettings",
                "globalAssertionSettings", "caApmSettings", "dynaTraceSettings", "appDynamicsSettings", "nudgeSettings",
                "qTestSettings", "amfSettings");
    }

    @Test
    public void templateHoldsOnlyFixedSettingsTest() throws IOException {
        final String template;
        try (InputStream in = ProjectSettingsWriter.class.getResourceAsStream(NeoLoadWriter.ConfigFiles.SETTINGS.getFileName())) {
            template = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        final String body = template.substring(template.indexOf("<settings>"));

        // These elements are written by the Java writers, with the defaults of the as-code model
        assertThat(body).doesNotContain("<globalAssertionSettings", "<dynaTraceSettings", "<qTestSettings");
    }

    @Test
    public void writeDynatraceSettingsXMLTest() throws Exception {
        final ProjectSettings settings = ProjectSettings.builder()
                .dynatrace(DynatraceSettings.builder().enabled(true).url("http://urlto.dynatrace.com").token("myApiTokenInClear").build())
                .build();
        final Document document = parse(write(Optional.of(settings)));

        final Element dynatrace = element(document, "dynaTraceSettings");
        assertThat(dynatrace.getAttribute("dynaTraceEnabled")).isEqualTo("true");
        assertThat(dynatrace.getAttribute("logicalNamesEnabled")).isEqualTo("false");
        assertThat(dynatrace.getAttribute("url")).isEqualTo("http://urlto.dynatrace.com");
        assertThat(dynatrace.getAttribute("token")).isEqualTo("myApiTokenInClear");
    }

    @Test
    public void writeQTestAndGlobalAssertionSettingsXMLTest() throws Exception {
        final ProjectSettings settings = ProjectSettings.builder()
                .qtest(QTestSettings.builder()
                        .enabled(true)
                        .url("https://myCompany.qtestnet.com")
                        .authMethod(QTestSettings.AuthMethod.LOGIN_PWD)
                        .projectId(101762L)
                        .cycleId(3761630L)
                        .build())
                .runtime(runtime(ContentAssertionsSettings.builder().contentTypes("application/json").applyOnCustomActions(true).build()))
                .build();
        final Document document = parse(write(Optional.of(settings)));

        final Element qtest = element(document, "qTestSettings");
        assertThat(qtest.getAttribute("enable")).isEqualTo("true");
        assertThat(qtest.getAttribute("url")).isEqualTo("https://myCompany.qtestnet.com");
        assertThat(qtest.getAttribute("authMethod")).isEqualTo("LOGIN_PWD");
        assertThat(qtest.getAttribute("projectId")).isEqualTo("101762");
        assertThat(qtest.getAttribute("contextTreePathIds")).isEqualTo("3761630");
        assertThat(qtest.getAttribute("username")).isEmpty();
        final Element globalAssertions = element(document, "globalAssertionSettings");
        assertThat(globalAssertions.getAttribute("applyGlobalAssertionsOnCA")).isEqualTo("true");
        assertThat(contentTypes(globalAssertions)).containsExactly("application/json");
        // Absent settings keep the defaults
        assertThat(element(document, "dynaTraceSettings").getAttribute("dynaTraceEnabled")).isEqualTo("false");
    }

    @Test
    public void writeGlobalAssertionContentTypesAreTrimmedXMLTest() throws Exception {
        final ProjectSettings settings = ProjectSettings.builder()
                .runtime(runtime(ContentAssertionsSettings.builder().contentTypes("text/html, application/json,,").build()))
                .build();
        final Document document = parse(write(Optional.of(settings)));

        final Element globalAssertions = element(document, "globalAssertionSettings");
        assertThat(contentTypes(globalAssertions)).containsExactly("text/html", "application/json");
        assertThat(globalAssertions.getAttribute("applyGlobalAssertionsOnCA")).isEqualTo("false");
    }

    @Test
    public void writeEmptyGlobalAssertionContentTypesXMLTest() throws Exception {
        final ProjectSettings settings = ProjectSettings.builder()
                .runtime(runtime(ContentAssertionsSettings.builder().contentTypes("").build()))
                .build();
        final Document document = parse(write(Optional.of(settings)));

        assertThat(contentTypes(element(document, "globalAssertionSettings"))).isEmpty();
    }

    @Test
    public void writeApplyOnCustomActionsOnlyKeepsDefaultContentTypesXMLTest() throws Exception {
        final ProjectSettings settings = ProjectSettings.builder()
                .runtime(runtime(ContentAssertionsSettings.builder().applyOnCustomActions(true).build()))
                .build();
        final Document document = parse(write(Optional.of(settings)));

        final Element globalAssertions = element(document, "globalAssertionSettings");
        assertThat(globalAssertions.getAttribute("applyGlobalAssertionsOnCA")).isEqualTo("true");
        assertThat(contentTypes(globalAssertions)).containsExactly("text/html", "text/xhtml");
    }

    @Test
    public void writeSettingsWithXMLSpecialCharactersXMLTest() throws Exception {
        final String token = "a&b\"c<d>e'f";
        final ProjectSettings settings = ProjectSettings.builder()
                .dynatrace(DynatraceSettings.builder().token(token).build())
                .qtest(QTestSettings.builder().password(token).build())
                .build();
        final Document document = parse(write(Optional.of(settings)));

        assertThat(element(document, "dynaTraceSettings").getAttribute("token")).isEqualTo(token);
        assertThat(element(document, "qTestSettings").getAttribute("password")).isEqualTo(token);
    }

    private static RuntimeSettings runtime(final ContentAssertionsSettings contentAssertions) {
        return RuntimeSettings.builder().responses(ResponsesSettings.builder().contentAssertions(contentAssertions).build()).build();
    }

    private static File write(final Optional<ProjectSettings> settings) throws IOException {
        final File tmpDir = Files.createTempDirectory("project-settings").toFile();
        ProjectSettingsWriter.writeSettingsXML(tmpDir.getAbsolutePath(), settings);
        return new File(tmpDir, NeoLoadWriter.ConfigFiles.SETTINGS.getFileName());
    }

    private static Document parse(final File settingsFile) throws ParserConfigurationException, SAXException, IOException {
        // The whole file, DTD included, must be well-formed XML
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(settingsFile);
    }

    private static Element element(final Document document, final String tagName) {
        final NodeList elements = document.getElementsByTagName(tagName);
        assertThat(elements.getLength()).as(tagName).isEqualTo(1);
        return (Element) elements.item(0);
    }

    private static List<String> childElementNames(final Element parent) {
        final List<String> names = new ArrayList<>();
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                names.add(child.getNodeName());
            }
        }
        return names;
    }

    private static List<String> contentTypes(final Element globalAssertions) {
        final NodeList elements = globalAssertions.getElementsByTagName("contentType");
        final List<String> values = new ArrayList<>();
        for (int i = 0; i < elements.getLength(); i++) {
            values.add(((Element) elements.item(i)).getAttribute("value"));
        }
        return values;
    }
}
