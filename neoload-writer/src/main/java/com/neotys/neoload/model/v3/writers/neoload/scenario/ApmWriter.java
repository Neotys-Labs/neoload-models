package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.Apm;
import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class ApmWriter {

    private static final String XML_TAG_NAME = "dynatrace-monitoring";

    private static final String XML_TAG_DYNATRACE_TAG = "tag";
    private static final String XML_TAG_DYNATRACE_ANOMALY_RULE = "anomaly-rule";

    private final Apm apm;

    private ApmWriter(final Apm apm) {
        this.apm = apm;
    }

    protected static ApmWriter of(final Apm apm) {
        return new ApmWriter(apm);
    }

    public void writeXML(final Document document, final Element currentElement) {
        // dynatrace-monitoring tag
        Element xmlDynatraceMonitoring = document.createElement(XML_TAG_NAME);

        // Write Dynatrace tag list
        for (final String tag : apm.getDynatraceTags()) {
            final Element tagElt = document.createElement(XML_TAG_DYNATRACE_TAG);
            tagElt.setTextContent(tag);
            xmlDynatraceMonitoring.appendChild(tagElt);
        }

        // Write Dynatrace Anomaly detection rules
        for (final DynatraceAnomalyRule anomaly : apm.getDynatraceAnomalyRules()) {
            final Element anomalyRuleElt = document.createElement(XML_TAG_DYNATRACE_ANOMALY_RULE);
            anomalyRuleElt.setAttribute("metric", anomaly.getMetricId());
            anomalyRuleElt.setAttribute("operator", anomaly.getOperator());
            anomalyRuleElt.setAttribute("value", anomaly.getValue());
            anomalyRuleElt.setAttribute("severity", anomaly.getSeverity());
            xmlDynatraceMonitoring.appendChild(anomalyRuleElt);
        }

        currentElement.appendChild(xmlDynatraceMonitoring);
    }
}
