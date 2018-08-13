package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ScenarioWriterTest {

    @Test
    public void writeScenarioConstantTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder().name("myScenario")
                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
                        .durationPolicy(ImmutableTimeDurationPolicy.builder().duration(122).build())
                        .loadPolicy(ImmutableConstantLoadPolicy.builder().load(25).build())
                        .build())
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlPopulation = document.createElement("scenario-test");
        ScenarioWriter.of(scenario).writeXML(document, xmlPopulation);
        Assertions.assertThat(xmlPopulation.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("myScenario");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        //<duration-policy-entry time="122" timeUnit="0" type="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("122");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        // <constant-volume-policy iterationNumber="1" userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
    }
}
