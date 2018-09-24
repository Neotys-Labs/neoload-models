package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PostMultipartRequestWriter extends PostRequestWriter {

	private static final String XML_TAG_MULTIPARTS = "multiparts";
	private static final String XML_TAG_MULTIPART_STRING = "multipart-string";
	private static final String XML_TAG_MULTIPART_FILE = "multipart-file";

	public PostMultipartRequestWriter(PostMultipartRequest request) {
		super(request);
	}

	public static PostMultipartRequestWriter of(PostMultipartRequest request) {
		return new PostMultipartRequestWriter(request);
	}

	@Override
	protected int getPostType() {
		return PART_CONTENT;
	}

	@Override
	protected void fillXML(final Document document, final Element xmlRequest, final Request theRequest) {
		super.fillXML(document, xmlRequest, theRequest);

		final PostMultipartRequest postMultipartRequest = (PostMultipartRequest)theRequest;
		final Element multiParts = document.createElement(XML_TAG_MULTIPARTS);

		postMultipartRequest.getParts().stream().forEach(part -> {
			if(part.getSourceFilename().isPresent()) {
				final Element filePartElement = document.createElement(XML_TAG_MULTIPART_FILE);
				writeFilePartElement(filePartElement, part);
				multiParts.appendChild(filePartElement);
			} else {
				final Element stringPartElement = document.createElement(XML_TAG_MULTIPART_STRING);
				writeStringPartElement(stringPartElement, part);
				multiParts.appendChild(stringPartElement);
			}
		});
		xmlRequest.appendChild(multiParts);
	}

	private static void writeStringPartElement(final Element stringPartElement, final Part part) {
		writeCommonPartElement(stringPartElement, part);
		stringPartElement.setAttribute("value", part.getValue().get());
		stringPartElement.setAttribute("valueMode", "USE_VALUE");
	}

	private static void writeFilePartElement(final Element filePartElement, final Part part) {
		writeCommonPartElement(filePartElement, part);
		filePartElement.setAttribute("attachedFilename", part.getSourceFilename().orElse(""));
		filePartElement.setAttribute("filename", part.getFilename().orElse(part.getSourceFilename().orElse("")));
	}

	private static void writeCommonPartElement(final Element partElement, final Part part) {
		partElement.setAttribute("name",part.getName());
		part.getCharSet().ifPresent(s -> partElement.setAttribute("charSet", s));
		part.getContentType().ifPresent(s -> partElement.setAttribute("contentType", s));
		part.getTransferEncoding().ifPresent(s -> partElement.setAttribute("transferEncoding", s));
	}


}
