package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ScenarioWriterTest {

    @Test
    public void writeScenarioConstantTimeTest() throws ParserConfigurationException {

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
        // <constant-volume-policy userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
    }

    @Test
    public void writeScenarioConstantIterationTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder().name("myScenario")
                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
                        .durationPolicy(ImmutableIterationDurationPolicy.builder().build())
                        .loadPolicy(ImmutableConstantLoadPolicy.builder().iterationNumber(12).load(25).build())
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

        //<duration-policy-entry type="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        // <constant-volume-policy iterationNumber="12" userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("iterationNumber").getNodeValue()).isEqualTo("12");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
    }

    @Test
    public void writeScenarioPeaksTimeTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder().name("myScenario")
                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
                        .durationPolicy(ImmutableTimeDurationPolicy.builder().duration(122).build())
                        .loadPolicy(ImmutablePeaksLoadPolicy.builder().minimumLoad(10).maximumLoad(25).minimumTime(10).maximumTime(11).startPolicy(PeaksLoadPolicy.StartPolicy.MINIMUM_LOAD).build())
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
        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="10" peakDelay="11" defaultDelayType="1" peakDelayType="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("11");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("1");
    }

    @Test
    public void writeScenarioRampUpTimeTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder().name("myScenario")
                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
                        .durationPolicy(ImmutableTimeDurationPolicy.builder().duration(122).build())
                        .loadPolicy(ImmutableRampupLoadPolicy.builder().incrementTime(15).initialLoad(10).incrementLoad(15).maximumLoad(50).build())
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

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="15" delayTypeIncrement="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("1");
    }
}
