package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.population.UserPathPolicy;

public class UserPathPolicyWriter {
    public static final String XML_TAG_SPLIT_NAME = "split";
    public static final String XML_FACTOR_ATTR = "factor";
    public static final String XML_VUUID_ATTR = "virtualUserUid";

    private final UserPathPolicy userPath;

    public UserPathPolicyWriter(UserPathPolicy userPath) {
        this.userPath = userPath;
    }

    public static UserPathPolicyWriter of(UserPathPolicy userPath) {
        return new UserPathPolicyWriter(userPath);
    }

    public void writeXML(final Document document, final Element currentElement) {
        // Split tag
        Element xmlParam = document.createElement(XML_TAG_SPLIT_NAME);
        xmlParam.setAttribute(XML_FACTOR_ATTR, String.valueOf(userPath.getDistribution()));
        xmlParam.setAttribute(XML_VUUID_ATTR, userPath.getName());
        currentElement.appendChild(xmlParam);
    }
}
