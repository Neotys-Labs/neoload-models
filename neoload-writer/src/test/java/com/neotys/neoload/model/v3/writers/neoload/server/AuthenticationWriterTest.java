package com.neotys.neoload.model.v3.writers.neoload.server;

import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;

public class AuthenticationWriterTest {

    @Test
    public void writeBasicAuthenticationXmlTest() throws ParserConfigurationException {
        Document doc = WrittingTestUtils.generateEmptyDocument();
        Element root = WrittingTestUtils.generateTestRootElement(doc);
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root>"
                + "<authorization password=\"passwd\" realm=\"mysecurerealm\"\n"
                + "        scheme=\"Basic\" userName=\"username\"/>"
                + "</test-root>";


        BasicAuthentication auth = BasicAuthentication.builder().login("username").password("passwd").realm("mysecurerealm").build();
        BasicAuthenticationWriter writer = new BasicAuthenticationWriter(auth);
        writer.writeXML(doc, root);

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

    @Test
    public void writeNtlmAuthenticationXmlTest() throws ParserConfigurationException {
        Document doc = WrittingTestUtils.generateEmptyDocument();
        Element root = WrittingTestUtils.generateTestRootElement(doc);
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root>"
                + "<authorization password=\"passwd\" realm=\"mydomain\"\n" +
                "        scheme=\"NTLM\" userName=\"username\"/>"
                + "</test-root>";


        NtlmAuthentication auth = NtlmAuthentication.builder().login("username").password("passwd").domain("mydomain").build();
        NtlmAuthenticationWriter writer = new NtlmAuthenticationWriter(auth);
        writer.writeXML(doc, root);

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

    @Test
    public void writeNegociateAuthenticationXmlTest() throws ParserConfigurationException {
        Document doc = WrittingTestUtils.generateEmptyDocument();
        Element root = WrittingTestUtils.generateTestRootElement(doc);
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<test-root>"
                + "<authorization password=\"passwd\" realm=\"mydomain\"\n" +
                "        scheme=\"Negociate\" userName=\"username\"/>"
                + "</test-root>";


        NegotiateAuthentication auth = NegotiateAuthentication.builder().login("username").password("passwd").domain("mydomain").build();
        NegotiateAuthenticationWriter writer = new NegotiateAuthenticationWriter(auth);
        writer.writeXML(doc, root);

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
}
