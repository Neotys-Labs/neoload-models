package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;

import com.neotys.neoload.model.repository.CounterNumberVariable;

public class CounterNumberVariableWriter extends VariableWriter{
	
	public static final String XML_TAG_NAME = "variable-counter";
	public static final String XML_ATTR_START_VAL = "starting";
	public static final String XML_ATTR_MAX_VAL = "max";
	public static final String XML_ATTR_INC_VAL = "inc";
	
	public CounterNumberVariableWriter(CounterNumberVariable variable) {
		super(variable);
	}
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;
		
		CounterNumberVariable theVariable = (CounterNumberVariable) variable;
		xmlVariable.setAttribute(XML_ATTR_START_VAL,	Integer.toString(theVariable.getStartValue()));
		xmlVariable.setAttribute(XML_ATTR_MAX_VAL,	Integer.toString(theVariable.getMaxValue()));
		xmlVariable.setAttribute(XML_ATTR_INC_VAL,	Integer.toString(theVariable.getIncrement()));
		
		currentElement.appendChild(xmlVariable);
	}
	
}
