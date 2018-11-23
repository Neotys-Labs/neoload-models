package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.population.Population;

public class PopulationWriter extends ElementWriter {
    public static final String XML_TAG_POPULATION_NAME = "population";
    public static final String XML_ATTR_NAME = "uid";

    public PopulationWriter(final Population population) {
        super(population);
    }

    public static PopulationWriter of(final Population population) {
        return new PopulationWriter(population);
    }

    public void writeXML(final Document document, final Element currentElement) {
        final Population population = (Population) this.element;
        
        // Population tag
        Element xmlPopulation = document.createElement(XML_TAG_POPULATION_NAME);
        xmlPopulation.setAttribute(XML_ATTR_NAME, population.getName());
        currentElement.appendChild(xmlPopulation);
        
        // Description tag
        super.writeDescription(document, xmlPopulation);

        // Split tag
        population.getUserPaths().forEach(userPaths -> UserPathPolicyWriter.of(userPaths).writeXML(document, xmlPopulation));
    }
}