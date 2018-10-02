package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.scenario.Scenario;

class ScenarioWriter {
	
    private static final String XML_TAG_SCENARIO_NAME = "scenario";
    private static final String XML_ATTR_NAME = "uid";
    private static final String XML_ATTR_SLAPROFILEENABLED = "slaProfileEnabled";
    private static final String XML_ATTR_SLAPROFILENAME = "slaProfileName";

    private static final String XML_TAG_DESCRIPTION_NAME = "description";

    private final Scenario scenario;

    private ScenarioWriter(final Scenario scenario) {
        this.scenario = scenario;
    }

    protected static ScenarioWriter of(Scenario scenario) {
        return new ScenarioWriter(scenario);
    }

    public void writeXML(final Document document, final Element currentElement) {
        // Scenario tag
    	final Element xmlScenario = document.createElement(XML_TAG_SCENARIO_NAME);
        // Uid attribute
        xmlScenario.setAttribute(XML_ATTR_NAME, scenario.getName());
        // Sla Profile Enabled & Sla Profile Name attributes
        xmlScenario.setAttribute(XML_ATTR_SLAPROFILEENABLED, "false");
        scenario.getSlaProfile().ifPresent(slaProfile -> {
        	xmlScenario.setAttribute(XML_ATTR_SLAPROFILEENABLED, "true");
        	xmlScenario.setAttribute(XML_ATTR_SLAPROFILENAME, slaProfile);
        });
        currentElement.appendChild(xmlScenario);

        // Description tag
        scenario.getDescription().ifPresent(description -> {
        	final Element xmlDescription = document.createElement(XML_TAG_DESCRIPTION_NAME);
            xmlDescription.setTextContent(description);
            xmlScenario.appendChild(xmlDescription);
        });
         
        // Population tag
        scenario.getPopulations().forEach(population -> PopulationPolicyWriter.of(population).writeXML(document, xmlScenario));
    }
}
