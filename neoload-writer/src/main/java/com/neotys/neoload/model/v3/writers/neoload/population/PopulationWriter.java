package com.neotys.neoload.model.v3.writers.neoload.population;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PopulationWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "population";
    public static final String XML_POPULATION_NAME_ATTR = "uid";

    public PopulationWriter(Population population) {
        super(population);
    }

    public static PopulationWriter of(final Population population) {
        return new PopulationWriter(population);
    }

    public void writeXML(final Document document, final Element currentElement) {
        Population thePopulation = (Population) this.element;
        Element xmlPopulation = document.createElement(XML_TAG_NAME);
        xmlPopulation.setAttribute(XML_POPULATION_NAME_ATTR, thePopulation.getName());
        super.writeDescription(document, xmlPopulation);

        currentElement.appendChild(xmlPopulation);
        thePopulation.getUserPaths().forEach(userPathPolicy -> UserPathPolicyWriter.of(userPathPolicy).writeXML(document, xmlPopulation));
    }
}