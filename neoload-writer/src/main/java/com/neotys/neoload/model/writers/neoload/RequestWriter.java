package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.CharSource;
import com.neotys.neoload.model.repository.RecordedFiles;
import com.neotys.neoload.model.repository.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;
import static com.neotys.neoload.model.writers.neoload.NeoLoadWriter.RECORDED_REQUESTS_FOLDER;
import static com.neotys.neoload.model.writers.neoload.NeoLoadWriter.RECORDED_RESPONSE_FOLDER;

public abstract class RequestWriter extends ElementWriter {

	private static Logger LOG = LoggerFactory.getLogger(RequestWriter.class);

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

	private static final String XML_TAG_RECORDED_REQUEST = "requestContentFileDescription";
	private static final String XML_TAG_RECORDED_RESPONSE = "responsePageFileDescription";
	private static final String XML_TAG_REQUEST_HEADER = "header";
	private static final String XML_TAG_RESPONSE_HEADERS = "responseHeaders";


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
		writeRecordedFiles(theRequest, document, xmlRequest, outputFolder);
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

    private void writeValidationSection(final Request request, final Document document, Element xmlRequest) {
		if (request.getValidators().isEmpty())
			return;

		Element xmlAssertBloc = document.createElement(XML_ATTR_ASSERT_BLOC);
		request.getValidators().forEach(validElem -> ValidatorWriter.getWriterFor(validElem).writeXML(document, xmlAssertBloc));

		xmlRequest.appendChild(xmlAssertBloc);
	}

	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		request.getParameters().forEach(paramElem -> ParameterWriter.of(paramElem).writeXML(document, xmlRequest, Optional.empty()));
	}

	private void writeRecordedFiles(final Request request, final Document document, final Element xmlRequest, final String outputFolder) {
		//Request header
		final String requestHeaderFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile).orElse(null);
		if (!isNullOrEmpty(requestHeaderFile)) {
			writeRecordedRequestHeaders(requestHeaderFile, document, xmlRequest);
		}

		//Request body
		final String requestBodyFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestBodyFile).orElse(null);
		if (!isNullOrEmpty(requestBodyFile)) {
			final Element element = document.createElement(XML_TAG_RECORDED_REQUEST);
			addResourceFileWithUuid(outputFolder, element, RECORDED_REQUESTS_FOLDER, "req_", requestBodyFile);
			xmlRequest.appendChild(element);
		}

		//Response header
		final String responseHeaderFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedResponseHeaderFile).orElse(null);
		if (!isNullOrEmpty(responseHeaderFile)) {
			writeRecordedResponseHeaders(responseHeaderFile, document, xmlRequest);
		}

		//Response body
		final String responseBodyFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedResponseBodyFile).orElse(null);
		if (!isNullOrEmpty(responseBodyFile)) {
			final Element element = document.createElement(XML_TAG_RECORDED_RESPONSE);
			addResourceFileWithUuid(outputFolder, element, RECORDED_RESPONSE_FOLDER, "res_", responseBodyFile);
			xmlRequest.appendChild(element);
		}
	}

	private void addResourceFileWithUuid(final String outputFolder, final Element element,
										 final String resourceFolderName, final String baseName,
										 final String resourcePathAsString) {
		final Path resourcePathInSrcProject = Paths.get(resourcePathAsString);
		final String srcFileName = resourcePathInSrcProject.getFileName().toString();
		final String srcFileNameWithoutExt = getNameWithoutExtension(resourcePathInSrcProject.getFileName().toString());
		String fileName = baseName + srcFileName;
		final Path resourcePathInNlProject = Paths.get(outputFolder, resourceFolderName, fileName);
		final String newName = baseName + srcFileNameWithoutExt + "_" + UUID.randomUUID() + "." + getFileExtension(fileName);
		try {
			Files.move(resourcePathInNlProject, resourcePathInNlProject.resolveSibling(newName));
			fileName = newName;
		} catch (final IOException e) {
			LOG.error("Can rename resource " + fileName + " to " + newName, e);
		}
		element.setTextContent(resourceFolderName + "/" + fileName);
	}

	private void writeRecordedResponseHeaders(final String recordedResponseHeaderFile, final Document document, final Element xmlRequest) {
		try {
			final String responseHeaders = new String(Files.readAllBytes(Paths.get(recordedResponseHeaderFile)), "UTF-8").replaceAll("\r\n", "\n");
			writeRecordedStatusCode(xmlRequest, responseHeaders);
			final Element element = document.createElement(XML_TAG_RESPONSE_HEADERS);
			element.setTextContent(responseHeaders);
			xmlRequest.appendChild(element);
		} catch (IOException e) {
			LOG.error("Can not write recorded response headers", e);
		}
	}

	private void writeRecordedStatusCode(final Element xmlRequest, final String responseHeaders) {
		final Matcher matcher = STATUS_CODE_PATTERN.matcher(responseHeaders);
		if(matcher.find()){
			final String statusCode = matcher.group(1);
			xmlRequest.setAttribute(XML_ATTR_RECORDED_RESPONSE_CODE, statusCode);
		}
	}

	private void writeRecordedRequestHeaders(final String recordedRequestHeaderFile, final Document document, final Element xmlRequest) {
		try {
			final Properties properties = new Properties();
			// we remove the status line from header file.
			final String contentWithoutFirstLine = Files.lines(Paths.get(recordedRequestHeaderFile)).skip(1).collect(Collectors.joining(System.lineSeparator()));
			properties.load(CharSource.wrap(contentWithoutFirstLine).openStream());
			properties.forEach((key, value) -> {
				if (key instanceof String && value instanceof String) {
					final Element element = document.createElement(XML_TAG_REQUEST_HEADER);
					element.setAttribute("name", (String) key);
					element.setAttribute("value", (String) value);
					xmlRequest.appendChild(element);
				}
			});
		} catch (IOException e) {
			LOG.error("Can not write recorded request headers", e);
		}
	}
}
