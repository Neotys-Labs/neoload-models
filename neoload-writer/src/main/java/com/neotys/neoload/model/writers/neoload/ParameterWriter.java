package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Parameter;

import java.util.Optional;

public class ParameterWriter {

    public static final String XML_TAG_NAME = "parameter";
    public static final String XML_VALUE = "value";
    public static final String XML_NAME = "name";

    private final Parameter parameter;

    public ParameterWriter(Parameter parameter) {
        this.parameter = parameter;
    }

    public void writeXML(final Document document, final Element currentElement, Optional<String> tagName) {
        Element xmlParam = document.createElement(tagName.orElse(XML_TAG_NAME));
        xmlParam.setAttribute(XML_NAME, parameter.getName());
        parameter.getValue().ifPresent(value -> xmlParam.setAttribute(XML_VALUE, value));
        currentElement.appendChild(xmlParam);
    }

	public static ParameterWriter of(Parameter paramElem) {
		return new ParameterWriter(paramElem);
	}

}
