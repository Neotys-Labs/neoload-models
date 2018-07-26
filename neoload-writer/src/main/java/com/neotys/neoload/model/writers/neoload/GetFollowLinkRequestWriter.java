package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.GetFollowLinkRequest;
import com.neotys.neoload.model.repository.Request;

public class GetFollowLinkRequestWriter extends RequestWriter {
	
	public GetFollowLinkRequestWriter(final GetFollowLinkRequest request) {
		super(request);
	}
	
	@Override
	protected int getActionType() {
		return 3;
	}	
	
	@Override
	protected void fillXML(final Document document, final Element xmlRequest, final Request theRequest) {
		super.fillXML(document, xmlRequest, theRequest);
		final GetFollowLinkRequest getFollowLinkRequest = (GetFollowLinkRequest)theRequest;
		xmlRequest.setAttribute(XML_ATTR_LINKEXTRACTORTYPE, ACTION_LINKEXTRACTOR_TYPE_MATCHCONTENT);
		xmlRequest.setAttribute(XML_ATTR_EXTRACTORPATH, theRequest.getName());		
		final Element recordHtmlInfos = document.createElement(XML_TAG_RECORD_HTML_INFOS);
		recordHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_CONTENT, getFollowLinkRequest.getText());
		recordHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_OCCURENCE, "1");
		recordHtmlInfos.setAttribute(XML_ATTR_HTML_TYPE, "1");
		xmlRequest.appendChild(recordHtmlInfos);				
		final Element extractorHtmlInfos = document.createElement(XML_TAG_EXTRACTOR_HTML_INFOS);
		extractorHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_CONTENT, getFollowLinkRequest.getText());
		extractorHtmlInfos.setAttribute(XML_ATTR_EXTRACTOR_OCCURENCE, "1");
		extractorHtmlInfos.setAttribute(XML_ATTR_HTML_TYPE, "1");
		xmlRequest.appendChild(extractorHtmlInfos);
		final Request referer = getFollowLinkRequest.getReferer();
		xmlRequest.setAttribute(XML_ATTR_REFERER_UID, WriterUtils.getElementUid(referer));
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_SERV_UID, server.getName()));
		theRequest.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_EXTRACTOR_SERVER_UID, server.getName()));	
	}
}
