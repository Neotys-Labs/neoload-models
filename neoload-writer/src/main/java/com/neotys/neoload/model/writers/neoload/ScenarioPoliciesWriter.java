package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.ScenarioPolicies;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScenarioPoliciesWriter {

    public static final String XML_TAG_NAME = "population-policy";
    public static final String XML_NAME_ATTR = "name";

    private final String populationName;
    private final ScenarioPolicies scenarioPolicies;

    public ScenarioPoliciesWriter(String populationName, ScenarioPolicies scenarioPolicies) {
        this.populationName = populationName;
        this.scenarioPolicies = scenarioPolicies;
    }

    public static ScenarioPoliciesWriter of(String populationName, ScenarioPolicies scenarioPolicies) {
        return new ScenarioPoliciesWriter(populationName, scenarioPolicies);
    }

    public void writeXML(final Document document, final Element currentElement) {
        Element xmlPopulationPolicy = document.createElement(XML_TAG_NAME);
        xmlPopulationPolicy.setAttribute(XML_NAME_ATTR, this.populationName);

        WriterUtils.<DurationPolicyWriter>getWriterFor(scenarioPolicies.getDurationPolicy()).writeXML(document, xmlPopulationPolicy);
        WriterUtils.<LoadPolicyWriter>getWriterFor(scenarioPolicies.getLoadPolicy()).writeXML(document, xmlPopulationPolicy);


        currentElement.appendChild(xmlPopulationPolicy);
    }
}
