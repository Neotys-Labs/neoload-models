package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.LoadPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class LoadPolicyWriter {

    public static final String XML_TAG_NAME = "volume-policy-entry";

    protected final LoadPolicy loadPolicy;

    protected LoadPolicyWriter(LoadPolicy loadPolicy) {
        this.loadPolicy = loadPolicy;
    }

    public abstract void writeXML(Document document, Element currentElement);

    public Element createElement(final Document document, final Element currentElement) {
        Element xmlElement = document.createElement(XML_TAG_NAME);
        currentElement.appendChild(xmlElement);
        return xmlElement;
    }

}