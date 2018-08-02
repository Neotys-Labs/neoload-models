package com.neotys.neoload.model.writers.neoload;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.neotys.neoload.model.repository.PostBinaryRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.io.Files;

public class RequestWriterTest {
	
	@Test
    public void writeRequestXmlWithOneParamTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST2)+ "\">"
    			+ "<parameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
    			+ "</http-action></test-root>";
    	
    	(new GetPlainRequestWriter(WrittingTestUtils.REQUEST_TEST2)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
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
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
    			+ "</test-root>";
    	
    	(new GetPlainRequestWriter(WrittingTestUtils.REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
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
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST3)+ "\">"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
    			+ "<parameter name=\"post param_name\" separator=\"=\" value=\"post_param Value\"/>"
    			+ "</http-action></test-root>";
    	
    	(new PostFormRequestWriter(WrittingTestUtils.REQUEST_TEST3)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
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
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST4)+ "\">"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<textPostContent><![CDATA[texte a convertir en binaire]]></textPostContent>"
    			+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
    			+ "</http-action></test-root>";
    	
    	(new PostTextRequestWriter(WrittingTestUtils.REQUEST_TEST4)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
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
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST5)+ "\">"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
    			+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
    			+ "</http-action></test-root>";
    	
    	(new PostBinaryRequestWriter((PostBinaryRequest) WrittingTestUtils.REQUEST_TEST5)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }

	@Test
    public void writeGetRequestTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" method=\"GET\" "
    			+ "name=\"GET_REQUEST_TEST\" path=\"/loadtest/\" "
    			+ "serverUid=\"jack\" uid=\"" 
    			+ WriterUtils.getElementUid(WrittingTestUtils.GET_REQUEST_TEST)+ "\"/></test-root>";
    	
    	(new GetPlainRequestWriter(WrittingTestUtils.GET_REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	@Test
    public void writeGetFollowLinkRequestTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"3\" extractorPath=\"GET_FOLLOW_LINK_REQUEST_TEST\" "
    			+ "extractorServerUid=\"jack\" linkExtractorType=\"3\" method=\"GET\" name=\"GET_FOLLOW_LINK_REQUEST_TEST\" "
    			+ "refererUid=\"" 
    			+ WriterUtils.getElementUid(WrittingTestUtils.GET_REQUEST_TEST)+ "\" serverUid=\"jack\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.GET_FOLLOW_LINK_REQUEST_TEST)+ "\">"
    			+ "<record-html-infos extractorContent=\"Form\" extractorOccurence=\"1\" htmlType=\"1\"/>"
    			+ "<extractor-html-infos extractorContent=\"Form\" extractorOccurence=\"1\" htmlType=\"1\"/>"
    			+ "</http-action></test-root>";
    	
    	(new GetFollowLinkRequestWriter(WrittingTestUtils.GET_FOLLOW_LINK_REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
	
	@Test
    public void writePostSubmitFormRequestTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"4\" "
    			+ "confFormExtractorParameters=\"firstname,lastname,email,address,sex\" "
    			+ "extractorPath=\"POST_SUBMIT_FORM_REQUEST_TEST\" extractorServerUid=\"jack\" "
    			+ "linkExtractorType=\"6\" method=\"POST\" name=\"POST_SUBMIT_FORM_REQUEST_TEST\" "
    			+ "refererUid=\"" + WriterUtils.getElementUid(WrittingTestUtils.GET_FOLLOW_LINK_REQUEST_TEST)+ "\" "
    			+ "serverUid=\"jack\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.POST_SUBMIT_FORM_REQUEST_TEST)+ "\"><parameter name=\"firstname\" "
    			+ "separator=\"=\" value=\"a\"/><parameter name=\"lastname\" separator=\"=\" value=\"b\"/><parameter name=\"email\" "
    			+ "separator=\"=\" value=\"c@d.fr\"/><parameter name=\"address\" separator=\"=\" value=\"e\"/><parameter name=\"sex\" "
    			+ "separator=\"=\" value=\"Male\"/><record-html-infos extractorOccurence=\"1\" extractorRegExp=\"false\" "
    			+ "htmlType=\"2\"/><extractor-html-infos extractorOccurence=\"1\" extractorRegExp=\"false\" "
    			+ "htmlType=\"2\"/></http-action></test-root>";
    	
    	(new PostSubmitFormRequestWriter(WrittingTestUtils.POST_SUBMIT_FORM_REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
}
