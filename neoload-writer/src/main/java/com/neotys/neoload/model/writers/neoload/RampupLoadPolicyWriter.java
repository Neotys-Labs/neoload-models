package com.neotys.neoload.model.writers.neoload;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.Duration.Type;
import com.neotys.neoload.model.scenario.RampupLoadPolicy;

class RampupLoadPolicyWriter extends LoadPolicyWriter {

	private static final String XML_TAG_NAME = "rampup-volume-policy";
	private static final String XML_ATTR_INITIALUSERNUMBER = "initialUserNumber";
	private static final String XML_ATTR_USERINCREMENT = "userIncrement";
	private static final String XML_ATTR_MAXUSERNUMBER = "maxUserNumber";
	private static final String XML_ATTR_ITERATIONNUMBER = "iterationNumber";
	private static final String XML_ATTR_DELAYINCREMENT = "delayIncrement";
	private static final String XML_ATTR_DELAYTYPEINCREMENT = "delayTypeIncrement";

    public RampupLoadPolicyWriter(final RampupLoadPolicy rampupLoadPolicy) {
    	super(rampupLoadPolicy);
    }

    @Override
    protected void writeVolumePolicyXML(Document document, Element currentElement) {
        final RampupLoadPolicy rampupLoadPolicy = (RampupLoadPolicy) this.loadPolicy;
        
    	final Element volumePolicyElement = super.createVolumePolicyElement(document, currentElement);
        Element xmlElement = document.createElement(XML_TAG_NAME);

        // Initial User Number attribute
        xmlElement.setAttribute(XML_ATTR_INITIALUSERNUMBER, String.valueOf(rampupLoadPolicy.getMinUsers()));
        // User Increment attribute
        xmlElement.setAttribute(XML_ATTR_USERINCREMENT, String.valueOf(rampupLoadPolicy.getIncrementUsers()));
        // Delay Increment & Delay Type Increment attribute
        final Duration incrementEvery = rampupLoadPolicy.getIncrementEvery();
        xmlElement.setAttribute(XML_ATTR_DELAYINCREMENT, String.valueOf(incrementEvery.getValue()));
        xmlElement.setAttribute(XML_ATTR_DELAYTYPEINCREMENT, "2");
        if (incrementEvery.getType() == Type.TIME) {           		
        	// Type is always seconds
           	xmlElement.setAttribute(XML_ATTR_DELAYTYPEINCREMENT, "1");
        }
        // Max User Number attribute        
        xmlElement.setAttribute(XML_ATTR_MAXUSERNUMBER, "0");
        Optional.ofNullable(rampupLoadPolicy.getMaxUsers()).ifPresent(maxUsers -> xmlElement.setAttribute(XML_ATTR_MAXUSERNUMBER, String.valueOf(maxUsers)));
        // Iteration Number attribute
        xmlElement.setAttribute(XML_ATTR_ITERATIONNUMBER, "1");
        Optional.ofNullable(rampupLoadPolicy.getDuration()).ifPresent(duration -> {
        	if (duration.getType() == Type.ITERATION) {
        		xmlElement.setAttribute(XML_ATTR_ITERATIONNUMBER, String.valueOf(duration.getValue()));
        	}
        });
        volumePolicyElement.appendChild(xmlElement);
    }
}
