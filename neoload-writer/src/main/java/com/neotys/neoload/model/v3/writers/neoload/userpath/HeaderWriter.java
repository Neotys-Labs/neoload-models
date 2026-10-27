package com.neotys.neoload.model.v3.writers.neoload.userpath;

import java.util.Optional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HeaderWriter {

	public static final String XML_TAG_NAME = "header";
	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_VALUE = "value";

	private HeaderWriter() {}

	public static void writeXML(final Document document, final Element currentElement, final String name, final Optional<String> value) {
		final Element headerElement = document.createElement(XML_TAG_NAME);
		headerElement.setAttribute(XML_ATTR_NAME, name);
		headerElement.setAttribute(XML_ATTR_VALUE, value.orElse(""));
		currentElement.appendChild(headerElement);
	}
}
