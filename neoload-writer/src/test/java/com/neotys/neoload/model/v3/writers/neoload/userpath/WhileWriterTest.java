package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;

public class WhileWriterTest {
	
	@Test
    public void testWriteXML() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><while-action name=\"while\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.WHILE_TEST) + "\">" +
				"<embedded-action>" + WriterUtils.getElementUid(WrittingTestUtils.DELAY_TEST) + "</embedded-action>" +
				"<conditions match-type=\"1\">" +
				"<condition operand1=\"1\" operand2=\"2\" operator=\"GREATER\"/>" +
				"</conditions>" +
				"</while-action><delay-action duration=\"200\" isThinkTime=\"false\" name=\"Delay_name\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.DELAY_TEST) + "\"/>" +
    			"</test-root>";

    	WhileWriter.of(WrittingTestUtils.WHILE_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

}
