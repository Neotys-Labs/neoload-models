package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import org.w3c.dom.Document;

public class ConstantVariableWriter extends VariableWriter {

	public static final String XML_TAG_NAME = "variable-constant";
	public static final String XML_CONST_VALUE = "constantValue";

	public ConstantVariableWriter(ConstantVariable variable) {
		super(variable);
	}
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable);
		ConstantVariable theVariable = (ConstantVariable) variable;
		xmlVariable.setAttribute(XML_CONST_VALUE, theVariable.getValue());
		writeDescription(document, xmlVariable);
		currentElement.appendChild(xmlVariable);
	}

}
