package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.neotys.neoload.model.writers.neoload.NeoLoadWriter.RECORDED_REQUESTS_FOLDER;
import static com.neotys.neoload.model.writers.neoload.NeoLoadWriter.RECORDED_RESPONSE_FOLDER;
import static java.nio.file.Files.createDirectory;

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
    			+ "path=\"/test_path\" postType=\"1\" serverUid=\"server_test\" "
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
    			+ "path=\"/test_path\" postType=\"4\" serverUid=\"server_test\" "
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
    			+ "path=\"/test_path\" postType=\"2\" serverUid=\"server_test\" "
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
    			+ "linkExtractorType=\"6\" method=\"POST\" name=\"POST_SUBMIT_FORM_REQUEST_TEST\" postType=\"1\" "
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

		(new GetPlainRequestWriter(WrittingTestUtils.GET_REQUEST_WITH_RECORDED_FILES)).writeXML(doc, root, outputFolder);

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
	}

	@Test
	public void writeMultipartRequestTest() throws ParserConfigurationException {

		// write the repository
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.newDocument();
		Element xmlRequest = document.createElement("http-action");
		PostMultipartRequestWriter.of(WrittingTestUtils.POST_MULTIPART_REQUEST_TEST).writeXML(document, xmlRequest, Files.createTempDir().getAbsolutePath());
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").getLength()).isEqualTo(1);
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().getLength()).isEqualTo(3);
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("stringPart");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(0).getAttributes().getNamedItem("value").getNodeValue()).isEqualTo("partValue");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(0).getAttributes().getNamedItem("charSet").getNodeValue()).isEqualTo("UTF-8");

		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(1).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("filePart");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(1).getAttributes().getNamedItem("contentType").getNodeValue()).isEqualTo("image/jpg");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(1).getAttributes().getNamedItem("attachedFilename").getNodeValue()).isEqualTo("filename.jpg");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(1).getAttributes().getNamedItem("filename").getNodeValue()).isEqualTo("filename.jpg");

		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(2).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("filePart");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(2).getAttributes().getNamedItem("contentType").getNodeValue()).isEqualTo("image/jpg");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(2).getAttributes().getNamedItem("attachedFilename").getNodeValue()).isEqualTo("filename.jpg");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(2).getAttributes().getNamedItem("filename").getNodeValue()).isEqualTo("sentFileName.jpg");
		Assertions.assertThat(xmlRequest.getElementsByTagName("multiparts").item(0).getChildNodes().item(2).getAttributes().getNamedItem("transferEncoding").getNodeValue()).isEqualTo("transfer");

	}
}
