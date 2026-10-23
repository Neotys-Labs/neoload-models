package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DelayWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "delay-action";
    public static final String XML_DURATION_ATT = "duration";
    public static final String XML_ISTHINKTIME_ATT = "isThinkTime";

    public DelayWriter(Delay delay) {
        super(delay);
    }

    private static String toXml(final MillisecondsDuration duration) {
        return duration.isLiteral() ? String.valueOf(duration.getAmount()) : duration.toText();
    }

    public static DelayWriter of(final Delay delay) {
        return new DelayWriter(delay);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        final Delay delay = (Delay) element;
        Element xmlDelay = document.createElement(XML_TAG_NAME);
        super.writeXML(document, xmlDelay, outputFolder);
        xmlDelay.setAttribute(XML_DURATION_ATT, toXml(((DurationConstant) delay.getDuration()).getValue()));
        xmlDelay.setAttribute(XML_ISTHINKTIME_ATT, String.valueOf(delay.isThinkTime()));
        currentElement.appendChild(xmlDelay);
    }
}
