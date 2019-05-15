package com.neotys.neoload.model.v3.writers.neoload.server;

import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class ServerWriterTest {
	
	@Test
    public void writeServerXmlTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-server hostname=\"host_test.com\" "
    			+ "name=\"server_test\" port=\"8080\" ssl=\"false\" "
    			+ "uid=\"server_test\"/>"
    			+ "</test-root>";
    	
    	ServerWriter.of(WrittingTestUtils.SERVER_TEST).writeXML(doc, root);

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

    @Test
	public void writeSSLServerXmlTest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><http-server hostname=\"host_test.com\" "
				+ "name=\"server_test\" port=\"443\" ssl=\"true\" "
				+ "uid=\"server_test\"/>"
				+ "</test-root>";

		final Server server = Server.builder()
				.name("server_test")
				.host("host_test.com")
				.port("443")
				.scheme(Server.Scheme.HTTPS)
				.build();
		ServerWriter.of(server).writeXML(doc, root);

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}
	
}
