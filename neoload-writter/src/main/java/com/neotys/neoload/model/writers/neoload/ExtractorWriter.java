package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.writers.RegExpUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.VariableExtractor;

public class ExtractorWriter {

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

    private final VariableExtractor extractor;
    

    public ExtractorWriter(VariableExtractor extractor) {
        this.extractor = extractor;
    }

    public void writeXML(final Document document, final Element currentElement) {
 
        Element xmlExtractor = document.createElement(XML_TAG_NAME);        
        xmlExtractor.setAttribute(XML_ATTR_DISPLAY_MODE, extractor.getRegExp().isPresent() ? "1" : "0");
        xmlExtractor.setAttribute(XML_ATTR_SET_DEFAULT_VALUE, "false");
        xmlExtractor.setAttribute(XML_ATTR_DEFAULT_VALUE, "<NOT FOUND>");
        final String group;
        if(extractor.getGroup().isPresent()){
        	group = "$"+extractor.getGroup().get()+"$";
        } else {
        	group = "$1$";
        }
        xmlExtractor.setAttribute(XML_ATTR_TEMPLATE, group);
        xmlExtractor.setAttribute(XML_ATTR_TEMPLATE_ADV, group);        
        xmlExtractor.setAttribute(XML_ATTR_START, extractor.getStartExpression().orElse(""));
        xmlExtractor.setAttribute(XML_ATTR_END, extractor.getEndExpression().orElse(""));
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTFROMVARNAME, "");
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTFROMVARNAMEADV, "");
        xmlExtractor.setAttribute(XML_ATTR_EXTRACTFROMVARNAMESIMPLE, "");
        ExtractTypeWriter.writeXML(xmlExtractor, extractor.getExtractType());
        final String matchNumber = Integer.toString(extractor.getNbOccur().orElse(1));
        xmlExtractor.setAttribute(XML_ATTR_MATCH_NUMBER, matchNumber);
       	xmlExtractor.setAttribute(XML_ATTR_MATCH_NUMBER_ADV, matchNumber);
       	xmlExtractor.setAttribute(XML_ATTR_MATCH_NUMBER_SIMPLE, matchNumber);               
        xmlExtractor.setAttribute(XML_ATTR_NAME, extractor.getName());
        final String regEx;
        if(extractor.getRegExp().isPresent()){
        	regEx = extractor.getRegExp().get();
        } else {
        	regEx = RegExpUtils.escapeExcludingVariables(extractor.getStartExpression().orElse(""))+
        			"(.*?)"+
        			RegExpUtils.escapeExcludingVariables(extractor.getEndExpression().orElse("")); 
        }
        if(extractor.getXPath().isPresent()){
        	  xmlExtractor.setAttribute(XML_ATTR_XPATH, extractor.getXPath().get());
        }
        if(extractor.getJsonPath().isPresent()){
      	  xmlExtractor.setAttribute(XML_ATTR_JSONPATH, extractor.getJsonPath().get());
        }
        xmlExtractor.setAttribute(XML_ATTR_REGEX, regEx);
        xmlExtractor.setAttribute(XML_ATTR_REGEX_ADV, regEx);
        xmlExtractor.setAttribute(XML_ATTR_HAS_TO_BE_PRESENT, Boolean.toString(extractor.getExitOnError()));        
        Element extractorGroup = document.createElement(XML_TAG_GROUP_NAME);
        extractorGroup.setAttribute("extract", "true");
        extractorGroup.setAttribute("occurs", "1");
        extractorGroup.setAttribute("pattern", "");
        extractorGroup.setAttribute("type", "4");

        xmlExtractor.appendChild(extractorGroup);

        currentElement.appendChild(xmlExtractor);
    }

	public static ExtractorWriter of(VariableExtractor paramElem) {
		return new ExtractorWriter(paramElem);
	}

}
