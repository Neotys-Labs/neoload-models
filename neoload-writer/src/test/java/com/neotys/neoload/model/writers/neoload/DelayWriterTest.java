package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.Delay;
import com.neotys.neoload.model.repository.ImmutableDelay;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class DelayWriterTest {
	
	@Test
    public void writeDelayWithThinktimeXmlTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final Delay delay = ImmutableDelay.builder().name("myDelay").delay("1000").isThinkTime(true).description("myDescription").build();
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><delay-action duration=\"1000\" isThinkTime=\"true\" name=\"myDelay\" uid=\"" + WriterUtils.getElementUid(delay)+ "\"><description>myDescription</description></delay-action>"
    			+ "</test-root>";	
    	DelayWriter.of(delay).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	@Test
    public void writeDelayNoThinktimeXmlTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final Delay delay = ImmutableDelay.builder().name("myDelay").delay("1000").isThinkTime(false).description("myDescription").build();
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><delay-action duration=\"1000\" isThinkTime=\"false\" name=\"myDelay\" uid=\"" + WriterUtils.getElementUid(delay)+ "\"><description>myDescription</description></delay-action>"
    			+ "</test-root>";	
    	DelayWriter.of(delay).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }

}
