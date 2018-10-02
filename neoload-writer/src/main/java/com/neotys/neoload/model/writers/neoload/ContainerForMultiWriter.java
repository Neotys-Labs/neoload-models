package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.ContainerForMulti;
import com.neotys.neoload.model.repository.IContainer;

public class ContainerForMultiWriter extends ElementWriter {

	public ContainerForMultiWriter(final ContainerForMulti containerForMulti) {
		super(containerForMulti);
	}

	public static ContainerForMultiWriter of(final ContainerForMulti containerForMulti) {
		return new ContainerForMultiWriter(containerForMulti);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlContainerElement = document.createElement(((ContainerForMulti) this.element).getTag());		
		currentElement.appendChild(xmlContainerElement);
		IContainer theContainer = ((IContainer) this.element);
		ContainerWriter.setPropertyAttributes(xmlContainerElement);
		ContainerWriter.writeEmbeddedActions(document, outputFolder, xmlContainerElement, theContainer);
	}
}
