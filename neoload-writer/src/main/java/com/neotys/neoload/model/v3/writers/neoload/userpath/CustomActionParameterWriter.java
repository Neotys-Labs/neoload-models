package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.CustomActionParameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustomActionParameterWriter {

	private static final String XML_CUSTOM_ACTION_PARAMETER_TAG = "custom-action-parameter";
	private static final String XML_NAME_ATT = "name";
	private static final String XML_TYPE_ATT = "type";	
	private static final String XML_VALUE_ATT = "value";
        
	private CustomActionParameterWriter() {		
	}
	
    public static void writeXML(final Document document, final Element currentElement, final CustomActionParameter customActionParameter) {
    	final Element element = document.createElement(XML_CUSTOM_ACTION_PARAMETER_TAG);
    	element.setAttribute(XML_NAME_ATT, customActionParameter.getName());
    	element.setAttribute(XML_TYPE_ATT, customActionParameter.getType().toString());
    	element.setAttribute(XML_VALUE_ATT, customActionParameter.getValue());
    	currentElement.appendChild(element);
    }  
}
