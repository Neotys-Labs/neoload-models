package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.scenario.RampupLoadPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RampupLoadPolicyWriter extends LoadPolicyWriter {

    public static final String XML_TAG_NAME = "rampup-volume-policy";
    public static final String XML_INITIALUSERNUMBER_ATTR = "initialUserNumber";
    public static final String XML_USERINCREMENT_ATTR = "userIncrement";
    public static final String XML_MAXUSERNUMBER_ATTR = "maxUserNumber";
    public static final String XML_ITERATION_ATTR = "iterationNumber";
    public static final String XML_DELAYINCREMENT_ATTR = "delayIncrement";
    public static final String XML_DELAYTYPEINCREMENT_ATTR = "delayTypeIncrement";

    public RampupLoadPolicyWriter(RampupLoadPolicy rampupLoadPolicy) { super(rampupLoadPolicy); }

    @Override
    public void writeXML(Document document, Element currentElement) {
        RampupLoadPolicy rampupLoadPolicy = (RampupLoadPolicy) this.loadPolicy;
        Element volumePolicyElement = super.createElement(document, currentElement);
        Element xmlElement = document.createElement(XML_TAG_NAME);

        xmlElement.setAttribute(XML_INITIALUSERNUMBER_ATTR, String.valueOf(rampupLoadPolicy.getInitialLoad()));
        xmlElement.setAttribute(XML_USERINCREMENT_ATTR, String.valueOf(rampupLoadPolicy.getIncrementLoad()));
        rampupLoadPolicy.getIncrementTime().ifPresent(incrementTime -> {
            xmlElement.setAttribute(XML_DELAYINCREMENT_ATTR, String.valueOf(incrementTime));
            // Type is always seconds
            xmlElement.setAttribute(XML_DELAYTYPEINCREMENT_ATTR, "1");
        });
        rampupLoadPolicy.getIncrementIteration().ifPresent(incrementIterations -> {
            xmlElement.setAttribute(XML_DELAYINCREMENT_ATTR, String.valueOf(incrementIterations));
            // type is iteration
            xmlElement.setAttribute(XML_DELAYTYPEINCREMENT_ATTR, "0");
        });
        rampupLoadPolicy.getMaximumVirtualUsers().ifPresent(maxVU -> xmlElement.setAttribute(XML_MAXUSERNUMBER_ATTR, String.valueOf(maxVU)));
        rampupLoadPolicy.getIterationNumber().ifPresent(iterationNumber -> xmlElement.setAttribute(XML_ITERATION_ATTR, String.valueOf(iterationNumber)));
        volumePolicyElement.appendChild(xmlElement);
    }
}
