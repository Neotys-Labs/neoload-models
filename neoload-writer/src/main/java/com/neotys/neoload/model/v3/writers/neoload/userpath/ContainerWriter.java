package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.SlaElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.userpath.assertion.AssertionsWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Optional;

public class ContainerWriter extends ElementWriter {

    public static final String XML_ELEMENT_NUMBER = "element-number";
    public static final String XML_EXECUTION_TYPE = "execution-type";
    public static final String XML_WEIGHTS_ENABLED = "weightsEnabled";

	private static final String DEFAULT_ELEMENT_NUMBER = "1";
	private static final String DEFAULT_EXECUTION_TYPE = "0";
	private static final String DEFAULT_WEIGHTS_ENABLED = "false";

	public static final String DEFAULT_TAG_NAME = "basic-logical-action-container";

	private String tagName = DEFAULT_TAG_NAME;

	public ContainerWriter(final Container container) {
		super(container);
	}

	public ContainerWriter(final Container container, String tagName) {
		super(container);
		this.tagName = tagName;
	}
	
	public static ContainerWriter of(final Container container) {
		return new ContainerWriter(container);
	}

	public static ContainerWriter of(final Container container, String tagName) {
		return new ContainerWriter(container, tagName);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlContainerElement = document.createElement(tagName);
		final Container theContainer = ((Container) this.element);
		if(tagName.equals(DEFAULT_TAG_NAME)) {
			super.writeXML(document, xmlContainerElement, outputFolder);
		} else{
				writeDescription(document, xmlContainerElement);
		}
		//In all containers
		SlaElementWriter.of(theContainer).writeXML(xmlContainerElement);
		currentElement.appendChild(xmlContainerElement);

		setPropertyAttributes(xmlContainerElement);
		writeEmbeddedActions(document, outputFolder, xmlContainerElement, theContainer);
		
		// write assertions
        final List<ContentAssertion> assertions = theContainer.getContentAssertions();
        if ((assertions != null && (!assertions.isEmpty()))) {
        	AssertionsWriter.of(assertions).writeXML(document, xmlContainerElement);	
        } 
	}

	protected static void writeEmbeddedActions(final Document document, final String outputFolder, final Element xmlContainerElement, final Container theContainer) {
		theContainer.getSteps().forEach(elt -> {
			WriterUtils.generateEmbeddedAction(document, xmlContainerElement, elt, Optional.of(WriterUtils.WEIGHTED_ACTION_XML_TAG_NAME), true);
			WriterUtils.<ElementWriter>getWriterFor(elt).writeXML(document, document.getDocumentElement(), outputFolder);
		});
	}
	
	protected static void setPropertyAttributes(final Element xmlContainerElement){
		xmlContainerElement.setAttribute(XML_ELEMENT_NUMBER, DEFAULT_ELEMENT_NUMBER);
		xmlContainerElement.setAttribute(XML_EXECUTION_TYPE, DEFAULT_EXECUTION_TYPE);
		xmlContainerElement.setAttribute(XML_WEIGHTS_ENABLED, DEFAULT_WEIGHTS_ENABLED);
	}

}
