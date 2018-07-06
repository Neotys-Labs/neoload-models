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
    public void writeDelayXmlTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><delay-action duration=\"1000\" name=\"myDelay\" uid=\"c65aa740540adb1b86b4b85216023c6f1feb410b55d504d143f1e29171bfc22d\"><description/></delay-action>"
    			+ "</test-root>";

    	final Delay delay = ImmutableDelay.builder().name("myDelay").delay("1000").description("myDescription").build();
    			
    	DelayWriter.of(delay).writeXML(doc, root, "delay", Files.createTempDir().getAbsolutePath());
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);

    }

}
