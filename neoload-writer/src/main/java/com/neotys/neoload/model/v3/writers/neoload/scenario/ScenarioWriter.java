package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.writers.neoload.SlaElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScenarioWriter {
	
    private static final String XML_TAG_SCENARIO_NAME = "scenario";
    private static final String XML_ATTR_NAME = "uid";
    private static final String XML_ATTR_SLAPROFILEENABLED = "slaProfileEnabled";
    private static final String XML_ATTR_SLAPROFILENAME = "slaProfileName";

    private static final String XML_TAG_DESCRIPTION_NAME = "description";

    private final Scenario scenario;

    private ScenarioWriter(final Scenario scenario) {
        this.scenario = scenario;
    }

    public static ScenarioWriter of(Scenario scenario) {
        return new ScenarioWriter(scenario);
    }

    public void writeXML(final Document document, final Element currentElement) {
        // Scenario tag
    	final Element xmlScenario = document.createElement(XML_TAG_SCENARIO_NAME);
        // Uid attribute
        xmlScenario.setAttribute(XML_ATTR_NAME, scenario.getName());
        SlaElementWriter.of(scenario).writeXML(xmlScenario);
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
