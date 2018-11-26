package com.neotys.neoload.model.writers.neoload;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.ImmutablePopulation;
import com.neotys.neoload.model.repository.ImmutablePopulationSplit;
import com.neotys.neoload.model.repository.Population;
import com.neotys.neoload.model.repository.PopulationSplit;

public class PopulationWriterTest {

    @Test
    public void writePopulationTest() throws ParserConfigurationException {

        PopulationSplit split1 = ImmutablePopulationSplit.builder().userPath("user1").percentage(10).build();
        PopulationSplit split2 = ImmutablePopulationSplit.builder().userPath("user2").percentage(90).build();

        Population population = ImmutablePopulation.builder().name("myPopulation").addSplits(split1, split2).build();

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
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("10");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("virtualUserUid").getNodeValue()).isEqualTo("user2");
        Assertions.assertThat(xmlPopulation.getChildNodes().item(0).getChildNodes().item(1).getAttributes().getNamedItem("factor").getNodeValue()).isEqualTo("90");
    }
}
