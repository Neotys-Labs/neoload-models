package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.DelayConstant;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DelayConstantWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "delay-action";
    public static final String XML_DURATION_ATT = "duration";
    public static final String XML_ISTHINKTIME_ATT = "isThinkTime";

    public DelayConstantWriter(DelayConstant delay) {
        super(delay);
    }

    public static DelayConstantWriter of(final DelayConstant delay) {
        return new DelayConstantWriter(delay);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        Element xmlDelay = document.createElement(XML_TAG_NAME);
        super.writeXML(document, xmlDelay, outputFolder);
        xmlDelay.setAttribute(XML_DURATION_ATT, ((DelayConstant)element).getValue());
        xmlDelay.setAttribute(XML_ISTHINKTIME_ATT, "false");
        currentElement.appendChild(xmlDelay);
    }
}
