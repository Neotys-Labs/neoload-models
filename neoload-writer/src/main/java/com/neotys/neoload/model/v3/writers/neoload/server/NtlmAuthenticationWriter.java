package com.neotys.neoload.model.v3.writers.neoload.server;

import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import org.w3c.dom.Document;

public class NtlmAuthenticationWriter extends LoginPasswordAuthenticationWriter {

    public NtlmAuthenticationWriter(NtlmAuthentication authentication) {
        super(authentication);
    }

    public void writeXML(final Document document, org.w3c.dom.Element serverElement) {
        org.w3c.dom.Element xmlAuthorization = document.createElement(XML_TAG_NAME);
        super.writeXML(xmlAuthorization);
        if(((NtlmAuthentication)authentication).getDomain().isPresent()) xmlAuthorization.setAttribute(XML_ATTRIBUTE_REALM, ((NtlmAuthentication)authentication).getDomain().get());
        xmlAuthorization.setAttribute(XML_ATTRIBUTE_SCHEME, "NTLM");
        serverElement.appendChild(xmlAuthorization);
    }
}
