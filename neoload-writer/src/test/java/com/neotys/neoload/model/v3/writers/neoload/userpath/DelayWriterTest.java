package com.neotys.neoload.model.v3.writers.neoload.userpath;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;

public class DelayWriterTest {

	
	@Test
    public void writeDelayXmlTest() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final Delay delay = new Delay.Builder().name("myDelay").value("1000").description("myDescription").build();
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><delay-action duration=\"1000\" isThinkTime=\"false\" name=\"myDelay\" uid=\"" + WriterUtils.getElementUid(delay)+ "\"><description>myDescription</description></delay-action>"
    			+ "</test-root>";	
    	DelayWriter.of(delay).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

}
