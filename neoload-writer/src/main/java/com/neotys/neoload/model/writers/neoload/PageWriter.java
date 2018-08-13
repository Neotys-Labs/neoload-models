package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.Page;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PageWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "http-page";
    public static final String XML_ATTRIBUTE_THINK_TIME = "thinkTime";
    public static final String XML_ATTRIBUTE_EXECUTE_RESOURCES_DYNAMICALLY = "executeResourcesDynamically";

    public PageWriter(Page page) {
        super(page);
    }
    
    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        Element xmlPage = document.createElement(XML_TAG_NAME);
        super.writeXML(document, xmlPage, outputFolder);
        Page thePage = (Page) this.element;
        xmlPage.setAttribute(XML_ATTRIBUTE_THINK_TIME, Integer.toString(thePage.getThinkTime()));
        xmlPage.setAttribute(XML_ATTRIBUTE_EXECUTE_RESOURCES_DYNAMICALLY, Boolean.toString(thePage.isDynamic()));
        currentElement.appendChild(xmlPage);
        thePage.getChilds().forEach(pageElem -> {
        	WriterUtils.generateEmbeddedAction(document, xmlPage, pageElem);
        	WriterUtils.getWriterFor(pageElem, ElementWriter.class).writeXML(document, currentElement, outputFolder);
        });
    }
}
