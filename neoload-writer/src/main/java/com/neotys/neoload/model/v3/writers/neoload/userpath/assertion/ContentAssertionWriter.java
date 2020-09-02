package com.neotys.neoload.model.v3.writers.neoload.userpath.assertion;

import static com.neotys.neoload.model.v3.util.AssertionUtils.normalyzeContains;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static com.google.common.base.Preconditions.checkNotNull;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

final class ContentAssertionWriter {
	// Contains with Text
	private static final String XML_ASSERTION_CONTENT_TAG_NAME   = "assertion-content";
	private static final String XML_ASSERTION_PLUGIN_CONTENT_TAG_NAME = "assertion-plugin-content";
	private static final String XML_ASSERTION_JSON_CONTENT_TAG_NAME = "assertion-json-content";
	
	// Contains with Regular Expression
	private static final String XML_ASSERTION_RESPONSE_TAG_NAME = "assertion-response";
	private static final String XML_ASSERTION_PLUGIN_RESPONSE_TAG_NAME = "assertion-plugin-response";
	private static final String XML_ASSERTION_JSON_RESPONSE_TAG_NAME = "assertion-json-response";
	
	private static final String XML_TEST_STRING_TAG_NAME = "testString";
		
	// All tags except 'testString' tag
	private static final String XML_ATTR_NAME = "name"; 
	private static final String XML_ATTR_NOT_TYPE = "notType";
	// Only for 'assertion-content', 'assertion-plugin-content' and 'assertion-json-content' tags
	private static final String XML_ATTR_PATTERN = "pattern";
	// Only for 'assertion-response', 'assertion-plugin-response' and 'assertion-json-response' tag
	private static final String XML_ATTR_CONTAINS_TYPE = "containsType";
	private static final String XML_ATTR_MATCH_TYPE = "matchType";	
	// Only for 'assertion-plugin-content' and 'assertion-plugin-response' tags
	private static final String XML_ATTR_BY_XPATH = "byXPath";
	private static final String XML_ATTR_XPATH = "xPath";
	// Only for 'assertion-json-content' and 'assertion-json-response' tags
	private static final String XML_ATTR_BY_JSONPATH = "byJsonPath";
	private static final String XML_ATTR_JSONPATH = "jsonPath";
	
	private final ContentAssertion assertion;

    private ContentAssertionWriter(final ContentAssertion assertion) {
        this.assertion = checkNotNull(assertion);
    }

    protected static ContentAssertionWriter of(final ContentAssertion assertion) {
        return new ContentAssertionWriter(assertion);
    }

	protected void writeXML(final Document document, final Element currentElement) {
		final Element xmlAssertion;
		final boolean regexp = assertion.getRegexp();
		if (regexp) {
			xmlAssertion = createAndFillAssertionElementfromResponse(document, assertion);
		}
		else {
			xmlAssertion = createAndFillAssertionElementfromContent(document, assertion);			
		}		
		currentElement.appendChild(xmlAssertion);
	}	
	
	private static Element createAndFillAssertionElementfromResponse(final Document document, final ContentAssertion assertion) {
		final Element xmlAssertion;
		if (assertion.getXPath().isPresent()) {
			xmlAssertion = document.createElement(XML_ASSERTION_PLUGIN_RESPONSE_TAG_NAME);
			xmlAssertion.setAttribute(XML_ATTR_BY_XPATH, "true");
			xmlAssertion.setAttribute(XML_ATTR_XPATH, assertion.getXPath().get());
		}
		else if (assertion.getJsonPath().isPresent()) {
			xmlAssertion = document.createElement(XML_ASSERTION_JSON_RESPONSE_TAG_NAME);
			xmlAssertion.setAttribute(XML_ATTR_BY_JSONPATH, "true");
			xmlAssertion.setAttribute(XML_ATTR_JSONPATH, assertion.getJsonPath().get());
		}
		else {
			xmlAssertion = document.createElement(XML_ASSERTION_RESPONSE_TAG_NAME);
		}			
		xmlAssertion.setAttribute(XML_ATTR_NAME, assertion.getName().get());
		xmlAssertion.setAttribute(XML_ATTR_NOT_TYPE, String.valueOf(assertion.getNot()));			
		xmlAssertion.setAttribute(XML_ATTR_CONTAINS_TYPE, "true");
		xmlAssertion.setAttribute(XML_ATTR_MATCH_TYPE, "false");
		
		final Element xmlRegexp = document.createElement(XML_TEST_STRING_TAG_NAME);
		xmlRegexp.setTextContent(normalyzeContains(assertion.getContains()));
		xmlAssertion.appendChild(xmlRegexp);
        
		return xmlAssertion;
	}	

	private static Element createAndFillAssertionElementfromContent(final Document document, final ContentAssertion assertion) {
		final Element xmlAssertion;
		if (assertion.getXPath().isPresent()) {
			xmlAssertion = document.createElement(XML_ASSERTION_PLUGIN_CONTENT_TAG_NAME);
			xmlAssertion.setAttribute(XML_ATTR_BY_XPATH, "true");
			xmlAssertion.setAttribute(XML_ATTR_XPATH, assertion.getXPath().get());
		}
		else if (assertion.getJsonPath().isPresent()) {
			xmlAssertion = document.createElement(XML_ASSERTION_JSON_CONTENT_TAG_NAME);
			xmlAssertion.setAttribute(XML_ATTR_BY_JSONPATH, "true");
			xmlAssertion.setAttribute(XML_ATTR_JSONPATH, assertion.getJsonPath().get());
		}
		else {
			xmlAssertion = document.createElement(XML_ASSERTION_CONTENT_TAG_NAME);
		}			
		xmlAssertion.setAttribute(XML_ATTR_NAME, assertion.getName().get());
		xmlAssertion.setAttribute(XML_ATTR_NOT_TYPE, String.valueOf(assertion.getNot()));
		xmlAssertion.setAttribute(XML_ATTR_PATTERN, normalyzeContains(assertion.getContains()));
		return xmlAssertion;
	}
}
