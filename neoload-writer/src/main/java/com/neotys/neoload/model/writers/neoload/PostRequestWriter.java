package com.neotys.neoload.model.writers.neoload;

import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.PostRequest;
import com.neotys.neoload.model.repository.Request;

public abstract class PostRequestWriter extends RequestWriter {
	public static final String XML_URL_PARAMETER_TAG_NAME = "urlPostParameter";
	public static final String XML_BINARY_DATA_TAG_NAME = "binaryPostContentBase64";
	public static final String XML_STRING_DATA_TAG_NAME = "textPostContent";
	public static final String XML_ATTR_POST_TYPE = "postType";

    public static final int FORM_CONTENT = 1;
    public static final int RAW_CONTENT = 2;
    public static final int PART_CONTENT = 3;
    public static final int TEXT_CONTENT = 4;
    public static final int MIME_CONTENT = 5;

	public PostRequestWriter(PostRequest request) {
		super(request);
	}
	
	@Override
	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		request.getParameters().forEach(paramElem -> ParameterWriter.of(paramElem).writeXML(document, xmlRequest, Optional.of(XML_URL_PARAMETER_TAG_NAME)));
		((PostRequest)request).getPostParameters().forEach(paramElem -> ParameterWriter.of(paramElem).writeXML(document, xmlRequest, Optional.empty()));
	}
	
	@Override
	protected void fillXML(Document document, Element xmlRequest, Request theRequest) {
		super.fillXML(document, xmlRequest, theRequest);
		xmlRequest.setAttribute(XML_ATTR_POST_TYPE, String.valueOf(getPostType()));
	}

	protected abstract int getPostType();

	@Override
	protected int getActionType() {
		return 1;
	}
}
