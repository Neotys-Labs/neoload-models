package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.RegexpValidator;
import com.neotys.neoload.model.repository.TextValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Validator;

public abstract class ValidatorWriter {

    public static final String XML_ATTR_NAME = "name";
    public static final String XML_ATTR_NOT_TYPE = "notType";
    public static final String XML_ATTR_XPATH = "byXPath";

    public abstract void writeXML(final Document document, final Element currentElement);

    public void writeXML(final Element currentElement, Validator validator) {
        currentElement.setAttribute(XML_ATTR_NAME, validator.getName());
        currentElement.setAttribute(XML_ATTR_NOT_TYPE, Boolean.toString(!validator.getHaveToContains()));
        currentElement.setAttribute(XML_ATTR_XPATH, "false");
    }

    public static ValidatorWriter getWriterFor(Validator validator) {
        if(validator instanceof RegexpValidator) return RegexpValidatorWriter.of((RegexpValidator)validator);
        if(validator instanceof TextValidator) return TextValidatorWriter.of((TextValidator)validator);
        return null;
    }

}
