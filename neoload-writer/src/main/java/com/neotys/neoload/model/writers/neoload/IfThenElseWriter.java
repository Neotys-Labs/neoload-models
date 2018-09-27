package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.IfThenElse;

public class IfThenElseWriter extends ElementWriter {

	private static final String XML_TAG_NAME = "if-action";	
	private static final String XML_TAG_THEN = "then-container";
	private static final String XML_TAG_ELSE= "else-container";	
    
	public IfThenElseWriter(final IfThenElse ifThenElse) {
		super(ifThenElse);	
	}

	public static IfThenElseWriter of(final IfThenElse ifThenElse) {
		return new IfThenElseWriter(ifThenElse);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element ifElement = document.createElement(XML_TAG_NAME);
		super.writeXML(document, ifElement, outputFolder);
		currentElement.appendChild(ifElement);
		
		final IfThenElse ifThenElse = ((IfThenElse) this.element);		
		ContainerForMultiWriter.of(ifThenElse.getThen(), XML_TAG_THEN).writeXML(document, ifElement, outputFolder);		
		ContainerForMultiWriter.of(ifThenElse.getElse(), XML_TAG_ELSE).writeXML(document, ifElement, outputFolder);
		ConditionsWriter.writeXML(document, ifElement, ifThenElse.getConditions());
	}
}
