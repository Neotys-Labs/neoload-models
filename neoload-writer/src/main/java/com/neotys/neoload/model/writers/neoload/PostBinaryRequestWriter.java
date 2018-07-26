package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.PostBinaryRequest;
import com.neotys.neoload.model.repository.PostRequest;
import com.neotys.neoload.model.repository.Request;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Base64;

public class PostBinaryRequestWriter extends PostRequestWriter {
	public static final String XML_BINARY_DATA_TAG_NAME = "binaryPostContentBase64";

	public PostBinaryRequestWriter(PostRequest request) {
		super(request);
	}
	
	@Override
	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		super.writeParameters(request, document, xmlRequest);
		Element xmlDataNode = document.createElement(XML_BINARY_DATA_TAG_NAME);
		CDATASection xmlData = document.createCDATASection(Base64.getEncoder().encodeToString(((PostBinaryRequest)request).getBinaryData()));
		xmlDataNode.appendChild(xmlData);
		xmlRequest.appendChild(xmlDataNode);


	}
}
