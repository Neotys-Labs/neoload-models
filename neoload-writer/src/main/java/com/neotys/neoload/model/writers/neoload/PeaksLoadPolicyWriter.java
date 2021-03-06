package com.neotys.neoload.model.writers.neoload;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.scenario.Duration;
import com.neotys.neoload.model.scenario.Duration.Type;
import com.neotys.neoload.model.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.scenario.PeaksLoadPolicy;

class PeaksLoadPolicyWriter extends LoadPolicyWriter {

	private static final String XML_TAG_NAME = "peaks-volume-policy";
    
	private static final String XML_ATTR_MINLOAD = "defaultUserNumber";
	private static final String XML_ATTR_MINDURATION = "defaultDelay";
	private static final String XML_ATTR_MINTYPE = "defaultDelayType";
	private static final String XML_ATTR_MAXLOAD = "peakUserNumber";
	private static final String XML_ATTR_MAXDURATION = "peakDelay";
	private static final String XML_ATTR_MAXTYPE = "peakDelayType";
	private static final String XML_ATTR_STARTPOLICY = "startPoint";
	private static final String XML_ATTR_ITERATIONNUMBER = "iterationNumber";

    public PeaksLoadPolicyWriter(final PeaksLoadPolicy peaksLoadPolicy) {
    	super(peaksLoadPolicy);
    }

    @Override
    protected void writeVolumePolicyXML(Document document, Element currentElement) {
        final PeaksLoadPolicy peaksLoadPolicy = (PeaksLoadPolicy) this.loadPolicy;
        
        final Element volumePolicyElement = super.createVolumePolicyElement(document, currentElement);
        final Element xmlElement = document.createElement(XML_TAG_NAME);

        // Default User Number attribute
        final PeakLoadPolicy minimumLoadPolicy = peaksLoadPolicy.getMinimum();
        xmlElement.setAttribute(XML_ATTR_MINLOAD, String.valueOf(minimumLoadPolicy.getUsers()));
        // Default Delay attribute
        final Duration minimumDuration = minimumLoadPolicy.getDuration();
        xmlElement.setAttribute(XML_ATTR_MINDURATION, String.valueOf(minimumDuration.getValue()));
        // Default Delay Type attribute
        xmlElement.setAttribute(XML_ATTR_MINTYPE, "2");
        if (minimumDuration.getType() == Type.TIME) {           		
        	// Type is always seconds
            xmlElement.setAttribute(XML_ATTR_MINTYPE, "1");
        }                 

        // Peak User Number attribute
        final PeakLoadPolicy maximumLoadPolicy = peaksLoadPolicy.getMaximum();
        xmlElement.setAttribute(XML_ATTR_MAXLOAD, String.valueOf(maximumLoadPolicy.getUsers()));
        // Peak Delay attribute
        final Duration maximumDuration = maximumLoadPolicy.getDuration();
        xmlElement.setAttribute(XML_ATTR_MAXDURATION, String.valueOf(maximumDuration.getValue()));
        // Peak Delay Type attribute
        xmlElement.setAttribute(XML_ATTR_MAXTYPE, "2");
        if (maximumDuration.getType() == Type.TIME) {           		
        	// Type is always seconds
            xmlElement.setAttribute(XML_ATTR_MAXTYPE, "1");
        }          
       
        // Start Point attribute
        xmlElement.setAttribute(XML_ATTR_STARTPOLICY, "0"); // Minimum
        if(peaksLoadPolicy.getStart() == PeaksLoadPolicy.Peak.MAXIMUM) {
            xmlElement.setAttribute(XML_ATTR_STARTPOLICY, "1");
        }
        
        // Iteration Number attribute
        xmlElement.setAttribute(XML_ATTR_ITERATIONNUMBER, "1");
        Optional.ofNullable(peaksLoadPolicy.getDuration()).ifPresent(duration -> {
        	if (duration.getType() == Type.ITERATION) {
        		xmlElement.setAttribute(XML_ATTR_ITERATIONNUMBER, String.valueOf(duration.getValue()));
        	}
        });
        
        volumePolicyElement.appendChild(xmlElement);
    }
}
