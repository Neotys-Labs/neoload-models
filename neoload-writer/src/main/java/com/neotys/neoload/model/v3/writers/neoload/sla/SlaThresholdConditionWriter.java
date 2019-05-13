package com.neotys.neoload.model.v3.writers.neoload.sla;

import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SlaThresholdConditionWriter {

    private final static String XML_TAG_NAME = "threshold-condition";
    private final static String XML_DURATION_KIND_ATTR = "durationKind";
    private final static String XML_DURATION_NUMBER_ATTR = "durationNumber";
    private final static String XML_OPERATOR_ATTR = "operator";
    private final static String XML_SEVERITY_ATTR = "severity";
    private final static String XML_VALUE_MIN_ATTR = "valueMin";
    private final static String XML_VALUE_MAX_ATTR = "valueMax";

    private SlaThresholdCondition slaThresholdCondition;

    public SlaThresholdConditionWriter(SlaThresholdCondition slaThresholdCondition) {
        this.slaThresholdCondition = slaThresholdCondition;
    }

    public static SlaThresholdConditionWriter of(SlaThresholdCondition slaThresholdCondition) { return new SlaThresholdConditionWriter(slaThresholdCondition);}

    public void writeXML(final Document document, final org.w3c.dom.Element currentElement) {
        final Element xmlThresholdCondition = document.createElement(XML_TAG_NAME);

        xmlThresholdCondition.setAttribute(XML_DURATION_KIND_ATTR, "NB_OF_TIMES");
        xmlThresholdCondition.setAttribute(XML_DURATION_NUMBER_ATTR, "1");
        xmlThresholdCondition.setAttribute(XML_OPERATOR_ATTR, getXmlOperator(slaThresholdCondition.getOperator()));
        xmlThresholdCondition.setAttribute(XML_SEVERITY_ATTR, getXmlSeverity(slaThresholdCondition.getSeverity()));
        xmlThresholdCondition.setAttribute(XML_VALUE_MIN_ATTR, Double.toString(slaThresholdCondition.getValue()));
        xmlThresholdCondition.setAttribute(XML_VALUE_MAX_ATTR, "Infinity");


        currentElement.appendChild(xmlThresholdCondition);
    }

    protected static String getXmlOperator(SlaThresholdCondition.Operator operator) {
        // TODAY it is operator.name() but we must know if this change
        switch (operator) {
            case GREATER_THAN: return "GREATER_THAN";
            case LESS_THAN: return "LESS_THAN";
            case EQUALS: return "EQUALS";
        }
        return "UNKNOWN_OPERATOR";
    }

    protected static String getXmlSeverity(SlaThresholdCondition.Severity severity) {
        // TODAY it is operator.name() but we must know if this change
        switch (severity) {
            case WARN: return "LOW";
            case FAIL: return "HIGH";
        }
        return "UNKNOWN_SEVERITY";
    }
}
