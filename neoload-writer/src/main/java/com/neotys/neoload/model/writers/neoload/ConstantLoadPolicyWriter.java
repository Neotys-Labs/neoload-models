package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.scenario.Duration.Type;

class ConstantLoadPolicyWriter extends LoadPolicyWriter {

	private static final String XML_TAG_NAME = "constant-volume-policy";
	private static final String XML_ATTR_USERNUMBER = "userNumber";
	private static final String XML_ATTR_ITERATIONNUMBER = "iterationNumber";

    public ConstantLoadPolicyWriter(final ConstantLoadPolicy constantLoadPolicy) { 
    	super(constantLoadPolicy);
    }

    @Override
    protected void writeVolumePolicyXML(Document document, Element currentElement) {
    	final ConstantLoadPolicy constantLoadPolicy = (ConstantLoadPolicy)this.loadPolicy;
    	
    	final Element volumePolicyElement = super.createVolumePolicyElement(document, currentElement);
        final Element xmlElement = document.createElement(XML_TAG_NAME);
        
        // UserNumber attribute
        xmlElement.setAttribute(XML_ATTR_USERNUMBER, String.valueOf(constantLoadPolicy.getUsers()));
        // IterationNumber attribute
        constantLoadPolicy.getDuration().ifPresent(duration -> {
        	if (duration.getType() == Type.ITERATION) {
        		xmlElement.setAttribute(XML_ATTR_ITERATIONNUMBER, String.valueOf(duration.getValue()));
        	}
        });
        volumePolicyElement.appendChild(xmlElement);
    }
}
