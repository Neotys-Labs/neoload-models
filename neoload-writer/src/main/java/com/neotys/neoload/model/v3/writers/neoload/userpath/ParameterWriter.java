package com.neotys.neoload.model.v3.writers.neoload.userpath;

import java.util.Optional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParameterWriter {

    public static final String XML_TAG_NAME = "parameter";
    public static final String XML_VALUE = "value";
    public static final String XML_NAME = "name";
    public static final String XML_SEPARATOR = "separator";

    private ParameterWriter() {}

    public static void writeXML(final Document document, final Element currentElement, final Optional<String> tagName, final String name, final Optional<String> value) {
        Element xmlParam = document.createElement(tagName.orElse(XML_TAG_NAME));
        xmlParam.setAttribute(XML_NAME, name);
        xmlParam.setAttribute(XML_VALUE, value.orElse(""));
        xmlParam.setAttribute(XML_SEPARATOR, value.map(v -> "=").orElse(""));
        currentElement.appendChild(xmlParam);
    }
}
