package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.writers.neoload.ProjectWriter;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class PopulationPolicyWriter {

	private static final String XML_TAG_NAME = "population-policy";
	private static final String XML_ATTR_NAME = "name";

	private static final String XML_TAG_LGS_NAME = "lg-hosts";
	private static final String XML_TAG_LG_ENTRY_NAME = "lg-host-entry";

    private final PopulationPolicy population;

    private PopulationPolicyWriter(final PopulationPolicy population) {
        this.population = population;
    }

    protected static PopulationPolicyWriter of(final PopulationPolicy population) {
        return new PopulationPolicyWriter(population);
    }

    public void writeXML(final Document document, final Element currentElement) {
    	// Population Policy tag
        Element xmlPopulationPolicy = document.createElement(XML_TAG_NAME);
        // Name attribute
        xmlPopulationPolicy.setAttribute(XML_ATTR_NAME, population.getName());

        WriterUtils.<LoadPolicyWriter>getWriterFor(population.getLoadPolicy()).writeXML(document, xmlPopulationPolicy);

        // Write a default zone and localhost LG
        Element lgsElement = document.createElement(XML_TAG_LGS_NAME);
        Element localhostElement = document.createElement(XML_TAG_LG_ENTRY_NAME);
        localhostElement.setTextContent("$zoneID="+ ProjectWriter.DEFAULT_ZONE_NAME+";$lgID="+ProjectWriter.DEFAULT_LG_NAME);
        lgsElement.appendChild(localhostElement);

        xmlPopulationPolicy.appendChild(lgsElement);

        currentElement.appendChild(xmlPopulationPolicy);
    }
}
