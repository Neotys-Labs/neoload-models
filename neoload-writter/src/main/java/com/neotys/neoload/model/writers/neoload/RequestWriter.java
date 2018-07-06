package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Request;

import java.util.Optional;

public class RequestWriter extends ElementWriter {
	public static final String XML_TAG_NAME = "http-action";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_ACTION_TYPE = "actionType";
	public static final String XML_ATTR_SERV_UID = "serverUid";
	public static final String XML_ATTR_PATH = "path";
	public static final String XML_ATTR_ASSERT_BLOC = "assertions";

	private static final String DEFAULT_ACTION_TYPE = "1";

	public RequestWriter(Request request) {
		super(request);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String parentPath, final String outputFolder) {
		Element xmlRequest = document.createElement(XML_TAG_NAME);
		Request theRequest = (Request) this.element;
		super.writeXML(document, xmlRequest, parentPath, outputFolder);

		xmlRequest.setAttribute(XML_ATTR_METHOD, theRequest.getHttpMethod().toString());
		xmlRequest.setAttribute(XML_ATTR_SERV_UID, theRequest.getServer().getName());
		xmlRequest.setAttribute(XML_ATTR_ACTION_TYPE, DEFAULT_ACTION_TYPE);
		xmlRequest.setAttribute(XML_ATTR_PATH, theRequest.getPath());
		
		theRequest.getExtractors().forEach(extractElem -> ExtractorWriter.of(extractElem).writeXML(document, xmlRequest));
		writeValidationSection(theRequest, document, xmlRequest);
		writeParameters(theRequest, document, xmlRequest);

		currentElement.appendChild(xmlRequest);
	}

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
