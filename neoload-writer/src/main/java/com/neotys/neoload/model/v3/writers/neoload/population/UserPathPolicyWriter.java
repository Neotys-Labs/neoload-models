package com.neotys.neoload.model.v3.writers.neoload.population;

import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserPathPolicyWriter {

    public static final String XML_TAG_NAME = "split";
    public static final String XML_FACTOR_ATTR = "factor";
    public static final String XML_VUUID_ATTR = "virtualUserUid";

    private final UserPathPolicy policy;

    public UserPathPolicyWriter(UserPathPolicy policy) {
        this.policy = policy;
    }

    public static UserPathPolicyWriter of(UserPathPolicy policy) {
        return new UserPathPolicyWriter(policy);
    }

    public void writeXML(final Document document, final Element currentElement) {
        Element xmlParam = document.createElement(XML_TAG_NAME);
        xmlParam.setAttribute(XML_FACTOR_ATTR, String.valueOf(policy.getDistribution().orElse(100.00)));
        xmlParam.setAttribute(XML_VUUID_ATTR, policy.getName());
        currentElement.appendChild(xmlParam);
    }
}
