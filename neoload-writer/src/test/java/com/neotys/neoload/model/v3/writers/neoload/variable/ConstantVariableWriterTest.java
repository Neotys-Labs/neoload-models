package com.neotys.neoload.model.v3.writers.neoload.variable;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;

public class ConstantVariableWriterTest {
	
	public static final ConstantVariable CONST = new ConstantVariable.Builder()
			.name("ConstVariable")
			.value("Constant Value")
			.description("Test desc")
			.order(Variable.Order.SEQUENTIAL)
			.scope(Variable.Scope.LOCAL)
			.changePolicy(Variable.ChangePolicy.EACH_USER)
			.outOfValue(Variable.OutOfValue.CYCLE)
			.build(); 
	
	
	@Test
	public void writeXmlCounterTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-constant constantValue=\"Constant Value\"" +
    			" name=\"ConstVariable\" order=\"1\" policy=\"4\" range=\"2\"" +
    			" whenOutOfValues=\"CYCLE_VALUES\">" + 
    			"<description>Test desc</description>" + 
    			"</variable-constant></test-root>";
    	
    	(new ConstantVariableWriter(CONST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

}
