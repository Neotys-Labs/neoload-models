package com.neotys.neoload.model.v3.writers.neoload.userpath;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;

public class ContainerWriterTest {
	
	@Test
    public void writeXmlTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root>"
    			+ "<basic-logical-action-container element-number=\"1\" execution-type=\"0\" name=\"Container_name\" "
				+ "slaProfileEnabled=\"false\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_TEST)+ "\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
    			+ "</basic-logical-action-container>"
    			+ "<http-action actionType=\"1\" followRedirects=\"false\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
    			+ "</test-root>";

    	ContainerWriter.of(WrittingTestUtils.CONTAINER_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

    	XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

	@Test
    public void writeXmlWithAssertionTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root>"
    			+ "<basic-logical-action-container element-number=\"1\" execution-type=\"0\" name=\"Container_name\" "
				+ "slaProfileEnabled=\"false\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_WITH_ASSERTIONS_TEST)+ "\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
    			+ "<assertions>"
		        + "<assertion-content name=\"assertion_1\" notType=\"false\" pattern=\"container_contains_1\"/>"
		        + "</assertions>"
    			+ "</basic-logical-action-container>"
    			+ "<http-action actionType=\"1\" followRedirects=\"false\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
    			+ "</test-root>";

    	ContainerWriter.of(WrittingTestUtils.CONTAINER_WITH_ASSERTIONS_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

    	XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

	/*@Test
	public void writeContainerXmlWithOneSharedContainerAsChild() throws ParserConfigurationException, TransformerException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><basic-logical-action-container element-number=\"1\" execution-type=\"0\" name=\"Container_with_shared_child\" "
				+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.CONTAINER_WITH_SHARED_CHILD_TEST)+ "\" weightsEnabled=\"false\">"
				+ "<weighted-embedded-action uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.SHARED_CONTAINER_TEST)+ "\"/>"
				+ "</basic-logical-action-container>"
				+ "</test-root>";

		ContainerWriter.of(WrittingTestUtils.CONTAINER_WITH_SHARED_CHILD_TEST).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);

	}*/

}
