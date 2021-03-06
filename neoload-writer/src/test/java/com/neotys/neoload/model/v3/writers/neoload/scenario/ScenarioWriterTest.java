package com.neotys.neoload.model.v3.writers.neoload.scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.scenario.Apm;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.MonitoringParameters;
import com.neotys.neoload.model.v3.project.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RendezvousPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;

public class ScenarioWriterTest {

	private void checkLoadPolicyNoLimit(final Element xmlPopulation) {
        //<duration-policy-entry type="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("0");
        
        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <start-stop-policy-entry stop-type="0" start-type="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("0");
        
        // <runtime-policy vuStartMode="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("0");
	}
	
	private void checkLoadPolicyTime(final Element xmlPopulation) {
        //<duration-policy-entry time="240" timeUnit="0" type="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("240");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <start-stop-policy-entry stop-type="1" stop-delay="180000" start-type="1" start-delay="60000"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("180000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("60000");
        
        // <runtime-policy vuStartMode="1" vuStartDelay="120000"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("120000");
	}

	private void checkLoadPolicyIteration(final Element xmlPopulation) {
        //<duration-policy-entry type="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("1");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <start-stop-policy-entry stop-type="2" start-type="2" start-population="myPopulation0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-population").getNodeValue()).isEqualTo("myPopulation0");
        
        // <runtime-policy vuStartMode="1" vuStartDelay="120000"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("120000");
	}

    @Test
    public void writeScenarioConstantNoLimitTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(new ConstantLoadPolicy.Builder()
                				.users(25)               				
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyNoLimit(xmlPopulation);

        // <constant-volume-policy userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
    }

    @Test
    public void writeScenarioConstantTimeTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ConstantLoadPolicy.builder()
                				.users(25)
                				.duration(LoadDuration.builder()
                						.value(240)
                						.type(LoadDuration.Type.TIME)
                						.build())
                				.startAfter(StartAfter.builder()
                						.value(60)
                						.type(StartAfter.Type.TIME)
                						.build())
                				.rampup(120)
                				.stopAfter(StopAfter.builder()
                						.value(180)
                						.type(StopAfter.Type.TIME)
                						.build())
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyTime(xmlPopulation);

