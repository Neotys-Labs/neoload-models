package com.neotys.neoload.model.writers.neoload;

import java.util.Base64;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.PostTextRequest;
import com.neotys.neoload.model.repository.Request;

public class PostTextRequestWriter extends PostRequestWriter {

	public PostTextRequestWriter(PostTextRequest request) {
		super(request);
	}
	
	@Override
	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		super.writeParameters(request, document, xmlRequest);
		String data = ((PostTextRequest)request).getData();
		Element xmlDataNode = document.createElement(XML_STRING_DATA_TAG_NAME);
		CDATASection xmlData = document.createCDATASection(data);
		xmlDataNode.appendChild(xmlData);
		xmlRequest.appendChild(xmlDataNode);

		// write also in the binary content in case of conversion
		Element xmlDataBinaryNode = document.createElement(XML_BINARY_DATA_TAG_NAME);
		CDATASection xmlDataBinary = document.createCDATASection(Base64.getEncoder().encodeToString(data.getBytes()));
		xmlDataBinaryNode.appendChild(xmlDataBinary);
		xmlRequest.appendChild(xmlDataBinaryNode);
	}
}
