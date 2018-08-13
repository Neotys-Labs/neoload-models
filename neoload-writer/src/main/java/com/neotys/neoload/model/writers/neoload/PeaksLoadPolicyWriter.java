package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.PeaksLoadPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PeaksLoadPolicyWriter extends LoadPolicyWriter {

    public final static String XML_TAG_NAME = "peaks-volume-policy";
    public final static String XML_MINLOAD_ATTR = "defaultUserNumber";
    public final static String XML_MINDURATION_ATTR = "defaultDelay";
    public final static String XML_MINTYPE_ATTR = "defaultDelayType";
    public final static String XML_MAXLOAD_ATTR = "peakUserNumber";
    public final static String XML_MAXDURATION_ATTR = "peakDelay";
    public final static String XML_MAXTYPE_ATTR = "peakDelayType";
    public final static String XML_STARTPOLICY_ATTR = "startPoint";

    public PeaksLoadPolicyWriter(PeaksLoadPolicy peaksLoadPolicy) { super(peaksLoadPolicy); }

    @Override
    public void writeXML(Document document, Element currentElement) {
        PeaksLoadPolicy peaksLoadPolicy = (PeaksLoadPolicy) this.loadPolicy;
        Element volumePolicyElement = super.createElement(document, currentElement);
        Element xmlElement = document.createElement(XML_TAG_NAME);

        xmlElement.setAttribute(XML_MINLOAD_ATTR, String.valueOf(peaksLoadPolicy.getMinimumLoad()));
        peaksLoadPolicy.getMinimumTime().ifPresent(minimumTime -> {
            xmlElement.setAttribute(XML_MINDURATION_ATTR, String.valueOf(minimumTime));
            // Type is always seconds
            xmlElement.setAttribute(XML_MINTYPE_ATTR, "1");
        });
        peaksLoadPolicy.getMinimumIteration().ifPresent(minimumIteration -> {
            xmlElement.setAttribute(XML_MINDURATION_ATTR, String.valueOf(minimumIteration));
            // Type is always seconds
            xmlElement.setAttribute(XML_MINTYPE_ATTR, "0");
        });

        xmlElement.setAttribute(XML_MAXLOAD_ATTR, String.valueOf(peaksLoadPolicy.getMaximumLoad()));
        peaksLoadPolicy.getMaximumTime().ifPresent(maximumTime -> {
            xmlElement.setAttribute(XML_MAXDURATION_ATTR, String.valueOf(maximumTime));
            // Type is always seconds
            xmlElement.setAttribute(XML_MAXTYPE_ATTR, "1");
        });
        peaksLoadPolicy.getMaximumIteration().ifPresent(maximumIteration -> {
            xmlElement.setAttribute(XML_MAXDURATION_ATTR, String.valueOf(maximumIteration));
            // Type is always seconds
            xmlElement.setAttribute(XML_MAXTYPE_ATTR, "0");
        });
        if(peaksLoadPolicy.getStartPolicy() == PeaksLoadPolicy.StartPolicy.MINIMUM_LOAD) {
            xmlElement.setAttribute(XML_STARTPOLICY_ATTR, "0");
        } else {
            xmlElement.setAttribute(XML_STARTPOLICY_ATTR, "1");
        }
        volumePolicyElement.appendChild(xmlElement);
    }
}
