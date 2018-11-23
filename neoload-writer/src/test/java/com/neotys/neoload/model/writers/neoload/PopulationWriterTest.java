package com.neotys.neoload.model.writers.neoload;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.population.Population;
import com.neotys.neoload.model.population.UserPathPolicy;


public class PopulationWriterTest {
    @Test
    public void writePopulationWithoutFractionTest() throws ParserConfigurationException {
        UserPathPolicy userPath1 = UserPathPolicy.builder().name("myUserPath1").distribution(10.0).build();
        UserPathPolicy userPath2 = UserPathPolicy.builder().name("myUserPath2").distribution(90.0).build();

        Population population = Population.builder().name("myPopulation").addUserPaths(userPath1, userPath2).build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlPopulation = document.createElement("population-test");
        PopulationWriter.of(population).writeXML(document, xmlPopulation);
        
        // <population uid="myPopulation">
        Assertions.assertThat(xmlPopulation.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("myPopulation");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);
        
        // <split virtualUserUid="myUserPath1">
        // <split virtualUserUid="myUserPath2">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("myUserPath1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("10.0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("myUserPath2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("90.0");
    }

    @Test
    public void writePopulationWithFractionTest() throws ParserConfigurationException {
        UserPathPolicy userPath1 = UserPathPolicy.builder().name("myUserPath1").distribution(10.5).build();
        UserPathPolicy userPath2 = UserPathPolicy.builder().name("myUserPath2").distribution(89.5).build();

        Population population = Population.builder().name("myPopulation").description("myDescription").addUserPaths(userPath1, userPath2).build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlPopulation = document.createElement("population-test");
        PopulationWriter.of(population).writeXML(document, xmlPopulation);
        
        // <population uid="myPopulation">
        Assertions.assertThat(xmlPopulation.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("myPopulation");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(3);       
        
        // <description>myDescription</description>
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("description");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getTextContent()).isEqualTo("myDescription");

        // <split virtualUserUid="myUserPath1">
        // <split virtualUserUid="myUserPath2">
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("myUserPath1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("10.5");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("myUserPath2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(2).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("89.5");
    }
}
