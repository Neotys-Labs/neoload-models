package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

public class ThinktimeWriterTest {
	
	@Test
    public void writeThinktimeXmlTest() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final Delay thinktime = Delay.builder().isThinkTime(true).name("myThinktime").duration(DurationConstant.builder().value("1000").build()).description("myDescription").build();
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><delay-action duration=\"1000\" isThinkTime=\"true\" name=\"myThinktime\" uid=\"" + WriterUtils.getElementUid(thinktime)+ "\"><description>myDescription</description></delay-action>"
    			+ "</test-root>";	
    	DelayWriter.of(thinktime).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }


}
