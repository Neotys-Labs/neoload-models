package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class SharedContainerWriterTest {
	
	@Test
    public void writeContainerXmlWithOnePageTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><basic-logical-action-container element-number=\"1\" execution-type=\"0\" name=\"Shared_Container_name\" "
				+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SHARED_CONTAINER_TEST)+ "\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.PAGE_TEST)+ "\"/>"
    			+ "</basic-logical-action-container>"
    			+ "<http-page executeResourcesDynamically=\"false\" name=\"page_name\" thinkTime=\"0\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.PAGE_TEST)+ "\">"
    			+ "<embedded-action>" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "</embedded-action></http-page>"
    			+ "<http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
				+ "<shared-element>"+WriterUtils.getElementUid(WrittingTestUtils.SHARED_CONTAINER_TEST)+"</shared-element>"
    			+ "</test-root>";

		SharedContainerWriter.of(WrittingTestUtils.SHARED_CONTAINER_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);

    }

}
