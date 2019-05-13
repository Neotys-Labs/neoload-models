package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
}
