package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.RegexpValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RegexpValidatorWriter extends ValidatorWriter {

    public static final String XML_TAG_NAME = "assertion-response";
    public static final String XML_TAG_STRING = "testString";

    private final RegexpValidator validator;


    public RegexpValidatorWriter(RegexpValidator validator) {
        this.validator = validator;
    }

    public static RegexpValidatorWriter of(RegexpValidator validator) {
        return new RegexpValidatorWriter(validator);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement) {

        Element xmlValidator = document.createElement(XML_TAG_NAME);
        super.writeXML(xmlValidator, validator);
        Element xmlStringDocument = document.createElement(XML_TAG_STRING);
        xmlStringDocument.setTextContent(validator.getValidationRegex());
        xmlValidator.appendChild(xmlStringDocument);
        currentElement.appendChild(xmlValidator);
    }
}
