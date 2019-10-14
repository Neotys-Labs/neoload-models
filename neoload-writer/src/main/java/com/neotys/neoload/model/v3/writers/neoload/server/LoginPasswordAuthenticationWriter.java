package com.neotys.neoload.model.v3.writers.neoload.server;

import com.neotys.neoload.model.v3.project.server.LoginPasswordAuthentication;
import org.w3c.dom.Document;

public abstract class LoginPasswordAuthenticationWriter {

    public static final String XML_TAG_NAME = "authorization";
    public static final String XML_ATTRIBUTE_USERNAME = "userName";
    public static final String XML_ATTRIBUTE_PASSWORD = "password";
    public static final String XML_ATTRIBUTE_REALM = "realm";
    public static final String XML_ATTRIBUTE_SCHEME = "scheme";


    LoginPasswordAuthentication authentication;

    public LoginPasswordAuthenticationWriter(LoginPasswordAuthentication authentication) {
        this.authentication = authentication;
    }

    public abstract void writeXML(final Document document, org.w3c.dom.Element serverElement);

    public void writeXML(org.w3c.dom.Element xmlAuthorization) {
        xmlAuthorization.setAttribute(XML_ATTRIBUTE_USERNAME, authentication.getLogin());
        xmlAuthorization.setAttribute(XML_ATTRIBUTE_PASSWORD, authentication.getPassword());
    }
}
