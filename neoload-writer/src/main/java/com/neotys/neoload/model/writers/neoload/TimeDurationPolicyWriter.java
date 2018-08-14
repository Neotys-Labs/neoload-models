package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.TimeDurationPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TimeDurationPolicyWriter extends DurationPolicyWriter {

    public static final String XML_PARAM_TIME = "time";
    public static final String XML_PARAM_TIME_UNIT = "timeUnit";

    public TimeDurationPolicyWriter(TimeDurationPolicy durationPolicy) {
        super(durationPolicy);
    }

    @Override
    protected String getType() {
        return "2";
    }

    public void writeXML(Document document, Element currentElement) {
        Element xmlElement = super.createElement(document, currentElement);
        xmlElement.setAttribute(XML_PARAM_TIME, String.valueOf(((TimeDurationPolicy)this.durationPolicy).getDuration()));
        xmlElement.setAttribute(XML_PARAM_TIME_UNIT, "0");

        currentElement.appendChild(xmlElement);
    }
}