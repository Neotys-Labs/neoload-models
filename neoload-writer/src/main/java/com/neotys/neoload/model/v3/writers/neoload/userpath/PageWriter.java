package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Page;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.SlaElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "http-page";
    public static final String XML_ATTRIBUTE_THINK_TIME = "thinkTime";
    public static final String XML_ATTRIBUTE_THINK_TIME_START =  "thinkTimeRangeStart";
    public static final String XML_ATTRIBUTE_THINK_TIME_STOP = "thinkTimeRangeEnd";
    public static final String XML_ATTRIBUTE_THINK_TIME_MODE = "thinkTimeMode";

    public static final String MODE_RANGE_THINK_TIME = "MODE_RANGE_THINK_TIME";

    public static final String XML_ATTRIBUTE_EXECUTE_RESOURCES_DYNAMICALLY = "executeResourcesDynamically";

    private static Pattern patternThinkTime = Pattern.compile("(\\d+|\\$\\{\\w+\\})-(\\d+|\\$\\{\\w+\\})");

    public PageWriter(final Page page) {
        super(page);
    }

    public static PageWriter of(final Page page){
        return new PageWriter(page);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        final Element xmlPage = document.createElement(XML_TAG_NAME);
        super.writeXML(document, xmlPage, outputFolder);
        final Page thePage = (Page) this.element;
        thePage.getThinkTime().ifPresent(thinkTime -> writeDelay(xmlPage, thinkTime));
        xmlPage.setAttribute(XML_ATTRIBUTE_EXECUTE_RESOURCES_DYNAMICALLY, Boolean.toString(thePage.isDynamic()));
        currentElement.appendChild(xmlPage);
        SlaElementWriter.of(thePage).writeXML(xmlPage);
        thePage.getChildren().forEach(pageElem -> {
        	WriterUtils.generateEmbeddedAction(document, xmlPage, pageElem);
        	WriterUtils.<ElementWriter>getWriterFor(pageElem).writeXML(document, currentElement, outputFolder);
        });
    }

    private void writeDelay(final Element xmlPage, final String thinkTime) {
        final Matcher matcher = patternThinkTime.matcher(thinkTime);
        if(matcher.matches()){
            xmlPage.setAttribute(XML_ATTRIBUTE_THINK_TIME_MODE,MODE_RANGE_THINK_TIME);
            xmlPage.setAttribute(XML_ATTRIBUTE_THINK_TIME_START, matcher.group(1));
            xmlPage.setAttribute(XML_ATTRIBUTE_THINK_TIME_STOP, matcher.group(2));
        }else{
            xmlPage.setAttribute(XML_ATTRIBUTE_THINK_TIME,thinkTime);
        }
    }
}
