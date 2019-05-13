package com.neotys.neoload.model.v3.writers.neoload.population;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PopulationWriterTest {

    @Test
    public void writePopulationTest() throws ParserConfigurationException {

        UserPathPolicy policy1 = UserPathPolicy.builder().name("user1").distribution(10).build();
        UserPathPolicy policy2 = UserPathPolicy.builder().name("user2").distribution(90).build();

        Population population = new Population.Builder().name("myPopulation").addUserPaths(policy1, policy2).build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlPopulation = document.createElement("population-test");
        PopulationWriter.of(population).writeXML(document, xmlPopulation);
        Assertions.assertThat(xmlPopulation.getChildNodes().getLength()).isEqualTo(1);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("myPopulation");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("user1");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("10.0");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("user2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("90.0");
    }
}
