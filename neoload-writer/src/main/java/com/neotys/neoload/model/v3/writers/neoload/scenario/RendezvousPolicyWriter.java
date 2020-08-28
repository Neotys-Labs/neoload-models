package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.RendezvousPolicy;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class RendezvousPolicyWriter {

	private static final String XML_TAG_NAME_PERCENTILE = "rdv-percentile-vu-count-policy";
	private static final String XML_TAG_NAME_FIXED_VU = "rdv-fixed-vu-count-policy";
	private static final String XML_TAG_NAME_MANUAL = "rdv-manual-policy";
	private static final String XML_ATTR_PERCENTILE_VU_COUNT = "percentileVirtualUserCount";
	private static final String XML_ATTR_FIXED_VU_COUNT = "fixedVirtualUserCount";
	private static final String XML_ATTR_NAME = "rendezVousName";
	private static final String XML_ATTR_TIMEOUT = "timeout";
	private static final String XML_ATTR_ENABLED = "isEnabled";

    private final RendezvousPolicy rendezvousPolicy;

    private RendezvousPolicyWriter(final RendezvousPolicy rendezvousPolicy) {
        this.rendezvousPolicy = rendezvousPolicy;
    }

    protected static RendezvousPolicyWriter of(final RendezvousPolicy rendezvousPolicy) {
        return new RendezvousPolicyWriter(rendezvousPolicy);
    }

    public void writeXML(final Document document, final Element currentElement) {

	    // Rendezvous Policy tag
	    Element xmlRendezvousPolicy = null;
	    final Object value = rendezvousPolicy.getWhen().getValue();
	    final WhenRelease.Type type = rendezvousPolicy.getWhen().getType();
	    switch (type){
		    case VU_NUMBER:
			    xmlRendezvousPolicy = document.createElement(XML_TAG_NAME_FIXED_VU);
			    xmlRendezvousPolicy.setAttribute(XML_ATTR_FIXED_VU_COUNT, String.valueOf(value));
		    	break;
		    case PERCENTAGE:
			    xmlRendezvousPolicy = document.createElement(XML_TAG_NAME_PERCENTILE);
			    xmlRendezvousPolicy.setAttribute(XML_ATTR_PERCENTILE_VU_COUNT, String.valueOf(value));
			    break;
		    case MANUAL:
			    xmlRendezvousPolicy = document.createElement(XML_TAG_NAME_MANUAL);
			    break;
	    }

        // Name attribute
        xmlRendezvousPolicy.setAttribute(XML_ATTR_NAME, rendezvousPolicy.getName());
        // Timeout attribute
        xmlRendezvousPolicy.setAttribute(XML_ATTR_TIMEOUT, String.valueOf(rendezvousPolicy.getTimeout()));
        // Enabled attribute
        xmlRendezvousPolicy.setAttribute(XML_ATTR_ENABLED, "true");

        currentElement.appendChild(xmlRendezvousPolicy);
    }
}
