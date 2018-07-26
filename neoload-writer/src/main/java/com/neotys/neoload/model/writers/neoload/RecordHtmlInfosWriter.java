package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.GetFollowLinkRequest;

public class RecordHtmlInfosWriter {

	public static final String XML_TAG_NAME = "record-html-infos";
	public static final String XML_ATTR_EXTRACTOR_CONTENT = "extractorContent";
	public static final String XML_ATTR_EXTRACTOR_OCCURENCE = "extractorOccurence";
	public static final String XML_ATTR_HTML_TYPE = "htmlType";

	public static void writeXML(final Document document, final Element parentElement, final GetFollowLinkRequest getFollowLinkRequest) {
		final Element element = document.createElement(XML_TAG_NAME);
		element.setAttribute(XML_ATTR_EXTRACTOR_CONTENT, getFollowLinkRequest.getText());
		element.setAttribute(XML_ATTR_EXTRACTOR_OCCURENCE, "1");
		element.setAttribute(XML_ATTR_HTML_TYPE, "1");
		parentElement.appendChild(element);
	}
}
