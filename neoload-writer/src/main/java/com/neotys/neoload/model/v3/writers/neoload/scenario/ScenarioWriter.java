package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.writers.neoload.SlaElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScenarioWriter {
	
    private static final String XML_TAG_SCENARIO_NAME = "scenario";
    private static final String XML_ATTR_NAME = "uid";
    private static final String XML_ATTR_REQUEST_PATH_EXCLUSION_FILTER = "request-path-exclusion-filter";
    private static final String XML_ATTR_REQUEST_PATH_EXCLUSION_FILTER_ENABLED = "isEnabled";
    private static final String XML_ATTR_REGEXPS = "regexps";
    private static final String XML_ATTR_REGEXP = "regexp";
    private static final String XML_ATTR_SLA_PROFILE_ENABLED = "slaProfileEnabled";
    private static final String XML_ATTR_SLA_PROFILE_NAME = "slaProfileName";
    private static final String XML_ATTR_PRE_MONITORING_TIME = "preMonitoringTime";
    private static final String XML_ATTR_POST_MONITORING_TIME = "postMonitoringTime";
    private static final String XML_ATTR_TRACE_VARIABLES = "traceVariables";

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
        	xmlScenario.setAttribute(XML_ATTR_SLA_PROFILE_ENABLED, "true");
        	xmlScenario.setAttribute(XML_ATTR_SLA_PROFILE_NAME, slaProfile);
        });
        xmlScenario.setAttribute(XML_ATTR_TRACE_VARIABLES, String.valueOf(scenario.isStoredVariables()));
        scenario.getMonitoringParameters().ifPresent(monitoringParameters -> {
        	if(monitoringParameters.getBeforeFirstVu() != null){
        		xmlScenario.setAttribute(XML_ATTR_PRE_MONITORING_TIME, String.valueOf(monitoringParameters.getBeforeFirstVu()));
	        }else {
		        xmlScenario.setAttribute(XML_ATTR_PRE_MONITORING_TIME, "-1");
	        }
        	if(monitoringParameters.getAfterLastVus() != null){
        		xmlScenario.setAttribute(XML_ATTR_POST_MONITORING_TIME, String.valueOf(monitoringParameters.getAfterLastVus()));
	        }else {
		        xmlScenario.setAttribute(XML_ATTR_POST_MONITORING_TIME, "-1");
	        }
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

        // Apm tag (dynatrace monitoring)
        scenario.getApm().ifPresent(apm -> ApmWriter.of(apm).writeXML(document, xmlScenario));

        // URL exclusions tag
        if(!scenario.getExcludedUrls().isEmpty()){
        	final Element requestPathExclusionFilter = document.createElement(XML_ATTR_REQUEST_PATH_EXCLUSION_FILTER);
        	requestPathExclusionFilter.setAttribute(XML_ATTR_REQUEST_PATH_EXCLUSION_FILTER_ENABLED, "true");

        	final Element regexps = document.createElement(XML_ATTR_REGEXPS);
        	requestPathExclusionFilter.appendChild(regexps);

        	for (final String excludedUrl : scenario.getExcludedUrls()) {
                final Element excludedUrlEl = document.createElement(XML_ATTR_REGEXP);
                excludedUrlEl.setTextContent(excludedUrl);
                regexps.appendChild(excludedUrlEl);
            }
        	xmlScenario.appendChild(requestPathExclusionFilter);
        }

        // RDV tag (dynatrace monitoring)
        scenario.getRendezvousPolicies().forEach(rdv -> RendezvousPolicyWriter.of(rdv).writeXML(document, xmlScenario));

    }
}
