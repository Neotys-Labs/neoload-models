package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.net.MediaType;
import com.neotys.neoload.model.v3.project.userpath.Part;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.util.Parameter;
import com.neotys.neoload.model.v3.util.RequestUtils;
import com.neotys.neoload.model.v3.util.URL;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.SlaElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.userpath.assertion.AssertionsWriter;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Base64;
import java.util.List;
import java.util.Optional;


public class RequestWriter extends ElementWriter {


	public static final String XML_TAG_NAME = "http-action";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_ACTION_TYPE = "actionType";
	public static final String XML_ATTR_SERV_UID = "serverUid";
	public static final String XML_ATTR_PATH = "path";
	public static final String XML_ATTR_CONTENT_TYPE = "contentType";
	public static final String XML_ATTR_FOLLOW_REDIRECTS = "followRedirects";

	public static final String XML_ATTR_POST_TYPE = "postType";
	public static final String XML_URL_PARAMETER_TAG_NAME = "urlPostParameter";
	public static final String XML_STRING_DATA_TAG_NAME = "textPostContent";
	public static final String XML_BINARY_DATA_TAG_NAME = "binaryPostContentBase64";
	public static final String XML_PARTS_TAG_NAME = "multiparts";

	public static final int FORM_CONTENT = 1;
	public static final int RAW_CONTENT = 2;
	public static final int MULTIPART_CONTENT = 3;
	public static final int TEXT_CONTENT = 4;


	public RequestWriter(Request request) {
		super(request);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element xmlRequest = document.createElement(XML_TAG_NAME);
		final Request theRequest = (Request) this.element;
		super.writeXML(document, xmlRequest, outputFolder);
		fillXML(document, xmlRequest, theRequest);
		SlaElementWriter.of(theRequest).writeXML(xmlRequest);
		// write assertions
        final List<ContentAssertion> assertions = theRequest.getContentAssertions();
        if ((assertions != null && (!assertions.isEmpty()))) {
        	AssertionsWriter.of(assertions).writeXML(document, xmlRequest);	
        } 
		currentElement.appendChild(xmlRequest);
	}

	protected void fillXML(final Document document, final Element xmlRequest, final Request theRequest) {
		xmlRequest.setAttribute(XML_ATTR_METHOD, theRequest.getMethod());
		getContentType(theRequest).ifPresent(c -> xmlRequest.setAttribute(XML_ATTR_CONTENT_TYPE, c));
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_SERV_UID, server));
		xmlRequest.setAttribute(XML_ATTR_ACTION_TYPE, String.valueOf(getActionType()));
		xmlRequest.setAttribute(XML_ATTR_FOLLOW_REDIRECTS, String.valueOf(theRequest.getFollowRedirects()));
		URL url = RequestUtils.parseUrl(Optional.ofNullable(theRequest.getUrl()).orElse("/"));
		xmlRequest.setAttribute(XML_ATTR_PATH, url.getPath());
		theRequest.getExtractors().forEach(extractElem -> VariableExtractorWriter.of(extractElem).writeXML(document, xmlRequest));
		final boolean bodySupportedByMethod = isBodySupportedByMethod(theRequest);
		if(bodySupportedByMethod) {
			int postType = getPostType(theRequest);
			xmlRequest.setAttribute(XML_ATTR_POST_TYPE, String.valueOf(postType));
			theRequest.getBody().ifPresent(s -> {
				if(postType==FORM_CONTENT) writeParameters(RequestUtils.getParameters(s), Optional.empty(), document, xmlRequest);
				if(postType==TEXT_CONTENT) writePostTextBody(s, document, xmlRequest);
				if(postType==RAW_CONTENT) writePostRawBody(s.getBytes(), document, xmlRequest);
			});
			theRequest.getBodyBinary().ifPresent(s -> writePostRawBody(s, document, xmlRequest));
			theRequest.getParts().ifPresent(s -> writeParts(s, document, xmlRequest));
		}
		final Optional<String> parameterTag = bodySupportedByMethod ? Optional.of(XML_URL_PARAMETER_TAG_NAME) : Optional.empty();
		url.getQuery().ifPresent(s -> writeParameters(RequestUtils.getParameters(s), parameterTag, document, xmlRequest));
		theRequest.getHeaders().forEach(header -> HeaderWriter.writeXML(document, xmlRequest, header));
	}

	private boolean isBodySupportedByMethod(final Request theRequest) {
		return "post".equalsIgnoreCase(theRequest.getMethod())
				|| "put".equalsIgnoreCase(theRequest.getMethod());
	}

	protected int getPostType(final Request request) {
		if(request.getBodyBinary().isPresent()) return RAW_CONTENT;
		if(request.getParts().isPresent()) return MULTIPART_CONTENT;

		return getContentType(request).map(s -> {
			MediaType mediaType = MediaType.parse(s);
			if(mediaType.is(MediaType.ANY_TEXT_TYPE)) return TEXT_CONTENT;
			if("application".equalsIgnoreCase(mediaType.type())
					&& mediaType.subtype().toLowerCase().contains("form-urlencoded")) return FORM_CONTENT;
			if("application".equalsIgnoreCase(mediaType.type())) return RAW_CONTENT;
			if("multipart".equalsIgnoreCase(mediaType.type())) return MULTIPART_CONTENT;
			return TEXT_CONTENT;
		}).orElse(TEXT_CONTENT);
	}


	public void writeParameters(final List<Parameter> parameters, Optional<String> xmlTag, final Document document, Element xmlRequest) {
			parameters.forEach(param -> ParameterWriter.of(param).writeXML(document, xmlRequest, xmlTag));
	}

	public void writePostTextBody(final String body, final Document document, Element xmlRequest) {
		Element xmlDataNode = document.createElement(XML_STRING_DATA_TAG_NAME);
		CDATASection xmlData = document.createCDATASection(body);
		xmlDataNode.appendChild(xmlData);
		xmlRequest.appendChild(xmlDataNode);

		// write also in the binary content in case of conversion
		writePostRawBody(body.getBytes(), document, xmlRequest);
	}

	public void writePostRawBody(final byte[] body, final Document document, Element xmlRequest) {
		// write also in the binary content in case of conversion
		Element xmlDataBinaryNode = document.createElement(XML_BINARY_DATA_TAG_NAME);
		CDATASection xmlDataBinary = document.createCDATASection(Base64.getEncoder().encodeToString(body));
		xmlDataBinaryNode.appendChild(xmlDataBinary);
		xmlRequest.appendChild(xmlDataBinaryNode);
	}

	public void writeParts(final List<Part> parts, final Document document, Element xmlRequest) {
		Element xmlDataPartsNode = document.createElement(XML_PARTS_TAG_NAME);
		parts.forEach(part -> PartWriter.of(part).writeXML(document, xmlDataPartsNode, null));
		xmlRequest.appendChild(xmlDataPartsNode);

	}

	private Optional<String> getContentType(Request request) {
		return request.getHeaders().stream()
				.filter(header -> "content-type".equalsIgnoreCase(header.getName()) && header.getValue().isPresent())
				.map(header -> header.getValue().get())
				.findFirst();
	}

	protected int getActionType() {
		// GET / POST = 1
		// FOLLOW_LINK = 3
		// SUBMIT FORM = 4
		return 1;
	}

}
