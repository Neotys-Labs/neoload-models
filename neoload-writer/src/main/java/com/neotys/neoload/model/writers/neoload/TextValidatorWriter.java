package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.TextValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextValidatorWriter extends ValidatorWriter {

    public static final String XML_TAG_NAME = "assertion-content";
    public static final String XML_ATTR_PATTERN = "pattern";

    private final TextValidator validator;


    public TextValidatorWriter(TextValidator validator) {
        this.validator = validator;
    }

    public static TextValidatorWriter of(TextValidator validator) {
        return new TextValidatorWriter(validator);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement) {

        Element xmlValidator = document.createElement(XML_TAG_NAME);
        super.writeXML(xmlValidator, validator);
        xmlValidator.setAttribute(XML_ATTR_PATTERN, this.validator.getValidationText());
        currentElement.appendChild(xmlValidator);
    }
}
