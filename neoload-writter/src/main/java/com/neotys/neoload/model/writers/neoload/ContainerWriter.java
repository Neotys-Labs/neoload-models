package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.Container;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Optional;

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
	public void writeXML(final Document document, final Element currentElement, final String parentPath, final String outputFolder) {
		Element xmlContainerElement = document.createElement(tagName.orElse("basic-logical-action-container"));
		super.writeXML(document, xmlContainerElement, parentPath, outputFolder);
		currentElement.appendChild(xmlContainerElement);
		Container theContainer = ((Container) this.element);

		xmlContainerElement.setAttribute(XML_ELEMENT_NUMBER, DEFAULT_ELEMENT_NUMBER);
		xmlContainerElement.setAttribute(XML_EXECUTION_TYPE, DEFAULT_EXECUTION_TYPE);
		xmlContainerElement.setAttribute(XML_WEIGHTS_ENABLED, DEFAULT_WEIGHTS_ENABLED);

		theContainer.getChilds().forEach(elt -> {
			WriterUtils.generateEmbeddedAction(document, xmlContainerElement, elt, parentPath+"/"+theContainer.getName(), Optional.of(WriterUtils.WEIGHTED_ACTION_XML_TAG_NAME), true);
			WriterUtils.getWriterFor(elt).writeXML(document, document.getDocumentElement(), parentPath+"/"+theContainer.getName(), outputFolder);
		});

	}
}
