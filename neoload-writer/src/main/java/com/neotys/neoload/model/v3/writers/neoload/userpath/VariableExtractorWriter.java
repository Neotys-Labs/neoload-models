package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Optional;

public class VariableExtractorWriter {

    private static final String XML_TAG_NAME = "variable-extractor";
    private static final String XML_TAG_GROUP_NAME = "variable-extractor-group";
    private static final String XML_ATTR_NAME = "name";
    private static final String XML_ATTR_DISPLAY_MODE = "displayMode";
    private static final String XML_ATTR_SET_DEFAULT_VALUE = "setDefaultValue";
    private static final String XML_ATTR_DEFAULT_VALUE = "defaultValue";
    private static final String XML_ATTR_TEMPLATE = "template";
    private static final String XML_ATTR_TEMPLATE_ADV = "templateAdv";
    private static final String XML_ATTR_START = "startsWithSimple";
    private static final String XML_ATTR_END = "endsWithSimple";
    private static final String XML_ATTR_REGEX = "regExp";
    private static final String XML_ATTR_XPATH = "xpath";
    private static final String XML_ATTR_EXTRACTFROMVARNAME = "extractFromVarName";
    private static final String XML_ATTR_EXTRACTFROMVARNAMEADV = "extractFromVarNameAdv";
    private static final String XML_ATTR_EXTRACTFROMVARNAMESIMPLE = "extractFromVarNameSimple";
    private static final String XML_ATTR_JSONPATH = "jsonpath";
    private static final String XML_ATTR_REGEX_ADV = "regExpAdv";
    private static final String XML_ATTR_HAS_TO_BE_PRESENT = "assertionOnNoMatch";
    private static final String XML_ATTR_MATCH_NUMBER = "matchNumber";
    private static final String XML_ATTR_MATCH_NUMBER_ADV = "matchNumberAdv";
    private static final String XML_ATTR_MATCH_NUMBER_SIMPLE = "matchNumberSimple";
    private static final String XML_ATTR_EXTRACT_TYPE = "extractType";
    private static final String XML_ATTR_EXTRACTTYPEADV = "extractTypeAdv";
    private static final String XML_ATTR_EXTRACT_TYPE_SIMPLE = "extractTypeSimple";

    private final VariableExtractor extractor;
    

    public VariableExtractorWriter(VariableExtractor extractor) {
        this.extractor = extractor;
    }

    public void writeXML(final Document document, final Element currentElement) {
 
        Element xmlExtractor = document.createElement(XML_TAG_NAME);        
        xmlExtractor.setAttribute(XML_ATTR_DISPLAY_MODE, Optional.ofNullable(extractor.getRegexp()).isPresent() ? "1" : "0");
        xmlExtractor.setAttribute(XML_ATTR_SET_DEFAULT_VALUE, "false");
        xmlExtractor.setAttribute(XML_ATTR_DEFAULT_VALUE, "<NOT FOUND>");
        xmlExtractor.setAttribute(XML_ATTR_TEMPLATE, extractor.getTemplate());
        xmlExtractor.setAttribute(XML_ATTR_TEMPLATE_ADV, extractor.getTemplate());
        xmlExtractor.setAttribute(XML_ATTR_START, "");
        xmlExtractor.setAttribute(XML_ATTR_END, "");
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTFROMVARNAME, "");
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTFROMVARNAMEADV, "");
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTFROMVARNAMESIMPLE, "");
        final String extractTypeString = getExtractTypeInt(extractor)+"";
        xmlExtractor.setAttribute(XML_ATTR_EXTRACT_TYPE, extractTypeString);
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTTYPEADV, "0");
        xmlExtractor.setAttribute(XML_ATTR_EXTRACT_TYPE_SIMPLE, extractTypeString);
        final String matchNumber = Integer.toString(extractor.getMatchNumber());
        xmlExtractor.setAttribute(XML_ATTR_MATCH_NUMBER, matchNumber);
       	xmlExtractor.setAttribute(XML_ATTR_MATCH_NUMBER_ADV, matchNumber);
       	xmlExtractor.setAttribute(XML_ATTR_MATCH_NUMBER_SIMPLE, matchNumber);               
        xmlExtractor.setAttribute(XML_ATTR_NAME, extractor.getName());
        xmlExtractor.setAttribute(XML_ATTR_REGEX, extractor.getRegexp());
        xmlExtractor.setAttribute(XML_ATTR_REGEX_ADV, extractor.getRegexp());
        if(extractor.getXpath().isPresent()){
        	  xmlExtractor.setAttribute(XML_ATTR_XPATH, extractor.getXpath().get());
        }
        if(extractor.getJsonPath().isPresent()){
      	  xmlExtractor.setAttribute(XML_ATTR_JSONPATH, extractor.getJsonPath().get());
        }
        xmlExtractor.setAttribute(XML_ATTR_HAS_TO_BE_PRESENT, "true");
        Element extractorGroup = document.createElement(XML_TAG_GROUP_NAME);
        extractorGroup.setAttribute("extract", "true");
        extractorGroup.setAttribute("occurs", "1");
        extractorGroup.setAttribute("pattern", "");
        extractorGroup.setAttribute("type", "4");

        xmlExtractor.appendChild(extractorGroup);

        currentElement.appendChild(xmlExtractor);
    }

	public static VariableExtractorWriter of(VariableExtractor paramElem) {
		return new VariableExtractorWriter(paramElem);
	}

    private static int getExtractTypeInt(final VariableExtractor extractor) {

        if(extractor.getXpath().isPresent()) return 3;
        if(extractor.getJsonPath().isPresent()) return 4;

        switch(extractor.getFrom()){
            case BOTH:
                return 5;
            case HEADER:
                return 1;
            case BODY:
                return 0;
            default:
                return 0;
        }
    }

}
