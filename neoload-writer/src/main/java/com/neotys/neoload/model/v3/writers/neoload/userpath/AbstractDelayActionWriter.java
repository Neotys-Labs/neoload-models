package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractDelayActionWriter extends ElementWriter {
    public static final String XML_TAG_NAME = "delay-action";
    public static final String XML_ATTRIBUTE_DURATION = "duration";
    public static final String XML_ATTRIBUTE_IS_THINK_TIME = "isThinkTime";
    public static final String XML_ATTRIBUTE_RANGE_START = "timeRangeStart";
    public static final String XML_ATTRIBUTE_RANGE_END = "timeRangeEnd";
    public static final String XML_ATTRIBUTE_TIME_MODE = "timeMode";
    public static final String MODE_RANGE_THINK_TIME = "MODE_RANGE_THINK_TIME";

    private static final Pattern patternThinkTime = Pattern.compile("(\\d+|\\$\\{\\w+\\})-(\\d+|\\$\\{\\w+\\})");


    private final String value;
    private final boolean thinkTime;

    protected AbstractDelayActionWriter(final Step delay, final String value, final boolean thinkTime) {
        super(delay);
        this.value = value;
        this.thinkTime = thinkTime;
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        Element element = document.createElement(XML_TAG_NAME);
        super.writeXML(document, element, outputFolder);

        final Matcher matcher = patternThinkTime.matcher(value);
        if(matcher.matches()){
            element.setAttribute(XML_ATTRIBUTE_TIME_MODE,MODE_RANGE_THINK_TIME);
            element.setAttribute(XML_ATTRIBUTE_RANGE_START, matcher.group(1));
            element.setAttribute(XML_ATTRIBUTE_RANGE_END, matcher.group(2));
        }else{
            element.setAttribute(XML_ATTRIBUTE_DURATION, value);
        }

        element.setAttribute(XML_ATTRIBUTE_IS_THINK_TIME, String.valueOf(thinkTime));
        currentElement.appendChild(element);
    }
}
