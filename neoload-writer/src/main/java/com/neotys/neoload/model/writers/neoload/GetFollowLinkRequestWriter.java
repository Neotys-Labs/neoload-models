package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.GetFollowLinkRequest;
import com.neotys.neoload.model.repository.Request;

public class GetFollowLinkRequestWriter extends RequestWriter {

	private static final String XML_ATTR_LINKEXTRACTORTYPE = "linkExtractorType";
	private static final String ACTION_LINKEXTRACTOR_TYPE_MATCHCONTENT = "3";
	private static final String XML_ATTR_REFERER_UID = "refererUid";
		
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
		RecordHtmlInfosWriter.writeXML(document, xmlRequest, getFollowLinkRequest);		
		ExtractorHtmlInfosWriter.writeXML(document, xmlRequest, getFollowLinkRequest);
		final Request referer = getFollowLinkRequest.getReferer();
		xmlRequest.setAttribute(XML_ATTR_REFERER_UID, WriterUtils.getElementUid(referer));
		referer.getServer().ifPresent(server -> xmlRequest.setAttribute(XML_ATTR_SERV_UID, server.getName()));	
	}
}
