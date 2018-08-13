package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.Scenario;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScenarioWriter {

    public static final String XML_TAG_NAME = "scenario";
    public static final String XML_NAME_ATTR = "uid";

    Scenario scenario;

    public ScenarioWriter(Scenario scenario) {
        this.scenario = scenario;
    }

    public static ScenarioWriter of(Scenario scenario) {
        return new ScenarioWriter(scenario);
    }

    public void writeXML(final Document document, final Element currentElement) {
        Element xmlScenario = document.createElement(XML_TAG_NAME);
        xmlScenario.setAttribute(XML_NAME_ATTR, this.scenario.getName());

        currentElement.appendChild(xmlScenario);
        this.scenario.getPopulations().forEach((populationName, scenarioPolicies) -> ScenarioPoliciesWriter.of(populationName, scenarioPolicies).writeXML(document, xmlScenario));
    }
}
