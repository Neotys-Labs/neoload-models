package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.DurationPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class DurationPolicyWriter {

    public final static String XML_TAG_NAME = "duration-policy-entry";
    public final static String XML_PARAM_TYPE = "type";

    protected final DurationPolicy durationPolicy;

    protected DurationPolicyWriter(DurationPolicy durationPolicy) {
        this.durationPolicy = durationPolicy;
    }

    protected abstract String getType();

    public abstract void writeXML(Document document, Element currentElement);

    public Element createElement(final Document document, final org.w3c.dom.Element currentElement) {
        Element xmlElement = document.createElement(XML_TAG_NAME);

        xmlElement.setAttribute(XML_PARAM_TYPE, getType());
        currentElement.appendChild(xmlElement);
        return xmlElement;
    }

}
