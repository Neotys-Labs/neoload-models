package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.PopulationSplit;

public class PopulationSplitWriter {

    public static final String XML_TAG_NAME = "split";
    public static final String XML_FACTOR_ATTR = "factor";
    public static final String XML_VUUID_ATTR = "virtualUserUid";

    private final PopulationSplit split;

    public PopulationSplitWriter(PopulationSplit split) {
        this.split = split;
    }

    public static PopulationSplitWriter of(PopulationSplit split) {
        return new PopulationSplitWriter(split);
    }

    public void writeXML(final Document document, final Element currentElement) {
        Element xmlParam = document.createElement(XML_TAG_NAME);
        xmlParam.setAttribute(XML_FACTOR_ATTR, String.valueOf(split.getPercentage()));
        xmlParam.setAttribute(XML_VUUID_ATTR, split.getUserPath());
        currentElement.appendChild(xmlParam);
    }
}
