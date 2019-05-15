package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.project.SlaElement;
import org.w3c.dom.Element;

public class SlaElementWriter {

    public static final String XML_SLA_PROFILE_ENABLED_ATTR = "slaProfileEnabled";
    public static final String XML_SLA_PROFILE_NAME_ATTR = "slaProfileName";

    SlaElement element;

    public SlaElementWriter(SlaElement element) {this.element = element;}

    public static SlaElementWriter of(SlaElement slaElement) {return new SlaElementWriter(slaElement);}

    public void writeXML(final Element currentElement) {
        currentElement.setAttribute(XML_SLA_PROFILE_ENABLED_ATTR, Boolean.toString(element.getSlaProfile().isPresent()));
        element.getSlaProfile().ifPresent(slaProfileName -> currentElement.setAttribute(XML_SLA_PROFILE_NAME_ATTR, slaProfileName));
    }


}
