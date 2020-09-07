package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustomStepWriter {

    private static final String XML_TAG_NAME = "custom-load-node";
    private static final String XML_ATTR_ITERATION = "iteration";
    private static final String XML_ATTR_POSITION = "position";
    private static final String XML_ATTR_TIME = "time";
    private static final String XML_ATTR_USERS = "users";

    private static final String DEFAULT_TIME_VALUE = "0";
    private static final String DEFAULT_ITERATION_VALUE = "0";

    private CustomStepWriter() {
    }

    public static Element getStepXML(Document document, CustomPolicyStep customPolicyStep, int index) {
        final Element xmlElement = document.createElement(XML_TAG_NAME);

        String customLoadPolicyDuration = String.valueOf(customPolicyStep
                .getWhen()
                .getValue()
        );
        if (LoadDuration.Type.ITERATION.equals(customPolicyStep.getWhen().getType())) {
            xmlElement.setAttribute(XML_ATTR_ITERATION, customLoadPolicyDuration);
            xmlElement.setAttribute(XML_ATTR_TIME, DEFAULT_TIME_VALUE);
        } else {
            xmlElement.setAttribute(XML_ATTR_TIME, customLoadPolicyDuration);
            xmlElement.setAttribute(XML_ATTR_ITERATION, DEFAULT_ITERATION_VALUE);
        }

        xmlElement.setAttribute(XML_ATTR_POSITION, String.valueOf(index));
        xmlElement.setAttribute(XML_ATTR_USERS, String.valueOf(customPolicyStep.getUsers()));

        return xmlElement;
    }
}
