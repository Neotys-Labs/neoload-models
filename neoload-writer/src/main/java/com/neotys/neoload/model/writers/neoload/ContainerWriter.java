package com.neotys.neoload.model.writers.neoload;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.IContainer;

public class ContainerWriter extends ElementWriter {

    public static final String XML_ELEMENT_NUMBER = "element-number";
    public static final String XML_EXECUTION_TYPE = "execution-type";
    public static final String XML_WEIGHTS_ENABLED = "weightsEnabled";

	private static final String DEFAULT_ELEMENT_NUMBER = "1";
	private static final String DEFAULT_EXECUTION_TYPE = "0";
	private static final String DEFAULT_WEIGHTS_ENABLED = "false";

	public ContainerWriter(final Container container) {
		super(container);
	}
	
	public static ContainerWriter of(final Container container) {
		return new ContainerWriter(container);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlContainerElement = document.createElement("basic-logical-action-container");
		super.writeXML(document, xmlContainerElement, outputFolder);
		currentElement.appendChild(xmlContainerElement);
		final IContainer theContainer = ((Container) this.element);
		setPropertyAttributes(xmlContainerElement);
		writeEmbeddedActions(document, outputFolder, xmlContainerElement, theContainer);
	}

	protected static void writeEmbeddedActions(final Document document, final String outputFolder, final Element xmlContainerElement, final IContainer theContainer) {
		theContainer.getChilds().forEach(elt -> {
			WriterUtils.generateEmbeddedAction(document, xmlContainerElement, elt, Optional.of(WriterUtils.WEIGHTED_ACTION_XML_TAG_NAME), true);
			// we don't write the definition of embedded shared containers
			if (!(elt instanceof Container) || !((Container) elt).isShared()) {
				WriterUtils.<ElementWriter>getWriterFor(elt).writeXML(document, document.getDocumentElement(), outputFolder);
			}
		});
	}
	
	protected static void setPropertyAttributes(final Element xmlContainerElement){
		xmlContainerElement.setAttribute(XML_ELEMENT_NUMBER, DEFAULT_ELEMENT_NUMBER);
		xmlContainerElement.setAttribute(XML_EXECUTION_TYPE, DEFAULT_EXECUTION_TYPE);
		xmlContainerElement.setAttribute(XML_WEIGHTS_ENABLED, DEFAULT_WEIGHTS_ENABLED);
	}

}
