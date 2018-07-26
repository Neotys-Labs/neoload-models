package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.PostFormRequest;
import com.neotys.neoload.model.repository.Request;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Optional;

public class PostFormRequestWriter extends PostRequestWriter {

	public PostFormRequestWriter(PostFormRequest request) {
		super(request);
	}
	
	@Override
	public void writeParameters(final Request request, final Document document, Element xmlRequest) {
		super.writeParameters(request, document, xmlRequest);
		((PostFormRequest)request).getPostParameters().forEach(paramElem -> ParameterWriter.of(paramElem).writeXML(document, xmlRequest, Optional.empty()));
	}
}
