package com.neotys.neoload.model.writers.neoload;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Container;

public class ContainerForMultiWriter extends ContainerWriter {

	private ContainerForMultiWriter(final Container container, final String tagName) {
		super(container, Optional.of(tagName));
	}

	public static ContainerForMultiWriter of(final Container container, final String tagName) {
		return new ContainerForMultiWriter(container, tagName);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlContainerElement = document.createElement(getTagName().get());		
		currentElement.appendChild(xmlContainerElement);
		Container theContainer = ((Container) this.element);
		setPropertyAttributes(xmlContainerElement);
		writeEmbeddedActions(document, outputFolder, xmlContainerElement, theContainer);
	}
}
