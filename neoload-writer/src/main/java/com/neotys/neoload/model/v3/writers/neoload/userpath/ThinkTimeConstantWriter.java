package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.ThinkTimeConstant;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ThinkTimeConstantWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "delay-action";
    public static final String XML_DURATION_ATT = "duration";
    public static final String XML_ISTHINKTIME_ATT = "isThinkTime";

    public ThinkTimeConstantWriter(ThinkTimeConstant thinktime) {
        super(thinktime);
    }

    public static ThinkTimeConstantWriter of(final ThinkTimeConstant thinktime) {
        return new ThinkTimeConstantWriter(thinktime);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        Element xmlDelay = document.createElement(XML_TAG_NAME);
        super.writeXML(document, xmlDelay, outputFolder);
        xmlDelay.setAttribute(XML_DURATION_ATT, ((ThinkTimeConstant)element).getValue());
        xmlDelay.setAttribute(XML_ISTHINKTIME_ATT, "true");
        currentElement.appendChild(xmlDelay);
    }
}
