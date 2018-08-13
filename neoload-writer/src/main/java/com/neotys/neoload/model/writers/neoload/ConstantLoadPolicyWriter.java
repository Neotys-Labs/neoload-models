package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConstantLoadPolicyWriter extends LoadPolicyWriter {

    public static final String XML_TAG_NAME = "constant-volume-policy";
    public static final String XML_USERNUMBER_ATTR = "userNumber";
    public static final String XML_ITERATION_ATTR = "iterationNumber";

    public ConstantLoadPolicyWriter(ConstantLoadPolicy constantLoadPolicy) { super(constantLoadPolicy); }

    @Override
    public void writeXML(Document document, Element currentElement) {
        Element volumePolicyElement = super.createElement(document, currentElement);
        Element xmlElement = document.createElement(XML_TAG_NAME);
        xmlElement.setAttribute(XML_USERNUMBER_ATTR, String.valueOf(((ConstantLoadPolicy)this.loadPolicy).getLoad()));
        this.loadPolicy.getIterationNumber().ifPresent(iterations -> xmlElement.setAttribute(XML_ITERATION_ATTR, String.valueOf(iterations)));
        volumePolicyElement.appendChild(xmlElement);
    }
}
