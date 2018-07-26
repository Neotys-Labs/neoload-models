package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Request;

import java.util.Optional;

public abstract class RequestWriter extends ElementWriter {
	public static final String XML_TAG_NAME = "http-action";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_ACTION_TYPE = "actionType";
	public static final String XML_ATTR_SERV_UID = "serverUid";
	public static final String XML_ATTR_PATH = "path";
	public static final String XML_ATTR_ASSERT_BLOC = "assertions";
	public static final String XML_ATTR_LINKEXTRACTORTYPE = "linkExtractorType";
	public static final String ACTION_LINKEXTRACTOR_TYPE_MATCH_DEFINITION = "6";
	public static final String ACTION_LINKEXTRACTOR_TYPE_MATCHCONTENT = "3";
	public static final String XML_ATTR_REFERER_UID = "refererUid";
	public static final String XML_TAG_RECORD_HTML_INFOS = "record-html-infos";
	public static final String XML_TAG_EXTRACTOR_HTML_INFOS = "extractor-html-infos";
	public static final String XML_ATTR_EXTRACTOR_REGEXP = "extractorRegExp";
	public static final String XML_ATTR_EXTRACTOR_OCCURENCE = "extractorOccurence";
	public static final String XML_ATTR_EXTRACTOR_CONTENT = "extractorContent";
	public static final String XML_ATTR_HTML_TYPE = "htmlType";
	
	public RequestWriter(Request request) {
		super(request);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element xmlRequest = document.createElement(XML_TAG_NAME);
		final Request theRequest = (Request) this.element;
		super.writeXML(document, xmlRequest, outputFolder);
		fillXML(document, xmlRequest, theRequest);
		currentElement.appendChild(xmlRequest);
	}

	protected void fillXML(final Document document, final Element xmlRequest, final Request theRequest) {
		xmlRequest.setAttribute(XML_ATTR_METHOD, theRequest.getHttpMethod().toString());
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_SERV_UID, server.getName()));		
		xmlRequest.setAttribute(XML_ATTR_ACTION_TYPE, String.valueOf(getActionType()));
		theRequest.getPath().ifPresent(path -> xmlRequest.setAttribute(XML_ATTR_PATH, path));
		theRequest.getExtractors().forEach(extractElem -> ExtractorWriter.of(extractElem).writeXML(document, xmlRequest));
		writeValidationSection(theRequest, document, xmlRequest);
		writeParameters(theRequest, document, xmlRequest);
		theRequest.getHeaders().forEach(header -> HeaderWriter.writeXML(document, xmlRequest, header));
	}

	protected abstract int getActionType();

	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		request.getParameters().forEach(paramElem -> ParameterWriter.of(paramElem).writeXML(document, xmlRequest, Optional.empty()));
	}
	
	public void writeValidationSection(final Request request, final Document document, Element xmlRequest) {
		if (request.getValidators().isEmpty())
			return;
		
		Element xmlAssertBloc = document.createElement(XML_ATTR_ASSERT_BLOC);
		request.getValidators().forEach(validElem -> ValidatorWriter.getWriterFor(validElem).writeXML(document, xmlAssertBloc));
		
		xmlRequest.appendChild(xmlAssertBloc);
	}
}
