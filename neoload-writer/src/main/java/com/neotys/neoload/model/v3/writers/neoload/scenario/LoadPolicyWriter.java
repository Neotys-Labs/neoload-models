package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Optional;

public abstract class LoadPolicyWriter {
    // duration-policy-entry
	private static final String XML_TAG_DURATION_POLICY_NAME = "duration-policy-entry";
	private static final String XML_ATTR_TYPE = "type";    
	private static final String XML_ATTR_TIME = "time";
	private static final String XML_ATTR_TIMEUNIT = "timeUnit";
	
    // volume-policy-entry
	private static final String XML_TAG_VOLUME_POLICY_NAME = "volume-policy-entry";

	// start-stop-policy-entry
    private static final String XML_TAG_START_STOP_POLICY_NAME = "start-stop-policy-entry";
    private static final String XML_ATTR_START_TYPE = "start-type";
    private static final String XML_ATTR_START_DELAY = "start-delay";
    private static final String XML_ATTR_START_POPULATION = "start-population";    
    private static final String XML_ATTR_STOP_TYPE = "stop-type";
    private static final String XML_ATTR_STOP_DELAY = "stop-delay";

	// runtime-policy
    private static final String XML_TAG_RUNTIME_POLICY_NAME = "runtime-policy";    
    private static final String XML_ATTR_VU_START_MODE = "vuStartMode";
    private static final String XML_ATTR_VU_START_DELAY = "vuStartDelay";

    protected final LoadPolicy loadPolicy;

    public LoadPolicyWriter(final LoadPolicy loadPolicy) {
        this.loadPolicy = loadPolicy;
    }

    protected Element createVolumePolicyElement(final Document document, final Element currentElement) {
        Element xmlElement = document.createElement(XML_TAG_VOLUME_POLICY_NAME);
        currentElement.appendChild(xmlElement);
        return xmlElement;
    }

    private void writeDurationPolicyXML(Document document, Element currentElement) {
        final Element xmlElement = document.createElement(XML_TAG_DURATION_POLICY_NAME);

        xmlElement.setAttribute(XML_ATTR_TYPE, "0");
        Optional.ofNullable(loadPolicy.getDuration()).ifPresent(duration -> {
        	if (duration.getType() == LoadDuration.Type.TIME) {
        		xmlElement.setAttribute(XML_ATTR_TYPE, "2");
                xmlElement.setAttribute(XML_ATTR_TIME, String.valueOf(duration.getValue()));
                xmlElement.setAttribute(XML_ATTR_TIMEUNIT, "0");
        	}
        	else {
        		xmlElement.setAttribute(XML_ATTR_TYPE, "1");
        	}
        });        
        currentElement.appendChild(xmlElement);
        
    }

    protected abstract void writeVolumePolicyXML(Document document, Element currentElement);

    private void writeStartStopPolicyXML(final Document document, final Element currentElement) {
    	Element xmlElement = document.createElement(XML_TAG_START_STOP_POLICY_NAME);

    	xmlElement.setAttribute(XML_ATTR_START_TYPE, "0");
    	Optional.ofNullable(loadPolicy.getStartAfter()).ifPresent(startAfter -> {
        	if (startAfter.getType() == StartAfter.Type.TIME) {
        		xmlElement.setAttribute(XML_ATTR_START_TYPE, "1");
                xmlElement.setAttribute(XML_ATTR_START_DELAY, String.valueOf((Integer)startAfter.getValue() * 1000));
        	}
        	else {
        		xmlElement.setAttribute(XML_ATTR_START_TYPE, "2");
                xmlElement.setAttribute(XML_ATTR_START_POPULATION, String.valueOf(startAfter.getValue()));
        	}
        });  
    	xmlElement.setAttribute(XML_ATTR_STOP_TYPE, "0");
    	Optional.ofNullable(loadPolicy.getStopAfter()).ifPresent(stopAfter -> {
        	if (stopAfter.getType() == StopAfter.Type.TIME) {
        		xmlElement.setAttribute(XML_ATTR_STOP_TYPE, "1");
                xmlElement.setAttribute(XML_ATTR_STOP_DELAY, String.valueOf((Integer)stopAfter.getValue() * 1000));
        	}
        	else {
        		xmlElement.setAttribute(XML_ATTR_STOP_TYPE, "2");
        	}
        });
        currentElement.appendChild(xmlElement);    	
    }
    
    private void writeRuntimePolicyXML(final Document document, final Element currentElement) {
    	Element xmlElement = document.createElement(XML_TAG_RUNTIME_POLICY_NAME);

    	xmlElement.setAttribute(XML_ATTR_VU_START_MODE, "0");
    	Optional.ofNullable(loadPolicy.getRampup()).ifPresent(duration -> {
        	xmlElement.setAttribute(XML_ATTR_VU_START_MODE, "1");
            xmlElement.setAttribute(XML_ATTR_VU_START_DELAY, String.valueOf(duration * 1000));
        });
        currentElement.appendChild(xmlElement);    	
    }
    
    public void writeXML(final Document document, final Element currentElement) {
    	writeDurationPolicyXML(document, currentElement);
    	writeVolumePolicyXML(document, currentElement);
    	writeStartStopPolicyXML(document, currentElement);
    	writeRuntimePolicyXML(document, currentElement);
    }
}