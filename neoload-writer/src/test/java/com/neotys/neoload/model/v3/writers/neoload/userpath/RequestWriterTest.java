package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class RequestWriterTest {
	
	@Test
    public void writeRequestXmlWithOneParamTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST2)+ "\">"
    			+ "<parameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
    			+ "</http-action></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.REQUEST_TEST2)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
	
	@Test
    public void writeRequestXmlWithoutParamsTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST)+ "\"/>"
    			+ "</test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
	
	@Test
    public void writePostRequestTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" contentType=\"application/x-www-form-urlencoded\" "
    			+ "method=\"POST\" name=\"request_test\" "
    			+ "path=\"/test_path\" postType=\"1\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST3)+ "\">"
				+ "<parameter name=\"post param_name\" separator=\"=\" value=\"post_param Value\"/>"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"application/x-www-form-urlencoded\"/>"
    			+ "</http-action></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.REQUEST_TEST3)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
	
	@Test
    public void writePostRequestTextDataTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" contentType=\"text/plain\" "
    			+ "method=\"POST\" name=\"request_test\" "
    			+ "path=\"/test_path\" postType=\"4\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST4)+ "\">"
				+ "<textPostContent><![CDATA[texte a convertir en binaire]]></textPostContent>"
    			+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"text/plain\"/>"
    			+ "</http-action></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.REQUEST_TEST4)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
		
	@Test
    public void writePostRequestBinaryDataTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" contentType=\"application/octet-stream\" "
    			+ "method=\"POST\" name=\"request_test\" "
    			+ "path=\"/test_path\" postType=\"2\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
    			+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST5)+ "\">"
    			+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"application/octet-stream\"/>"
    			+ "</http-action></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.REQUEST_TEST5)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

	@Test
    public void writeGetRequestTest() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" method=\"GET\" "
    			+ "name=\"GET_REQUEST_TEST\" path=\"/loadtest/\" "
    			+ "serverUid=\"jack\" slaProfileEnabled=\"false\" uid=\""
    			+ WriterUtils.getElementUid(WrittingTestUtils.GET_REQUEST_TEST)+ "\"/></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.GET_REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }
	
	/*@Test
    public void writePostSubmitFormRequestTest() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"4\" "
    			+ "confFormExtractorParameters=\"firstname,lastname,email,address,sex\" "
    			+ "extractorPath=\"POST_SUBMIT_FORM_REQUEST_TEST\" extractorServerUid=\"jack\" "
    			+ "linkExtractorType=\"6\" method=\"POST\" name=\"POST_SUBMIT_FORM_REQUEST_TEST\" postType=\"1\" "
    			//+ "refererUid=\"" + WriterUtils.getElementUid(WrittingTestUtils.GET_FOLLOW_LINK_REQUEST_TEST)+ "\" "
    			+ "serverUid=\"jack\" uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.POST_SUBMIT_FORM_REQUEST_TEST)+ "\"><parameter name=\"firstname\" "
    			+ "separator=\"=\" value=\"a\"/><parameter name=\"lastname\" separator=\"=\" value=\"b\"/><parameter name=\"email\" "
    			+ "separator=\"=\" value=\"c@d.fr\"/><parameter name=\"address\" separator=\"=\" value=\"e\"/><parameter name=\"sex\" "
    			+ "separator=\"=\" value=\"Male\"/><record-html-infos extractorOccurence=\"1\" extractorRegExp=\"false\" "
    			+ "htmlType=\"2\"/><extractor-html-infos extractorOccurence=\"1\" extractorRegExp=\"false\" "
    			+ "htmlType=\"2\"/></http-action></test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.POST_SUBMIT_FORM_REQUEST_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	
    	XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }

	@Test
	public void writeRequestWithRecordedFiles() throws ParserConfigurationException, TransformerException, IOException {

		final String outputFolder = Files.createTempDir().getAbsolutePath();
		final Path recordedRequestsFolderPath = Paths.get(outputFolder, RECORDED_REQUESTS_FOLDER);
		if (!recordedRequestsFolderPath.toFile().exists()) {
			createDirectory(recordedRequestsFolderPath);
		}

		final Path recordedResponsesFolderPath = Paths.get(outputFolder, RECORDED_RESPONSE_FOLDER);
		if (!recordedResponsesFolderPath.toFile().exists()) {
			createDirectory(recordedResponsesFolderPath);
		}

		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		(new RequestWriter(WrittingTestUtils.GET_REQUEST_WITH_RECORDED_FILES)).writeXML(doc, root, outputFolder);

		String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult)
				.contains("<requestContentFileDescription>recorded-requests/req_requestBody")
				.contains("<responseHeaders>HTTP/1.1 200 OK" + System.lineSeparator() +
						"Date: Thu, 11 Feb 2015 13:23:40 GMT" + System.lineSeparator() +
						"X-AspNet-Version: 2.0.50727" + System.lineSeparator() +
						"Cache-Control: private, max-age=0" + System.lineSeparator() +
						"Content-Type: application/json; charset=utf-8" + System.lineSeparator() +
						"Content-Length: 150" + System.lineSeparator() + System.lineSeparator() +
						"</responseHeaders>")
				.contains("<responsePageFileDescription>recorded-responses/res_responseBody_");
	}*/

}
