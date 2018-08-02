package com.neotys.neoload.model.writers.neoload;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ParameterWriterTest {
	
	@Test
    public void writeRequestXmlTestWithValue() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><parameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
    			+ "</test-root>";
    	
    	(new ParameterWriter(WrittingTestUtils.PARAMETER_TEST)).writeXML(doc, root, Optional.empty());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
	
	@Test
    public void writeRequestXmlTestWithEmptyValue() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><parameter name=\"param_name\" separator=\"=\" value=\"\"/>"
    			+ "</test-root>";
    	
    	(new ParameterWriter(WrittingTestUtils.PARAMETER_TEST_EMPTY_VALUE)).writeXML(doc, root, Optional.empty());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
	
	@Test
    public void writeRequestXmlTestWithNoValue() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><parameter name=\"param_name\" separator=\"\" value=\"\"/>"
    			+ "</test-root>";
    	
    	(new ParameterWriter(WrittingTestUtils.PARAMETER_TEST_NO_VALUE)).writeXML(doc, root, Optional.empty());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }
}
