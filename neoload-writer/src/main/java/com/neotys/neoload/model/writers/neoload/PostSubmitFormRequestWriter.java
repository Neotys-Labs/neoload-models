package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Parameter;
import com.neotys.neoload.model.repository.PostSubmitFormRequest;
import com.neotys.neoload.model.repository.Request;

public class PostSubmitFormRequestWriter extends PostRequestWriter {
	
	public static final int FORM_CONTENT = 1;
		
	public PostSubmitFormRequestWriter(final PostSubmitFormRequest request) {
		super(request);
	}
	
	@Override
	protected int getActionType() {
		return 4;
	}	
	
	@Override
	protected void fillXML(final Document document, final Element xmlRequest, final Request theRequest) {
		super.fillXML(document, xmlRequest, theRequest);
		final PostSubmitFormRequest postSubmitFormRequest = (PostSubmitFormRequest)theRequest;
		xmlRequest.setAttribute(XML_ATTR_LINKEXTRACTORTYPE, ACTION_LINKEXTRACTOR_TYPE_MATCH_DEFINITION);
		xmlRequest.setAttribute(XML_ATTR_EXTRACTORPATH, theRequest.getPath().orElse(theRequest.getName()));
		xmlRequest.setAttribute(XML_ATTR_CONF_FORM_EXTRACTOR_PARAMETERS, buildConfFormExtractorParameters(postSubmitFormRequest));		
		final Element recordHtmlInfos = document.createElement(XML_TAG_RECORD_HTML_INFOS);
		recordHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_REGEXP, "false");
		recordHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_OCCURENCE, "1");
		recordHtmlInfos.setAttribute(XML_ATTR_HTML_TYPE, "2");
		xmlRequest.appendChild(recordHtmlInfos);					
		final Element extractorHtmlInfos = document.createElement(XML_TAG_EXTRACTOR_HTML_INFOS);
		extractorHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_REGEXP, "false");
		extractorHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_OCCURENCE, "1");
		extractorHtmlInfos.setAttribute(XML_ATTR_HTML_TYPE, "2");
		xmlRequest.appendChild(extractorHtmlInfos);
		final Request referer = postSubmitFormRequest.getReferer();
		xmlRequest.setAttribute(XML_ATTR_REFERER_UID, WriterUtils.getElementUid(referer));
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_SERV_UID, server.getName()));
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_EXTRACTOR_SERVER_UID, server.getName()));	
	}

	private static String buildConfFormExtractorParameters(final PostSubmitFormRequest postSubmitFormRequest) {		
		if(postSubmitFormRequest.getPostParameters().isEmpty()){
			return "";
		}
		final StringBuilder content = new StringBuilder();
		for(final Parameter parameter : postSubmitFormRequest.getPostParameters()){
			content.append(parameter.getName()).append(",");
		}
		return content.deleteCharAt(content.length()-1).toString();
	}
	
	@Override
	protected int getPostType() {
		return FORM_CONTENT;
	}
}
