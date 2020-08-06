package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule;
import com.neotys.neoload.model.v3.project.scenario.ImmutableDynatraceAnomalyRule;
import com.neotys.neoload.model.v3.project.scenario.ImmutableApm;
import com.neotys.neoload.model.v3.project.scenario.Apm;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ApmWriterTest {

    @Test
    public void writeXMLAllFields() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlScenario = document.createElement("scenario-test");
        Apm apm = Apm.builder()
                .addDynatraceTags("tag1")
                .addDynatraceAnomalyRules(DynatraceAnomalyRule.builder()
                        .metricId("theActualmetricId")
                        .operator("ABOVE")
                        .value("123")
                        .severity("AVAILABILITY")
                        .build()
                )
                .build();
        ApmWriter.of(apm).writeXML(document, xmlScenario);
        Assertions.assertThat(xmlScenario.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getNodeName()).isEqualTo("dynatrace-monitoring");
        final NodeList dynatraceMonitoring = xmlScenario.getChildNodes().item(0).getChildNodes();
        Assertions.assertThat(dynatraceMonitoring.getLength()).isEqualTo(2);
        Assertions.assertThat(dynatraceMonitoring.item(0).getNodeName()).isEqualTo("tag");
        Assertions.assertThat(dynatraceMonitoring.item(0).getTextContent()).isEqualTo("tag1");

        Assertions.assertThat(dynatraceMonitoring.item(1).getNodeName()).isEqualTo("anomaly-rule");
        Assertions.assertThat(dynatraceMonitoring.item(1).getAttributes().getLength()).isEqualTo(4);
        Assertions.assertThat(dynatraceMonitoring.item(1).getAttributes().getNamedItem("metric").getNodeValue()).isEqualTo("theActualmetricId");
        Assertions.assertThat(dynatraceMonitoring.item(1).getAttributes().getNamedItem("operator").getNodeValue()).isEqualTo("ABOVE");
        Assertions.assertThat(dynatraceMonitoring.item(1).getAttributes().getNamedItem("value").getNodeValue()).isEqualTo("123");
        Assertions.assertThat(dynatraceMonitoring.item(1).getAttributes().getNamedItem("severity").getNodeValue()).isEqualTo("AVAILABILITY");
    }

    @Test
    public void writeXMLMinimumFields() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlScenario = document.createElement("scenario-test");
        Apm apm = Apm.builder().build();
        ApmWriter.of(apm).writeXML(document, xmlScenario);
        Assertions.assertThat(xmlScenario.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getNodeName()).isEqualTo("dynatrace-monitoring");
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().getLength()).isZero();
    }
}