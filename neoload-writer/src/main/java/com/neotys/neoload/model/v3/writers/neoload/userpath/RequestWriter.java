package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.net.MediaType;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.util.Parameter;
import com.neotys.neoload.model.v3.util.RequestUtils;
import com.neotys.neoload.model.v3.util.URL;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.SlaElementWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestWriter extends ElementWriter {

	private static final Logger LOG = LoggerFactory.getLogger(RequestWriter.class);

	public static final String XML_TAG_NAME = "http-action";
	public static final String XML_ATTR_METHOD = "method";
	public static final String XML_ATTR_ACTION_TYPE = "actionType";
	public static final String XML_ATTR_SERV_UID = "serverUid";
	public static final String XML_ATTR_EXTRACTOR_SERVER_UID = "extractorServerUid";
	public static final String XML_ATTR_PATH = "path";
	public static final String XML_ATTR_ASSERT_BLOC = "assertions";
	public static final String XML_ATTR_RECORDED_RESPONSE_CODE = "recordedResponseCode";
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
	public static final String XML_ATTR_EXTRACTORPATH = "extractorPath";
	public static final String XML_ATTR_CONF_FORM_EXTRACTOR_PARAMETERS = "confFormExtractorParameters";
	public static final String XML_ATTR_CONTENT_TYPE = "contentType";

	private static final String XML_TAG_RECORDED_REQUEST = "requestContentFileDescription";
	private static final String XML_TAG_RECORDED_RESPONSE = "responsePageFileDescription";
	private static final String XML_TAG_RESPONSE_HEADERS = "responseHeaders";
	public static final String XML_ATTR_POST_TYPE = "postType";
	public static final String XML_URL_PARAMETER_TAG_NAME = "urlPostParameter";
	public static final String XML_STRING_DATA_TAG_NAME = "textPostContent";
	public static final String XML_BINARY_DATA_TAG_NAME = "binaryPostContentBase64";

	public static final int FORM_CONTENT = 1;
	public static final int RAW_CONTENT = 2;
	public static final int PART_CONTENT = 3;
	public static final int TEXT_CONTENT = 4;
	public static final int MIME_CONTENT = 5;

	private static final Pattern STATUS_CODE_PATTERN = Pattern.compile("(\\d{3})");

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
		//writeRecordedFiles(theRequest, document, xmlRequest, outputFolder);
		currentElement.appendChild(xmlRequest);
	}

	protected void fillXML(final Document document, final Element xmlRequest, final Request theRequest) {
		xmlRequest.setAttribute(XML_ATTR_METHOD, theRequest.getMethod());
		getContentType(theRequest).ifPresent(c -> xmlRequest.setAttribute(XML_ATTR_CONTENT_TYPE, c));
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_SERV_UID, server));
		xmlRequest.setAttribute(XML_ATTR_ACTION_TYPE, String.valueOf(getActionType(theRequest)));
		URL url = RequestUtils.parseUrl(Optional.ofNullable(theRequest.getUrl()).orElse("/"));
		xmlRequest.setAttribute(XML_ATTR_PATH, url.getPath());
		theRequest.getExtractors().forEach(extractElem -> VariableExtractorWriter.of(extractElem).writeXML(document, xmlRequest));
		if(theRequest.getMethod().toLowerCase().equals("post")) {
			int postType = getPostType(theRequest);
			xmlRequest.setAttribute(XML_ATTR_POST_TYPE, String.valueOf(postType));
			theRequest.getBody().ifPresent(s -> {
				if(postType==FORM_CONTENT) writeParameters(RequestUtils.getParameters(s), Optional.empty(), document, xmlRequest);
				if(postType==TEXT_CONTENT) writePostTextBody(s, document, xmlRequest);
				if(postType==RAW_CONTENT) writePostRawBody(s, document, xmlRequest);
			});
		}
		final Optional<String> parameterTag = theRequest.getMethod().toLowerCase().equals("post") ? Optional.of(XML_URL_PARAMETER_TAG_NAME) : Optional.empty();
		url.getQuery().ifPresent(s -> writeParameters(RequestUtils.getParameters(s), parameterTag, document, xmlRequest));
		theRequest.getHeaders().forEach(header -> HeaderWriter.writeXML(document, xmlRequest, header));
	}

	protected int getPostType(Request request) {
		return getContentType(request).map(s -> {
			MediaType mediaType = MediaType.parse(s);
			if(mediaType.is(MediaType.ANY_TEXT_TYPE)) return TEXT_CONTENT;
			if(mediaType.type().toLowerCase().equals("application")
					&& mediaType.subtype().toLowerCase().contains("form-urlencoded")) return FORM_CONTENT;
			if(mediaType.type().toLowerCase().equals("application")) return RAW_CONTENT;
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
		writePostRawBody(body, document, xmlRequest);
	}

	public void writePostRawBody(final String body, final Document document, Element xmlRequest) {
		// write also in the binary content in case of conversion
		Element xmlDataBinaryNode = document.createElement(XML_BINARY_DATA_TAG_NAME);
		CDATASection xmlDataBinary = document.createCDATASection(Base64.getEncoder().encodeToString(body.getBytes()));
		xmlDataBinaryNode.appendChild(xmlDataBinary);
		xmlRequest.appendChild(xmlDataBinaryNode);
	}

	private Optional<String> getContentType(Request request) {
		return request.getHeaders().stream()
				.filter(header -> header.getName().toLowerCase().equals("content-type") && header.getValue().isPresent())
				.map(header -> header.getValue().get())
				.findFirst();
	}

	protected int getActionType(Request request) {
		// GET / POST = 1
		// FOLLOW_LINK = 3
		// SUBMIT FORM = 4
		return 1;
	}


	private static void writeRecordedStatusCode(final Element xmlRequest, final String responseHeaders) {
		final Matcher matcher = STATUS_CODE_PATTERN.matcher(responseHeaders);
		if(matcher.find()){
			final String statusCode = matcher.group(1);
			xmlRequest.setAttribute(XML_ATTR_RECORDED_RESPONSE_CODE, statusCode);
		}
	}

}
