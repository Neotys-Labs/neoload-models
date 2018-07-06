package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;

import com.neotys.neoload.model.repository.ConstantVariable;

public class ConstantVariableWriter extends VariableWriter {

	public static final String XML_TAG_NAME = "variable-constant";
	public static final String XML_CONST_VALUE = "constantValue";
	
	protected ConstantVariableWriter(ConstantVariable variable) {
		super(variable);
	}
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;
		ConstantVariable theVariable = (ConstantVariable) variable;
		xmlVariable.setAttribute(XML_CONST_VALUE, theVariable.getConstantValue());
		writeDescription(document, xmlVariable);
		currentElement.appendChild(xmlVariable);
	}
	
	// needs a refactor to be put in an upper class or in an util class (duplication from ElementWritter). The original function might have a bug!!!
    public void writeDescription(final Document document, final org.w3c.dom.Element currentElement) {
        this.variable.getDescription().ifPresent(s -> {
            org.w3c.dom.Element descElement = document.createElement(ElementWriter.XML_DESCRIPTION_TAG);
            descElement.setTextContent(s);
            currentElement.appendChild(descElement);
        });
    }
}
