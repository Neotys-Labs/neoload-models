package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.*;
import com.neotys.neoload.model.scenario.Duration.Type;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy.Peak;

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
    public void writeScenarioConstantNoLimitTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableConstantLoadPolicy.builder()
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

        //<duration-policy-entry time="120" timeUnit="0" type="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("120");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");
        
        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <constant-volume-policy userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
        
        // <start-stop-policy-entry stop-type="0" stop-delay="60000" start-type="0" start-delay="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("60000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("0");
        
        // <runtime-policy vuStartMode="0" vuStartDelay="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("0");
    }

    @Test
    public void writeScenarioConstantTimeTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableConstantLoadPolicy.builder()
                				.users(25)
                				.duration(ImmutableDuration.builder()
                						.value(240)
                						.type(Type.TIME)
                						.build())
                				.startAfter(ImmutableStartAfter.builder()
                						.value(60)
                						.type(StartAfter.Type.TIME)
                						.build())
                				.rampup(120)
                				.stopAfter(ImmutableStopAfter.builder()
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

        //<duration-policy-entry time="240" timeUnit="0" type="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("240");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <constant-volume-policy userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
        
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

    @Test
    public void writeScenarioConstantIterationTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
        		.description("myDescription")
        		.slaProfile("mySlaProfile")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableConstantLoadPolicy.builder()
                				.users(25)
                				.duration(ImmutableDuration.builder()
                						.value(12)
                						.type(Type.ITERATION)
                						.build())
                				.startAfter(ImmutableStartAfter.builder()
                						.value("myPopulation0")
                						.type(StartAfter.Type.POPULATION)
                						.build())
                				.rampup(120)
                				.stopAfter(ImmutableStopAfter.builder()
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
        
        //<duration-policy-entry type="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("1");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <constant-volume-policy iterationNumber="12" userNumber="25"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("constant-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("iterationNumber").getNodeValue()).isEqualTo("12");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("userNumber").getNodeValue()).isEqualTo("25");
        
        // <start-stop-policy-entry stop-type="2" stop-delay="60000" start-type="2" start-delay="0" start-population="myPopulation0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("60000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-population").getNodeValue()).isEqualTo("myPopulation0");
        
        // <runtime-policy vuStartMode="1" vuStartDelay="120000"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("120000");
    }

    @Test
    public void writeScenarioPeaksNoLimitTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutablePeaksLoadPolicy.builder()
                				.minimum(ImmutablePeakLoadPolicy.builder()
                						.users(10)
                						.duration(ImmutableDuration.builder()
                								.value(10)
                								.type(Type.TIME)
                								.build())
                						.build())
                				.maximum(ImmutablePeakLoadPolicy.builder()
                						.users(25)
                						.duration(ImmutableDuration.builder()
                								.value(20)
                								.type(Type.TIME)
                								.build())
                						.build())
                				.start(Peak.MINIMUM)
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

        //<duration-policy-entry time="120" timeUnit="0" type="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("120");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="10" peakDelay="20" defaultDelayType="1" peakDelayType="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("20");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("1");
        
        // <start-stop-policy-entry stop-type="0" stop-delay="60000" start-type="0" start-delay="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("60000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("0");
        
        // <runtime-policy vuStartMode="0" vuStartDelay="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("0");
    }

    @Test
    public void writeScenarioPeaksTimeTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutablePeaksLoadPolicy.builder()
                				.minimum(ImmutablePeakLoadPolicy.builder()
                						.users(10)
                						.duration(ImmutableDuration.builder()
                								.value(10)
                								.type(Type.TIME)
                								.build())
                						.build())
                				.maximum(ImmutablePeakLoadPolicy.builder()
                						.users(25)
                						.duration(ImmutableDuration.builder()
                								.value(20)
                								.type(Type.TIME)
                								.build())
                						.build())
                				.duration(ImmutableDuration.builder()
                						.value(122)
                						.type(Type.TIME)
                						.build())
                				.start(Peak.MINIMUM)
                				.startAfter(ImmutableStartAfter.builder()
                						.value(60)
                						.type(StartAfter.Type.TIME)
                						.build())
                				.rampup(120)
                				.stopAfter(ImmutableStopAfter.builder()
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

        //<duration-policy-entry time="122" timeUnit="0" type="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("122");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="10" peakDelay="20" defaultDelayType="1" peakDelayType="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("20");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("1");
        
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

    @Test
    public void writeScenarioPeaksIterationTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
        		.description("myDescription")
        		.slaProfile("mySlaProfile")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutablePeaksLoadPolicy.builder()
                				.minimum(ImmutablePeakLoadPolicy.builder()
                						.users(10)
                						.duration(ImmutableDuration.builder()
                								.value(2)
                								.type(Type.ITERATION)
                								.build())
                						.build())
                				.maximum(ImmutablePeakLoadPolicy.builder()
                						.users(25)
                						.duration(ImmutableDuration.builder()
                								.value(2)
                								.type(Type.ITERATION)
                								.build())
                						.build())
                				.duration(ImmutableDuration.builder()
                						.value(20)
                						.type(Type.ITERATION)
                						.build())
                				.start(Peak.MINIMUM)
                		    	.startAfter(ImmutableStartAfter.builder()
                						.value("myPopulation0")
                						.type(StartAfter.Type.POPULATION)
                						.build())
                				.rampup(120)
                				.stopAfter(ImmutableStopAfter.builder()
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

        //<duration-policy-entry type="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("1");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");
        
        // <peaks-volume-policy defaultUserNumber="10" peakUserNumber="25" defaultDelay="2" peakDelay="2" defaultDelayType="2" peakDelayType="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("peaks-volume-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakUserNumber").getNodeValue()).isEqualTo("25");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelay").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelay").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("defaultDelayType").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("peakDelayType").getNodeValue()).isEqualTo("2");
        
        // <start-stop-policy-entry stop-type="2" stop-delay="60000" start-type="2" start-delay="0" start-population="myPopulation0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("60000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-population").getNodeValue()).isEqualTo("myPopulation0");
        
        // <runtime-policy vuStartMode="1" vuStartDelay="120000"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("120000");
    }


    @Test
    public void writeScenarioRampUpNoLimitTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableRampupLoadPolicy.builder()
                				.minUsers(10)
                				.maxUsers(50)
                				.incrementUsers(15)
                				.incrementEvery(ImmutableDuration.builder()
                						.value(15)
                						.type(Type.TIME)
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

        //<duration-policy-entry time="120" timeUnit="0" type="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("120");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="15" delayTypeIncrement="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("1");
        
        // <start-stop-policy-entry stop-type="0" stop-delay="60000" start-type="0" start-delay="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("60000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("0");
        
        // <runtime-policy vuStartMode="0" vuStartDelay="0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("0");
    }

    @Test
    public void writeScenarioRampUpTimeTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableRampupLoadPolicy.builder()
                				.minUsers(10)
                				.maxUsers(50)
                				.incrementUsers(15)
                				.incrementEvery(ImmutableDuration.builder()
                						.value(15)
                						.type(Type.TIME)
                						.build())
                				.duration(ImmutableDuration.builder()
                						.value(122)
                						.type(Type.TIME)
                						.build())
                				.startAfter(ImmutableStartAfter.builder()
                						.value(60)
                						.type(StartAfter.Type.TIME)
                						.build())
                				.rampup(120)
                				.stopAfter(ImmutableStopAfter.builder()
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

        //<duration-policy-entry time="122" timeUnit="0" type="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("time").getNodeValue()).isEqualTo("122");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("timeUnit").getNodeValue()).isEqualTo("0");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="15" delayTypeIncrement="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("1");
        
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
    
    @Test
    public void writeScenarioRampUpIterationTest() throws ParserConfigurationException {

        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
        		.description("myDescription")
        		.slaProfile("mySlaProfile")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableRampupLoadPolicy.builder()
                				.minUsers(10)
                				.maxUsers(50)
                				.incrementUsers(15)
                				.incrementEvery(ImmutableDuration.builder()
                						.value(2)
                						.type(Type.ITERATION)
                						.build())
                				.duration(ImmutableDuration.builder()
                						.value(20)
                						.type(Type.ITERATION)
                						.build())
                   		    	.startAfter(ImmutableStartAfter.builder()
                    					.value("myPopulation0")
                    					.type(StartAfter.Type.POPULATION)
                    					.build())
                    			.rampup(120)
                    			.stopAfter(ImmutableStopAfter.builder()
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

        //<duration-policy-entry type="1"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("duration-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("type").getNodeValue()).isEqualTo("1");

        // <volume-policy-entry>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getNodeName()).isEqualTo("volume-policy-entry");

        // <rampup-volume-policy initialUserNumber="10" userIncrement="15" maxUserNumber="50" delayIncrement="2" delayTypeIncrement="2"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0).getNodeName()).isEqualTo("rampup-volume-policy");
        Node volumePolicyNode = xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0);
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("initialUserNumber").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("userIncrement").getNodeValue()).isEqualTo("15");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("maxUserNumber").getNodeValue()).isEqualTo("50");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayIncrement").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(volumePolicyNode.getAttributes().getNamedItem("delayTypeIncrement").getNodeValue()).isEqualTo("2");
        
        // <start-stop-policy-entry stop-type="2" stop-delay="60000" start-type="2" start-delay="0" start-population="myPopulation0"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getNodeName()).isEqualTo("start-stop-policy-entry");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("stop-delay").getNodeValue()).isEqualTo("60000");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-type").getNodeValue()).isEqualTo("2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-delay").getNodeValue()).isEqualTo("0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(2).getAttributes().getNamedItem("start-population").getNodeValue()).isEqualTo("myPopulation0");
        
        // <runtime-policy vuStartMode="1" vuStartDelay="120000"/>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getNodeName()).isEqualTo("runtime-policy");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartMode").getNodeValue()).isEqualTo("1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("vuStartDelay").getNodeValue()).isEqualTo("120000");
    }
}
