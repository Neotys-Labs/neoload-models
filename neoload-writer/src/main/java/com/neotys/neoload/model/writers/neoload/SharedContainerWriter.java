package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.Container;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SharedContainerWriter extends ContainerWriter {

	public static final String XML_TAG_NAME = "shared-element";

	public SharedContainerWriter(final Container container) {
		super(container);
	}

	public static SharedContainerWriter of(final Container container) {
		return new SharedContainerWriter(container);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		super.writeXML(document, currentElement, outputFolder);
		final org.w3c.dom.Element newElem = document.createElement(XML_TAG_NAME);
		newElem.setTextContent(WriterUtils.getElementUid(element));
		currentElement.appendChild(newElem);
	}
}
