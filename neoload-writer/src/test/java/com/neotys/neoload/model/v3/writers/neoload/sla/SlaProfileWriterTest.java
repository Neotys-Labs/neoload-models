package com.neotys.neoload.model.v3.writers.neoload.sla;

import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SlaProfileWriterTest {

    @Test
    public void writeSlaProfileTest() throws ParserConfigurationException {

        SlaThresholdCondition condition = SlaThresholdCondition.builder()
                .operator(SlaThresholdCondition.Operator.GREATER_THAN)
                .severity(SlaThresholdCondition.Severity.WARN)
                .value(3.0)
                .build();
        SlaThresholdCondition condition2 = SlaThresholdCondition.builder()
                .operator(SlaThresholdCondition.Operator.LESS_THAN)
                .severity(SlaThresholdCondition.Severity.FAIL)
                .value(6.0)
                .build();

        SlaThreshold threshold = SlaThreshold.builder()
                .scope(SlaThreshold.Scope.PER_TEST)
                .kpi(SlaThreshold.KPI.ERRORS_COUNT)
                .percent(4)
                .addConditions(condition,condition2)
                .build();

        SlaProfile profile = SlaProfile.builder()
                .name("mySlaProfile")
                .addThresholds(threshold)
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        SlaProfileWriter.of(profile).writeXML(document, null, "");
        Assertions.assertThat(document.getDocumentElement().getTagName()).isEqualTo("sla-profile");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().getLength()).isEqualTo(15);
        Assertions.assertThat(document.getDocumentElement().getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("mySlaProfile");
        Assertions.assertThat(document.getDocumentElement().getAttributes().getNamedItem("uid")).isNotNull();

        int index = getChildWithAttributeAndValue(document.getDocumentElement().getChildNodes(), "identifier","TOTAL_ERRORS");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getAttributes().getNamedItem("enabled").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getAttributes().getNamedItem("family").getNodeValue()).isEqualTo("PER_RUN");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getAttributes().getNamedItem("identifier").getNodeValue()).isEqualTo("TOTAL_ERRORS");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getAttributes().getNamedItem("percent").getNodeValue()).isEqualTo("4");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getAttributes().getNamedItem("uuid").getNodeValue()).isNotEmpty();

        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getChildNodes().item(0).getAttributes().getNamedItem("operator").getNodeValue()).isEqualTo("GREATER_THAN");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getChildNodes().item(0).getAttributes().getNamedItem("severity").getNodeValue()).isEqualTo("LOW");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getChildNodes().item(1).getAttributes().getNamedItem("operator").getNodeValue()).isEqualTo("LESS_THAN");
        Assertions.assertThat(document.getDocumentElement().getChildNodes().item(index).getChildNodes().item(1).getAttributes().getNamedItem("severity").getNodeValue()).isEqualTo("HIGH");

    }

    private int getChildWithAttributeAndValue(NodeList nodes, String attribute, String value) {
        for(int i=0; i<nodes.getLength();i++) {
            Node node = nodes.item(i).getAttributes().getNamedItem(attribute);
            if(node!=null && node.getNodeValue().equals(value)) return i;
        }
        return -1;
    }
}
