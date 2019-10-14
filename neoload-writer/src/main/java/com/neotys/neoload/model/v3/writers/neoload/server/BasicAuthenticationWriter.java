package com.neotys.neoload.model.v3.writers.neoload.server;

import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import org.w3c.dom.Document;

public class BasicAuthenticationWriter extends LoginPasswordAuthenticationWriter {

    public BasicAuthenticationWriter(BasicAuthentication authentication) {
        super(authentication);
    }

    public void writeXML(final Document document, org.w3c.dom.Element serverElement) {
        org.w3c.dom.Element xmlAuthorization = document.createElement(XML_TAG_NAME);
        super.writeXML(xmlAuthorization);
        if(((BasicAuthentication)authentication).getRealm().isPresent()) xmlAuthorization.setAttribute(XML_ATTRIBUTE_REALM, ((BasicAuthentication)authentication).getRealm().get());
        xmlAuthorization.setAttribute(XML_ATTRIBUTE_SCHEME, "Basic");
        serverElement.appendChild(xmlAuthorization);
    }
}
