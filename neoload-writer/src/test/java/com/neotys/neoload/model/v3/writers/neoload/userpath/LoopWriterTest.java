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

public class LoopWriterTest {
	
	@Test
    public void testWriteXML() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root>"
				+ "<loop-action loop=\"20\" name=\"looper\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.LOOP_TEST) + "\">" +
				"<description>a simple loop</description>" +
				"<embedded-action>" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_TEST) + "</embedded-action>" +
				"<embedded-action>" + WriterUtils.getElementUid(WrittingTestUtils.DELAY_TEST) + "</embedded-action>" +
				"</loop-action>" +
				"<basic-logical-action-container element-number=\"1\" execution-type=\"0\" name=\"Container_name\" slaProfileEnabled=\"false\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_TEST)
				+ "\" weightsEnabled=\"false\"><weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)
				+ "\"/></basic-logical-action-container><http-action actionType=\"1\" method=\"GET\" name=\"request_test\" path=\"/test_path\" serverUid=\"server_test\" slaProfileEnabled=\"false\" uid=\""
				+ WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST) +"\"/>"
				+ "<delay-action duration=\"200\" isThinkTime=\"false\" name=\"Delay_name\" uid=\""+ WriterUtils.getElementUid(WrittingTestUtils.DELAY_TEST) +"\"/>"
    			+ "</test-root>";


    	LoopWriter.of(WrittingTestUtils.LOOP_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

}
