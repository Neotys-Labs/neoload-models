package com.neotys.neoload.model.v3.writers.neoload.scenario;

import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.stream.IntStream;

public class CustomLoadPolicyWriter extends LoadPolicyWriter {

    private static final String XML_TAG_NAME = "custom-volume-policy";
    private static final String XML_ATTR_MAX_USERS = "maxUsers";
    private static final String XML_ATTR_TOTAL_LENGTH = "totalLength";
    private static final String XML_ATTR_ITERATION = "iteration";

    private static final String DEFAULT_TIME_VALUE = "60";
    private static final String DEFAULT_ITERATION_VALUE = "10";

    public CustomLoadPolicyWriter(CustomLoadPolicy customLoadPolicy) {
        super(customLoadPolicy);
    }

    @Override
    protected void writeVolumePolicyXML(Document document, Element currentElement) {
        final CustomLoadPolicy customLoadPolicy = (CustomLoadPolicy) this.loadPolicy;

        final Element volumePolicyElement = super.createVolumePolicyElement(document, currentElement);
        final Element xmlElement = document.createElement(XML_TAG_NAME);

        xmlElement.setAttribute(
                XML_ATTR_MAX_USERS,
                String.valueOf(customLoadPolicy.getSteps().stream()
                        .map(CustomPolicyStep::getUsers)
                        .max(Integer::compareTo).orElse(0))
        );

        String customLoadPolicyDuration = String.valueOf(customLoadPolicy.getSteps()
                .get(customLoadPolicy.getSteps().size()-1)
                .getWhen()
                .getValue()
        );
        if (customLoadPolicy.getDuration() != null && LoadDuration.Type.ITERATION.equals(customLoadPolicy.getDuration().getType())) {
            xmlElement.setAttribute(XML_ATTR_ITERATION, customLoadPolicyDuration);
            xmlElement.setAttribute(XML_ATTR_TOTAL_LENGTH, DEFAULT_TIME_VALUE);
        } else {
            xmlElement.setAttribute(XML_ATTR_TOTAL_LENGTH, customLoadPolicyDuration);
            xmlElement.setAttribute(XML_ATTR_ITERATION, DEFAULT_ITERATION_VALUE);
        }

        IntStream.range(0, customLoadPolicy.getSteps().size())
                .forEachOrdered(index -> xmlElement.appendChild(CustomStepWriter.getStepXML(document, customLoadPolicy.getSteps().get(index),index)));

        volumePolicyElement.appendChild(xmlElement);
    }
}
