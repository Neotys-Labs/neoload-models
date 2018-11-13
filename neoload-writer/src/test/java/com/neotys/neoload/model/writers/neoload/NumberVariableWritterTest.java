package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class NumberVariableWritterTest {

	public static final CounterNumberVariable COUNTER = ImmutableCounterNumberVariable.builder()
			.name("CounterVariable_1")
			.increment(1)
			.maxValue(100)
			.startValue(1)
			.order(Variable.VariableOrder.SEQUENTIAL)
			.scope(Variable.VariableScope.UNIQUE)
			.policy(Variable.VariablePolicy.EACH_USE)
			.noValuesLeftBehavior(Variable.VariableNoValuesLeftBehavior.CYCLE)
			.build(); 
	
	@Test
	public void writeXmlCounterTest() throws ParserConfigurationException, TransformerException{
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-counter inc=\"1\" max=\"100\" name=\"CounterVariable_1\" order=\"1\" " 
    			+ "policy=\"1\" range=\"4\" starting=\"1\" "
    			+ "whenOutOfValues=\"CYCLE_VALUES\"/></test-root>";
    	
    	(new CounterNumberVariableWriter(COUNTER)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
	}
	
	public static final RandomNumberVariable RANDOM = ImmutableRandomNumberVariable.builder()
			.name("NumberRandomVariable_1")
			.maxValue(100)
			.minValue(1)
			.scope(Variable.VariableScope.LOCAL)
			.policy(Variable.VariablePolicy.EACH_USE)
			.build(); 
	
	@Test
	public void writeXmlRandomTest() throws ParserConfigurationException, TransformerException{
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-random-number max-value=\"100\" min-value=\"1\" "
    			+ "name=\"NumberRandomVariable_1\" policy=\"1\" "
    			+ "range=\"2\"/></test-root>";
    	
    	(new RandomNumberVariableWriter(RANDOM)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
	}
}
