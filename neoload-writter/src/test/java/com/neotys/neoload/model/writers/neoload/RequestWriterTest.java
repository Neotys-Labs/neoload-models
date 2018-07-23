package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class RequestWriterTest {
	
	@Test
    public void writeRequestXmlWithOneParamTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"032e11f533cd97719d4e1a09b88dc201a1fa2a5f01da9b38631bcbaecf5b0bc4\">"
    			+ "<parameter name=\"param_name\" value=\"param_value\"/>"
    			+ "</http-action></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.REQUEST_TEST2)).writeXML(doc, root, "PagePath", Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	@Test
    public void writeRequestXmlWithoutParamsTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"032e11f533cd97719d4e1a09b88dc201a1fa2a5f01da9b38631bcbaecf5b0bc4\"/>"
    			+ "</test-root>";
    	
    	(new GetPlainRequestWriter(WrittingTestUtils.REQUEST_TEST)).writeXML(doc, root, "PagePath", Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	@Test
    public void writePostRequestTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"POST\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"032e11f533cd97719d4e1a09b88dc201a1fa2a5f01da9b38631bcbaecf5b0bc4\">"
				+ "<urlPostParameter name=\"param_name\" value=\"param_value\"/>"
    			+ "<parameter name=\"post param_name\" value=\"post_param Value\"/>"
    			+ "</http-action></test-root>";
    	
    	(new PostFormRequestWriter(WrittingTestUtils.REQUEST_TEST3)).writeXML(doc, root, "PagePath", Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	@Test
    public void writePostRequestTextDataTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"POST\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"032e11f533cd97719d4e1a09b88dc201a1fa2a5f01da9b38631bcbaecf5b0bc4\">"
				+ "<urlPostParameter name=\"param_name\" value=\"param_value\"/>"
				+ "<textPostContent><![CDATA[texte a convertir en binaire]]></textPostContent>"
    			+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
    			+ "</http-action></test-root>";
    	
    	(new PostTextRequestWriter(WrittingTestUtils.REQUEST_TEST4)).writeXML(doc, root, "PagePath", Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	
	@Test
    public void writePostRequestBinaryDataTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"POST\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"032e11f533cd97719d4e1a09b88dc201a1fa2a5f01da9b38631bcbaecf5b0bc4\">"
				+ "<urlPostParameter name=\"param_name\" value=\"param_value\"/>"
    			+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
    			+ "</http-action></test-root>";
    	
    	(new PostBinaryRequestWriter(WrittingTestUtils.REQUEST_TEST5)).writeXML(doc, root, "PagePath", Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }

}
