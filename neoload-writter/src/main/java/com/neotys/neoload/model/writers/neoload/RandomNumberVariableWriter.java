package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import com.neotys.neoload.model.repository.RandomNumberVariable;

public class RandomNumberVariableWriter extends VariableWriter{
	
	public static final String XML_TAG_NAME = "variable-random-number";
	public static final String XML_ATTR_MIN_VAL = "min-value";
	public static final String XML_ATTR_MAX_VAL = "max-value";

	public RandomNumberVariableWriter(RandomNumberVariable variable) {
		super(variable);
	}
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;
		
		RandomNumberVariable theVariable = (RandomNumberVariable) variable;
		xmlVariable.setAttribute(XML_ATTR_MIN_VAL,	Integer.toString(theVariable.getMinValue()));
		xmlVariable.setAttribute(XML_ATTR_MAX_VAL,	Integer.toString(theVariable.getMaxValue()));
		
		currentElement.appendChild(xmlVariable);
	}
	
}
