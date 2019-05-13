package com.neotys.neoload.model.v3.writers.neoload.variable;


import com.neotys.neoload.model.v3.project.variable.CounterVariable;
import org.w3c.dom.Document;

public class CounterVariableWriter extends VariableWriter{
	
	public static final String XML_TAG_NAME = "variable-counter";
	public static final String XML_ATTR_START_VAL = "starting";
	public static final String XML_ATTR_MAX_VAL = "max";
	public static final String XML_ATTR_INC_VAL = "inc";
	
	public CounterVariableWriter(CounterVariable variable) {
		super(variable);
	}
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;

		CounterVariable theVariable = (CounterVariable) variable;
		xmlVariable.setAttribute(XML_ATTR_START_VAL,	Integer.toString(theVariable.getStart()));
		xmlVariable.setAttribute(XML_ATTR_MAX_VAL,	Integer.toString(theVariable.getEnd()));
		xmlVariable.setAttribute(XML_ATTR_INC_VAL,	Integer.toString(theVariable.getIncrement()));
		writeDescription(document, xmlVariable);
		currentElement.appendChild(xmlVariable);
	}
	
}
