package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.GoToNextIteration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GoToNextIterationWriter extends ElementWriter {

	public static final String XML_TAG_NAME = "gotonext-action";

	public GoToNextIterationWriter(GoToNextIteration goToNextIteration) {
		super(goToNextIteration);
	}

	public static GoToNextIterationWriter of(final GoToNextIteration goToNextIteration) {
		return new GoToNextIterationWriter(goToNextIteration);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlGoToNextIteration = document.createElement(XML_TAG_NAME);
		super.writeXML(document, xmlGoToNextIteration, outputFolder);
		currentElement.appendChild(xmlGoToNextIteration);
	}
}
