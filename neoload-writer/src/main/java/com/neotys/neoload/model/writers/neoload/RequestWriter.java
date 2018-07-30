package com.neotys.neoload.model.writers.neoload;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;
import static com.neotys.neoload.model.writers.neoload.NeoLoadWriter.RECORDED_REQUESTS_FOLDER;
import static com.neotys.neoload.model.writers.neoload.NeoLoadWriter.RECORDED_RESPONSE_FOLDER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.RecordedFiles;
import com.neotys.neoload.model.repository.Request;

public abstract class RequestWriter extends ElementWriter {

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

	private static final String XML_TAG_RECORDED_REQUEST = "requestContentFileDescription";
	private static final String XML_TAG_RECORDED_RESPONSE = "responsePageFileDescription";
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

    private static void writeValidationSection(final Request request, final Document document, Element xmlRequest) {
		if (request.getValidators().isEmpty())
			return;

		Element xmlAssertBloc = document.createElement(XML_ATTR_ASSERT_BLOC);
		request.getValidators().forEach(validElem -> ValidatorWriter.getWriterFor(validElem).writeXML(document, xmlAssertBloc));

		xmlRequest.appendChild(xmlAssertBloc);
	}

	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		request.getParameters().forEach(paramElem -> ParameterWriter.of(paramElem).writeXML(document, xmlRequest, Optional.empty()));
	}

	private static void writeRecordedFiles(final Request request, final Document document, final Element xmlRequest, final String outputFolder) {
		//Request content
		final String requestBodyFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestBodyFile).orElse(null);
		final String requestHeaderFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile).orElse(null);
		if (!isNullOrEmpty(requestBodyFile) || !isNullOrEmpty(requestHeaderFile) ) {
			final Element element = document.createElement(XML_TAG_RECORDED_REQUEST);
			final boolean copied = copyRequestContent(outputFolder, element, requestHeaderFile, requestBodyFile);
			if (copied) {
				xmlRequest.appendChild(element);
			}
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
			final boolean copied = copyResponseBody(outputFolder, element, responseBodyFile);
			if (copied) {
				xmlRequest.appendChild(element);
			}
		}
	}

	private static boolean copyRequestContent(final String outputFolder, final Element element,
									   final String requestHeaderFile, final String requestBodyFile) {
		final boolean hasHeaders = !isNullOrEmpty(requestHeaderFile);
		final boolean hasBody = !isNullOrEmpty(requestBodyFile);
		Preconditions.checkState(hasHeaders || hasBody);

		final Path requestHeaderPathFromLRProject = hasHeaders ? Paths.get(requestHeaderFile) : null;
		final Path requestBodyPathFromLRProject = hasBody ? Paths.get(requestBodyFile) : null;

		final Path file = hasBody ? requestBodyPathFromLRProject : requestHeaderPathFromLRProject;
		if(file == null){
			return false;
		}
		final String srcFileNameWithoutExt = getNameWithoutExtension(file.getFileName().toString());
		final String newName = "req_" + srcFileNameWithoutExt + "_" + UUID.randomUUID() + "." + getFileExtension(file.getFileName().toString());
		final Path filePathInNLProject = Paths.get(outputFolder, RECORDED_REQUESTS_FOLDER, newName);

		try (final FileOutputStream fileOutputStream = new FileOutputStream(filePathInNLProject.toFile())) {
			if (hasHeaders) {
				fileOutputStream.write(Files.readAllBytes(requestHeaderPathFromLRProject));
			}
			if (hasBody) {
				fileOutputStream.write(Files.readAllBytes(requestBodyPathFromLRProject));
			}
			fileOutputStream.flush();
			element.setTextContent(RECORDED_REQUESTS_FOLDER + "/" + newName);
			return true;
		} catch (IOException e) {
			LOG.error("Cannot copy resource " + file + " to " + filePathInNLProject, e);
		}
		return false;

	}

	private static boolean copyResponseBody(final String outputFolder, final Element element,
									 final String resourcePathAsString) {
		final Path resourcePathInSrcProject = Paths.get(resourcePathAsString);
		final String srcFileName = resourcePathInSrcProject.getFileName().toString();
		final String srcFileNameWithoutExt = getNameWithoutExtension(resourcePathInSrcProject.getFileName().toString());

		final String newName = "res_" + srcFileNameWithoutExt + "_" + UUID.randomUUID() + "." + getFileExtension(srcFileName);
		final Path resourcePathInNlProject = Paths.get(outputFolder, RECORDED_RESPONSE_FOLDER, newName);
		try {
			Files.copy(resourcePathInSrcProject, resourcePathInNlProject, REPLACE_EXISTING);
			element.setTextContent(RECORDED_RESPONSE_FOLDER + "/" + newName);
			return true;
		} catch (final IOException e) {
			LOG.error("Cannot copy resource " + resourcePathInSrcProject + " to " + resourcePathInNlProject, e);
			return false;
		}
	}

	private static void writeRecordedResponseHeaders(final String recordedResponseHeaderFile, final Document document, final Element xmlRequest) {
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

	private static void writeRecordedStatusCode(final Element xmlRequest, final String responseHeaders) {
		final Matcher matcher = STATUS_CODE_PATTERN.matcher(responseHeaders);
		if(matcher.find()){
			final String statusCode = matcher.group(1);
			xmlRequest.setAttribute(XML_ATTR_RECORDED_RESPONSE_CODE, statusCode);
		}
	}

}
