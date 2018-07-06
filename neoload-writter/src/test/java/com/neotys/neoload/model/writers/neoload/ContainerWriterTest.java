package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ContainerWriterTest {
	
	@Test
    public void writeContainerXmlWithOnePageTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><basic-logical-action-container element-number=\"1\" execution-type=\"0\" name=\"Container_name\" "
				+ "uid=\"a599f8054d3f64eace61ce0c43829b767a63743f640b67ddb6596a55afc844c7\" weightsEnabled=\"false\">"
    			+ "<weighted-embedded-action uid=\"3a9f971f17fb524d7065c030527970fd939656b5a463e19cc2ce7e447535d5fd\"/>"
    			+ "</basic-logical-action-container>"
    			+ "<http-page name=\"page_name\" thinkTime=\"0\" uid=\"3a9f971f17fb524d7065c030527970fd939656b5a463e19cc2ce7e447535d5fd\">"
    			+ "<embedded-action>575b0aed5cea8b6ddcdd8c64297d34298f6c855096b615e30f223c69519409eb</embedded-action></http-page>"
    			+ "<http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"575b0aed5cea8b6ddcdd8c64297d34298f6c855096b615e30f223c69519409eb\"/>"
    			+ "</test-root>";

    	ContainerWriter.of(WrittingTestUtils.CONTAINER_TEST).writeXML(doc, root, "ContainerPath", Files.createTempDir().getAbsolutePath());

    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);

    }

}