        // <constant-volume-policy userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
    }

    @Test
    public void writeScenarioConstantIterationTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
        		.description("myDescription")
        		.slaProfile("mySlaProfile")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ConstantLoadPolicy.builder()
                				.users(25)
                				.duration(LoadDuration.builder()
                						.value(12)
                						.type(LoadDuration.Type.ITERATION)
                						.build())
                				.startAfter(StartAfter.builder()
                						.value("myPopulation0")
                						.type(StartAfter.Type.POPULATION)
                						.build())
                				.rampup(120)
                				.stopAfter(StopAfter.builder()
                						.type(StopAfter.Type.CURRENT_ITERATION)
                						.build())
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileName").getNodeValue()).isEqualTo("mySlaProfile");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);

        // <description>myDescription</description>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("description");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("myDescription");

        // <population-policy name="Population1">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyIteration(xmlPopulation);

        // <constant-volume-policy iterationNumber="12" userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("iterationNumber").getNodeValue()).isEqualTo("12");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
    }

    @Test
    public void writeScenarioPeaksNoLimitTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(PeaksLoadPolicy.builder()
                				.minimum(PeakLoadPolicy.builder()
                						.users(10)
                						.duration(LoadDuration.builder()
                								.value(10)
                								.type(LoadDuration.Type.TIME)
                								.build())
                						.build())
                				.maximum(PeakLoadPolicy.builder()
                						.users(25)
                						.duration(LoadDuration.builder()
                								.value(20)
                								.type(LoadDuration.Type.TIME)
                								.build())
                						.build())
                				.start(PeaksLoadPolicy.Peak.MINIMUM)
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);

        // <population-policy name="Population1">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyNoLimit(xmlPopulation);

        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="10" peakDelay="20" defaultDelayType="1" peakDelayType="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("20");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("startPoint").getNodeValue()).isEqualTo("0");
    }

    @Test
    public void writeScenarioPeaksTimeTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(PeaksLoadPolicy.builder()
                				.minimum(PeakLoadPolicy.builder()
                						.users(10)
                						.duration(LoadDuration.builder()
                								.value(10)
                								.type(LoadDuration.Type.TIME)
                								.build())
                						.build())
                				.maximum(PeakLoadPolicy.builder()
                						.users(25)
                						.duration(LoadDuration.builder()
                								.value(20)
                								.type(LoadDuration.Type.TIME)
                								.build())
                						.build())
                				.duration(LoadDuration.builder()
                						.value(240)
                						.type(LoadDuration.Type.TIME)
                						.build())
                				.start(PeaksLoadPolicy.Peak.MINIMUM)
                				.startAfter(StartAfter.builder()
                						.value(60)
                						.type(StartAfter.Type.TIME)
                						.build())
                				.rampup(120)
                				.stopAfter(StopAfter.builder()
                						.value(180)
                						.type(StopAfter.Type.TIME)
                						.build())
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);

        // <population-policy name="Population1">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyTime(xmlPopulation);

        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="10" peakDelay="20" defaultDelayType="1" peakDelayType="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("20");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("startPoint").getNodeValue()).isEqualTo("0");
    }

    @Test
    public void writeScenarioPeaksIterationTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
        		.description("myDescription")
        		.slaProfile("mySlaProfile")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(PeaksLoadPolicy.builder()
                				.minimum(PeakLoadPolicy.builder()
                						.users(10)
                						.duration(LoadDuration.builder()
                								.value(2)
                								.type(LoadDuration.Type.ITERATION)
                								.build())
                						.build())
                				.maximum(PeakLoadPolicy.builder()
                						.users(25)
                						.duration(LoadDuration.builder()
                								.value(2)
                								.type(LoadDuration.Type.ITERATION)
                								.build())
                						.build())
                				.duration(LoadDuration.builder()
                						.value(20)
                						.type(LoadDuration.Type.ITERATION)
                						.build())
                				.start(PeaksLoadPolicy.Peak.MAXIMUM)
                		    	.startAfter(StartAfter.builder()
                						.value("myPopulation0")
                						.type(StartAfter.Type.POPULATION)
                						.build())
                				.rampup(120)
                				.stopAfter(StopAfter.builder()
                						.type(StopAfter.Type.CURRENT_ITERATION)
                						.build())
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileName").getNodeValue()).isEqualTo("mySlaProfile");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);

        // <description>myDescription</description>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("description");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("myDescription");

        // <population-policy name="Population1">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyIteration(xmlPopulation);

        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="2" peakDelay="2" defaultDelayType="2" peakDelayType="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("startPoint").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("iterationNumber").getNodeValue()).isEqualTo("20");
    }


    @Test
    public void writeScenarioRampUpNoLimitTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(RampupLoadPolicy.builder()
                				.minUsers(10)
                				.maxUsers(50)
                				.incrementUsers(15)
                				.incrementEvery(LoadDuration.builder()
                						.value(15)
                						.type(LoadDuration.Type.TIME)
                						.build())
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);

        // <population-policy name="Population1">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyNoLimit(xmlPopulation);

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="15" delayTypeIncrement="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("1");
    }

    @Test
    public void writeScenarioRampUpTimeTest() throws ParserConfigurationException {

        Scenario scenario = Scenario.builder()
        		.name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(RampupLoadPolicy.builder()
                				.minUsers(10)
                				.maxUsers(50)
                				.incrementUsers(15)
                				.incrementEvery(LoadDuration.builder()
                						.value(15)
                						.type(LoadDuration.Type.TIME)
                						.build())
                				.duration(LoadDuration.builder()
                						.value(240)
                						.type(LoadDuration.Type.TIME)
                						.build())
                				.startAfter(StartAfter.builder()
                						.value(60)
                						.type(StartAfter.Type.TIME)
                						.build())
                				.rampup(120)
                				.stopAfter(StopAfter.builder()
                						.value(180)
                						.type(StopAfter.Type.TIME)
                						.build())
                				.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyTime(xmlPopulation);

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="15" delayTypeIncrement="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("1");
    }

    @Test
    public void writeScenarioRampUpIterationTest() throws ParserConfigurationException {
        Scenario scenario = Scenario.builder()
        		.name("myScenario")
        		.description("myDescription")
        		.slaProfile("mySlaProfile")
                .addPopulations(PopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(RampupLoadPolicy.builder()
                				.minUsers(10)
                				.maxUsers(50)
                				.incrementUsers(15)
                				.incrementEvery(LoadDuration.builder()
                						.value(2)
                						.type(LoadDuration.Type.ITERATION)
                						.build())
                				.duration(LoadDuration.builder()
                						.value(20)
                						.type(LoadDuration.Type.ITERATION)
                						.build())
                   		    	.startAfter(StartAfter.builder()
                    					.value("myPopulation0")
                    					.type(StartAfter.Type.POPULATION)
                    					.build())
                    			.rampup(120)
                    			.stopAfter(StopAfter.builder()
                    					.type(StopAfter.Type.CURRENT_ITERATION)
                    					.build())
                    			.build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("true");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileName").getNodeValue()).isEqualTo("mySlaProfile");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);

        // <description>myDescription</description>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("description");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("myDescription");

        // <population-policy name="Population1">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyIteration(xmlPopulation);

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="2" delayTypeIncrement="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("iterationNumber").getNodeValue()).isEqualTo("20");
    }

    @Test
    public void writeScenarioCustomTimeTest() throws ParserConfigurationException {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(240)
                .type(LoadDuration.Type.TIME)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(300)
                .build();

        Scenario scenario = Scenario.builder()
                .name("myScenario")
                .addPopulations(PopulationPolicy.builder()
                        .name("myPopulation1")
                        .loadPolicy(CustomLoadPolicy.builder()
                                .steps(Collections.singletonList(customPolicyStep))
                                .startAfter(StartAfter.builder()
                                        .value(60)
                                        .type(StartAfter.Type.TIME)
                                        .build())
                                .rampup(120)
                                .stopAfter(StopAfter.builder()
                                        .value(180)
                                        .type(StopAfter.Type.TIME)
                                        .build())
                                .build())
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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("slaProfileEnabled").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("population-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("myPopulation1");

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyTime(xmlPopulation);

        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("custom-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("240");
        Assertions.assertThat(volumePolicyNode.getChildNodes().item(0).getAttributes().getNamedItem("position").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(volumePolicyNode.getChildNodes().item(0).getAttributes().getNamedItem("users").getNodeValue()).isEqualTo("300");
    }

    @Test
    public void writeScenarioCustomIterationTest() throws ParserConfigurationException {
        ImmutableLoadDuration loadDuration = LoadDuration.builder()
                .value(240)
                .type(LoadDuration.Type.ITERATION)
                .build();
        ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
                .when(loadDuration)
                .users(300)
                .build();

        Scenario scenario = Scenario.builder()
                .name("myScenario")
                .description("myDescription")
                .slaProfile("mySlaProfile")
                .addPopulations(PopulationPolicy.builder()
                        .name("myPopulation1")
                        .loadPolicy(CustomLoadPolicy.builder()
                                .steps(Collections.singletonList(customPolicyStep))
                                .startAfter(StartAfter.builder()
                                        .value("myPopulation0")
                                        .type(StartAfter.Type.POPULATION)
                                        .build())
                                .rampup(120)
                                .stopAfter(StopAfter.builder()
                                        .type(StopAfter.Type.CURRENT_ITERATION)
                                        .build())
                                .build())
                        .build())
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlPopulation = document.createElement("scenario-test");
        ScenarioWriter.of(scenario).writeXML(document, xmlPopulation);

        // Check duration-policy-entry, volume-policy-entry, start-stop-policy-entry and runtime-policy tags
        checkLoadPolicyIteration(xmlPopulation);

        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("custom-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getChildNodes().item(0).getAttributes().getNamedItem("iteration").getNodeValue()).isEqualTo("240");
        Assertions.assertThat(volumePolicyNode.getChildNodes().item(0).getAttributes().getNamedItem("position").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(volumePolicyNode.getChildNodes().item(0).getAttributes().getNamedItem("users").getNodeValue()).isEqualTo("300");
    }

    @Test
    public void writeScenarioApmTest() throws ParserConfigurationException {
        Scenario scenario = Scenario.builder()
                .name("myScenario")
                .description("myDescription")
                .slaProfile("mySlaProfile")
                .apm(Apm.builder()
                        .addDynatraceTags("firstTag", "secondTag")
                        .addDynatraceAnomalyRules(DynatraceAnomalyRule.builder()
                                .metricId("builtin:metric")
                                .operator("BELOW")
                                .value("10")
                                .severity("ERROR")
                                .build())
                        .addDynatraceAnomalyRules(DynatraceAnomalyRule.builder()
                                .metricId("builtin:metric2")
                                .operator("ABOVE")
                                .value("12")
                                .severity("PERFORMANCE")
                                .build())
                        .build())
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlScenario = document.createElement("scenario-test");
        ScenarioWriter.of(scenario).writeXML(document, xmlScenario);

        Assertions.assertThat(xmlScenario.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getNodeName()).isEqualTo("scenario");

        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("description");
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("dynatrace-monitoring");
        final NodeList dynatraceMonitoring = xmlScenario.getChildNodes().item(0).getChildNodes().item(1).getChildNodes();
        Assertions.assertThat(dynatraceMonitoring.getLength()).isEqualTo(4);
        Assertions.assertThat(dynatraceMonitoring.item(0).getNodeName()).isEqualTo("tag");
        Assertions.assertThat(dynatraceMonitoring.item(0).getTextContent()).isEqualTo("firstTag");
        Assertions.assertThat(dynatraceMonitoring.item(1).getNodeName()).isEqualTo("tag");
        Assertions.assertThat(dynatraceMonitoring.item(1).getTextContent()).isEqualTo("secondTag");

        Assertions.assertThat(dynatraceMonitoring.item(2).getNodeName()).isEqualTo("anomaly-rule");
        Assertions.assertThat(dynatraceMonitoring.item(2).getAttributes().getLength()).isEqualTo(4);
        Assertions.assertThat(dynatraceMonitoring.item(2).getAttributes().getNamedItem("metric").getNodeValue()).isEqualTo("builtin:metric");
        Assertions.assertThat(dynatraceMonitoring.item(2).getAttributes().getNamedItem("operator").getNodeValue()).isEqualTo("BELOW");
        Assertions.assertThat(dynatraceMonitoring.item(2).getAttributes().getNamedItem("value").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(dynatraceMonitoring.item(2).getAttributes().getNamedItem("severity").getNodeValue()).isEqualTo("ERROR");

        Assertions.assertThat(dynatraceMonitoring.item(3).getNodeName()).isEqualTo("anomaly-rule");
        Assertions.assertThat(dynatraceMonitoring.item(3).getAttributes().getLength()).isEqualTo(4);
        Assertions.assertThat(dynatraceMonitoring.item(3).getAttributes().getNamedItem("metric").getNodeValue()).isEqualTo("builtin:metric2");
        Assertions.assertThat(dynatraceMonitoring.item(3).getAttributes().getNamedItem("operator").getNodeValue()).isEqualTo("ABOVE");
        Assertions.assertThat(dynatraceMonitoring.item(3).getAttributes().getNamedItem("value").getNodeValue()).isEqualTo("12");
        Assertions.assertThat(dynatraceMonitoring.item(3).getAttributes().getNamedItem("severity").getNodeValue()).isEqualTo("PERFORMANCE");
    }

    @Test
    public void writeScenarioUrlExclusionTest() throws ParserConfigurationException {
        Scenario scenario = Scenario.builder()
                .name("myScenario")
                .description("myDescription")
                .slaProfile("mySlaProfile")
                .addAllExcludedUrls(ImmutableList.of(".*\\.jpg", ".*\\.png"))
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlScenario = document.createElement("scenario-test");
        ScenarioWriter.of(scenario).writeXML(document, xmlScenario);

        Assertions.assertThat(xmlScenario.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getNodeName()).isEqualTo("scenario");

        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("description");
        Assertions.assertThat(xmlScenario.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("request-path-exclusion-filter");
        final NodeList excludedUrls = xmlScenario.getChildNodes().item(0).getChildNodes().item(1).getChildNodes();
        Assertions.assertThat(excludedUrls.getLength()).isEqualTo(1);
        Assertions.assertThat(excludedUrls.item(0).getNodeName()).isEqualTo("regexps");
        final NodeList regexpsNode = excludedUrls.item(0).getChildNodes();
		Assertions.assertThat(regexpsNode.getLength()).isEqualTo(2);
		Assertions.assertThat(regexpsNode.item(0).getNodeName()).isEqualTo("regexp");
		Assertions.assertThat(regexpsNode.item(0).getTextContent()).isEqualTo(".*\\.jpg");
		Assertions.assertThat(regexpsNode.item(1).getNodeName()).isEqualTo("regexp");
        Assertions.assertThat(regexpsNode.item(1).getTextContent()).isEqualTo(".*\\.png");
    }

    @Test
    public void writeScenarioRendezvousTest() throws ParserConfigurationException {
	    final List<RendezvousPolicy> listRendezvous = new ArrayList<>();
	    listRendezvous.add(RendezvousPolicy.builder()
			    .when(WhenRelease.builder()
					    .type(WhenRelease.Type.PERCENTAGE)
					    .value(15)
					    .build())
			    .name("percentage")
			    .timeout(200)
			    .build());
	    listRendezvous.add(RendezvousPolicy.builder()
			    .when(WhenRelease.builder()
					    .type(WhenRelease.Type.VU_NUMBER)
					    .value(25)
					    .build())
			    .name("number")
			    .build());
	    listRendezvous.add(RendezvousPolicy.builder()
			    .when(WhenRelease.builder()
					    .type(WhenRelease.Type.MANUAL)
					    .build())
			    .name("manually")
			    .timeout(150)
			    .build());
	    Scenario scenario = Scenario.builder()
                .name("myScenario")
                .description("myDescription")
                .slaProfile("mySlaProfile")
                .rendezvousPolicies(listRendezvous)
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlScenario = document.createElement("scenario-test");
        ScenarioWriter.of(scenario).writeXML(document, xmlScenario);

	    final NodeList childNodes = xmlScenario.getChildNodes();
	    Assertions.assertThat(childNodes.getLength()).isEqualTo(1);
	    final Node item0 = childNodes.item(0);
	    Assertions.assertThat(item0.getNodeName()).isEqualTo("scenario");

        Assertions.assertThat(item0.getChildNodes().getLength()).isEqualTo(4);
        Assertions.assertThat(item0.getChildNodes().item(0).getNodeName()).isEqualTo("description");

        Assertions.assertThat(item0.getChildNodes().item(1).getNodeName()).isEqualTo("rdv-percentile-vu-count-policy");
	    final NamedNodeMap percentageAttributes = item0.getChildNodes().item(1).getAttributes();
	    Assertions.assertThat(percentageAttributes.item(0).getNodeName()).isEqualTo("isEnabled");
	    Assertions.assertThat(percentageAttributes.item(0).getNodeValue()).isEqualTo("true");
	    Assertions.assertThat(percentageAttributes.item(1).getNodeName()).isEqualTo("percentileVirtualUserCount");
	    Assertions.assertThat(percentageAttributes.item(1).getNodeValue()).isEqualTo("15");
	    Assertions.assertThat(percentageAttributes.item(2).getNodeName()).isEqualTo("rendezVousName");
	    Assertions.assertThat(percentageAttributes.item(2).getNodeValue()).isEqualTo("percentage");
	    Assertions.assertThat(percentageAttributes.item(3).getNodeName()).isEqualTo("timeout");
	    Assertions.assertThat(percentageAttributes.item(3).getNodeValue()).isEqualTo("200");

        Assertions.assertThat(item0.getChildNodes().item(2).getNodeName()).isEqualTo("rdv-fixed-vu-count-policy");
	    final NamedNodeMap fixedVuAttributes = item0.getChildNodes().item(2).getAttributes();
	    Assertions.assertThat(fixedVuAttributes.item(0).getNodeName()).isEqualTo("fixedVirtualUserCount");
	    Assertions.assertThat(fixedVuAttributes.item(0).getNodeValue()).isEqualTo("25");
	    Assertions.assertThat(fixedVuAttributes.item(1).getNodeName()).isEqualTo("isEnabled");
	    Assertions.assertThat(fixedVuAttributes.item(1).getNodeValue()).isEqualTo("true");
	    Assertions.assertThat(fixedVuAttributes.item(2).getNodeName()).isEqualTo("rendezVousName");
	    Assertions.assertThat(fixedVuAttributes.item(2).getNodeValue()).isEqualTo("number");
	    Assertions.assertThat(fixedVuAttributes.item(3).getNodeName()).isEqualTo("timeout");
	    Assertions.assertThat(fixedVuAttributes.item(3).getNodeValue()).isEqualTo("300");

        Assertions.assertThat(item0.getChildNodes().item(3).getNodeName()).isEqualTo("rdv-manual-policy");
	    final NamedNodeMap manualAttributes = item0.getChildNodes().item(3).getAttributes();
	    Assertions.assertThat(manualAttributes.item(0).getNodeName()).isEqualTo("isEnabled");
	    Assertions.assertThat(manualAttributes.item(0).getNodeValue()).isEqualTo("true");
	    Assertions.assertThat(manualAttributes.item(1).getNodeName()).isEqualTo("rendezVousName");
	    Assertions.assertThat(manualAttributes.item(1).getNodeValue()).isEqualTo("manually");
	    Assertions.assertThat(manualAttributes.item(2).getNodeName()).isEqualTo("timeout");
	    Assertions.assertThat(manualAttributes.item(2).getNodeValue()).isEqualTo("150");
    }

	@Test
	public void writeScenarioMonitoringTest() throws ParserConfigurationException {

		Scenario scenario = Scenario.builder()
				.name("myScenario")
				.description("myDescription")
				.slaProfile("mySlaProfile")
				.monitoringParameters(MonitoringParameters.builder().afterLastVus(50).beforeFirstVu(10).build())
				.build();

		// write the repository
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		Element xmlScenario = document.createElement("scenario-test");
		ScenarioWriter.of(scenario).writeXML(document, xmlScenario);

		final NodeList childNodes = xmlScenario.getChildNodes();
		Assertions.assertThat(childNodes.getLength()).isEqualTo(1);
		final Node item0 = childNodes.item(0);
		Assertions.assertThat(item0.getNodeName()).isEqualTo("scenario");

		Assertions.assertThat(item0.getAttributes().getLength()).isEqualTo(6);
		Assertions.assertThat(item0.getAttributes().getNamedItem("postMonitoringTime").getNodeValue()).isEqualTo("50000");
		Assertions.assertThat(item0.getAttributes().getNamedItem("preMonitoringTime").getNodeValue()).isEqualTo("10000");
	}

	@Test
	public void writeScenarioStoreVariableTest() throws ParserConfigurationException {

		Scenario scenario = Scenario.builder()
				.name("myScenario")
				.description("myDescription")
				.slaProfile("mySlaProfile")
				.isStoredVariables(true)
				.build();

		// write the repository
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		Element xmlScenario = document.createElement("scenario-test");
		ScenarioWriter.of(scenario).writeXML(document, xmlScenario);

		final NodeList childNodes = xmlScenario.getChildNodes();
		Assertions.assertThat(childNodes.getLength()).isEqualTo(1);
		final Node item0 = childNodes.item(0);
		Assertions.assertThat(item0.getNodeName()).isEqualTo("scenario");

		Assertions.assertThat(item0.getAttributes().getLength()).isEqualTo(4);
		Assertions.assertThat(item0.getAttributes().getNamedItem("traceVariables").getNodeValue()).isEqualTo("true");

		Scenario scenario2 = Scenario.builder()
				.name("myScenario")
				.description("myDescription")
				.slaProfile("mySlaProfile")
				.isStoredVariables(false)
				.build();

		// write the repository
		Element xmlScenario2 = document.createElement("scenario-test2");
		ScenarioWriter.of(scenario2).writeXML(document, xmlScenario2);
		final NodeList childNodes2 = xmlScenario2.getChildNodes();
		final Node item2_1 = childNodes2.item(0);
		Assertions.assertThat(item2_1.getAttributes().getLength()).isEqualTo(4);
		Assertions.assertThat(item2_1.getAttributes().getNamedItem("traceVariables").getNodeValue()).isEqualTo("false");
	}
}
