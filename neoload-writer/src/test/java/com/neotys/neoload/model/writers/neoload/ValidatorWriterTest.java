package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.ImmutableRegexpValidator;
import com.neotys.neoload.model.repository.ImmutableTextValidator;
import com.neotys.neoload.model.repository.Validator;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class ValidatorWriterTest{
	
	@Test
	public void writeXmlTest() throws ParserConfigurationException, TransformerException {
		Validator inputValidator = ImmutableTextValidator.builder()
				.name("webRegFind_TEST")
				.haveToContains(true)
				.validationText("test de valeur")
				.build();
		
	
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><assertion-content byXPath=\"false\" name=\"webRegFind_TEST\" " +
    			"notType=\"false\" pattern=\"test de valeur\"/>"
    			+ "</test-root>";
	
    	
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ValidatorWriter.getWriterFor(inputValidator).writeXML(doc, root);
		String generatedValidator = WrittingTestUtils.getXmlString(doc);
		assertEquals(expectedResult, generatedValidator);
	}
	
	
	@Test
	public void writeXmlTest2() throws ParserConfigurationException, TransformerException {
		Validator inputValidator = ImmutableRegexpValidator.builder()
				.name("webRegFind_TEST")
				.haveToContains(true)
				.validationRegex("the regex")
				.build();
		
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><assertion-response byXPath=\"false\" "
    			+ "name=\"webRegFind_TEST\" notType=\"false\">" 
    			+ "<testString>the regex</testString>"
    			+ "</assertion-response>"
    			+ "</test-root>";
		
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		ValidatorWriter.getWriterFor(inputValidator).writeXML(doc, root);
		String generatedValidator = WrittingTestUtils.getXmlString(doc);
		assertEquals(expectedResult, generatedValidator);
	}
}

