package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ConstantVariable;
import com.neotys.neoload.model.repository.ImmutableConstantVariable;
import com.neotys.neoload.model.repository.Variable;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.Assert.assertEquals;

public class ConstantVariableWriterTest {
	
	public static final ConstantVariable CONST = ImmutableConstantVariable.builder()
			.name("ConstVariable")
			.constantValue("Constant Value")
			.description("Test desc")
			.order(Variable.VariableOrder.SEQUENTIAL)
			.scope(Variable.VariableScope.LOCAL)
			.policy(Variable.VariablePolicy.EACH_VUSER)
			.noValuesLeftBehavior(Variable.VariableNoValuesLeftBehavior.CYCLE)
			.build(); 
	
	
	@Test
	public void writeXmlCounterTest() throws ParserConfigurationException, TransformerException{
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-constant constantValue=\"Constant Value\"" +
    			" name=\"ConstVariable\" order=\"1\" policy=\"4\" range=\"2\"" +
    			" whenOutOfValues=\"CYCLE_VALUES\">" + 
    			"<description>Test desc</description>" + 
    			"</variable-constant></test-root>";
    	
    	(new ConstantVariableWriter(CONST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
	}

}
