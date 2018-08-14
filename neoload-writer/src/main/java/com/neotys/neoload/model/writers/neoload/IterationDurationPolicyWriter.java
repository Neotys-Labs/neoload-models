package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.IterationDurationPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IterationDurationPolicyWriter extends DurationPolicyWriter {

    public IterationDurationPolicyWriter(IterationDurationPolicy durationPolicy) {
        super(durationPolicy);
    }

    @Override
    protected String getType() {
        return "1";
    }

    public void writeXML(Document document, Element currentElement) {
        Element xmlElement = super.createElement(document, currentElement);
        currentElement.appendChild(xmlElement);
    }
}
