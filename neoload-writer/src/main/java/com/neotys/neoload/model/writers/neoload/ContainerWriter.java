package com.neotys.neoload.model.writers.neoload;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Container;

public class ContainerWriter extends ElementWriter {

	private final Optional<String> tagName;

    public static final String XML_ELEMENT_NUMBER = "element-number";
    public static final String XML_EXECUTION_TYPE = "execution-type";
    public static final String XML_WEIGHTS_ENABLED = "weightsEnabled";

	private static final String DEFAULT_ELEMENT_NUMBER = "1";
	private static final String DEFAULT_EXECUTION_TYPE = "0";
	private static final String DEFAULT_WEIGHTS_ENABLED = "false";

	public ContainerWriter(Container container) {
		super(container);
		this.tagName = Optional.empty();
	}

	public ContainerWriter(final Container container, Optional<String> tagName) {
		super(container);
		this.tagName = tagName;
	}

	public static ContainerWriter of(Container container) {
		return new ContainerWriter(container, Optional.empty());
	}

	public static ContainerWriter of(Container container, String tagName) {
		return new ContainerWriter(container, Optional.ofNullable(tagName));
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlContainerElement = document.createElement(tagName.orElse("basic-logical-action-container"));
		super.writeXML(document, xmlContainerElement, outputFolder);
		currentElement.appendChild(xmlContainerElement);
		Container theContainer = ((Container) this.element);
		setPropertyAttributes(xmlContainerElement);
		writeEmbeddedActions(document, outputFolder, xmlContainerElement, theContainer);
	}

	protected static void writeEmbeddedActions(final Document document, final String outputFolder, Element xmlContainerElement, Container theContainer) {
		theContainer.getChilds().forEach(elt -> {
			WriterUtils.generateEmbeddedAction(document, xmlContainerElement, elt, Optional.of(WriterUtils.WEIGHTED_ACTION_XML_TAG_NAME), true);
			WriterUtils.<ElementWriter>getWriterFor(elt).writeXML(document, document.getDocumentElement(), outputFolder);
		});
	}
	
	protected void setPropertyAttributes(final Element xmlContainerElement){
		xmlContainerElement.setAttribute(XML_ELEMENT_NUMBER, DEFAULT_ELEMENT_NUMBER);
		xmlContainerElement.setAttribute(XML_EXECUTION_TYPE, DEFAULT_EXECUTION_TYPE);
		xmlContainerElement.setAttribute(XML_WEIGHTS_ENABLED, DEFAULT_WEIGHTS_ENABLED);
	}
	
	public Optional<String> getTagName() {
		return tagName;
	}
}
