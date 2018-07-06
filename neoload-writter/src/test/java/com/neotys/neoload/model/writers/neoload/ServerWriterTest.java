package com.neotys.neoload.model.writers.neoload;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class ServerWriterTest {
	
	@Test
    public void writeServerXmlTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-server hostname=\"host_test.com\" "
    			+ "name=\"server_test\" port=\"8080\" ssl=\"false\" "
    			+ "uid=\"server_test\"/>"
    			+ "</test-root>";
    	
    	ServerWriter.of(WrittingTestUtils.SERVER_TEST).writeXML(doc, root);
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
	
}
