package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.Header;
import com.neotys.neoload.model.v3.project.userpath.Part;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;

public class RequestWriterTest {
	
	@Test
    public void writeRequestXmlWithOneParamTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" followRedirects=\"false\" "
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
    			+ "<test-root><http-action actionType=\"1\" followRedirects=\"false\" "
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
    			+ "<test-root><http-action actionType=\"1\" contentType=\"application/x-www-form-urlencoded\" followRedirects=\"false\" "
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
	public void writePutRequestTest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><http-action actionType=\"1\" contentType=\"application/x-www-form-urlencoded\" followRedirects=\"false\" "
				+ "method=\"PUT\" name=\"request_test\" "
				+ "path=\"/test_path\" postType=\"1\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
				+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST3_PUT_METHOD)+ "\">"
				+ "<parameter name=\"post param_name\" separator=\"=\" value=\"post_param Value\"/>"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"application/x-www-form-urlencoded\"/>"
				+ "</http-action></test-root>";

		(new RequestWriter(WrittingTestUtils.REQUEST_TEST3_PUT_METHOD)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
    public void writePostRequestTextDataTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" contentType=\"text/plain\" followRedirects=\"false\" "
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
	public void writePutRequestTextDataTest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><http-action actionType=\"1\" contentType=\"text/plain\" followRedirects=\"false\" "
				+ "method=\"PUT\" name=\"request_test\" "
				+ "path=\"/test_path\" postType=\"4\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
				+ "uid=\"" + WriterUtils.getElementUid(WrittingTestUtils.REQUEST_TEST4_PUT_METHOD)+ "\">"
				+ "<textPostContent><![CDATA[texte a convertir en binaire]]></textPostContent>"
				+ "<binaryPostContentBase64><![CDATA[dGV4dGUgYSBjb252ZXJ0aXIgZW4gYmluYWlyZQ==]]></binaryPostContentBase64>"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"text/plain\"/>"
				+ "</http-action></test-root>";

		(new RequestWriter(WrittingTestUtils.REQUEST_TEST4_PUT_METHOD)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}


	@Test
    public void writePostRequestBinaryDataTest() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" contentType=\"application/octet-stream\" followRedirects=\"false\" "
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
	public void writePostRequestMultipartContentTypeTest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);

		Request request = Request.builder()
				.name("request_test")
				.url("/test_path?param_name=param_value")
				.server("server_test")
				.method("POST")
				.addHeaders(Header.builder().name("Content-Type").value("multipart/mixed").build())
				.build();

		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><http-action actionType=\"1\" contentType=\"multipart/mixed\" followRedirects=\"false\" "
				+ "method=\"POST\" name=\"request_test\" "
				+ "path=\"/test_path\" postType=\"3\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
				+ "uid=\"" + WriterUtils.getElementUid(request)+ "\">"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"multipart/mixed\"/>"
				+ "</http-action></test-root>";

		(new RequestWriter(request)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writePostRequestMultipartTest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);

		Part filePart = Part.builder().filename("/myPath/myfilename").sourceFilename("/myPath/mysourcefilename").name("myFilePart").contentType("application/json").build();
		Part stringPart = Part.builder().value("My Part Value").name("myStringPart").build();

		Request request = Request.builder()
				.name("request_test")
				.url("/test_path?param_name=param_value")
				.server("server_test")
				.method("POST")
				.parts(ImmutableList.of(stringPart, filePart))
				.addHeaders(Header.builder().name("Content-Type").value("multipart/mixed").build())
				.build();

		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><http-action actionType=\"1\" contentType=\"multipart/mixed\" followRedirects=\"false\" "
				+ "method=\"POST\" name=\"request_test\" "
				+ "path=\"/test_path\" postType=\"3\" serverUid=\"server_test\" slaProfileEnabled=\"false\" "
				+ "uid=\"" + WriterUtils.getElementUid(request)+ "\">"
				+ "<multiparts>"
				+ "<multipart-string name=\"myStringPart\"\n"
				+ "            value=\"My Part Value\" valueMode=\"USE_VALUE\"/>"
				+ "<multipart-file attachedFilename=\"/myPath/mysourcefilename\"\n"
				+ "            contentType=\"application/json\" filename=\"/myPath/myfilename\" name=\"myFilePart\"/>"
				+ "</multiparts>"
				+ "<urlPostParameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>"
				+ "<header name=\"Content-Type\" value=\"multipart/mixed\"/>"
				+ "</http-action></test-root>";

		(new RequestWriter(request)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
    public void writeGetRequestTest() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" "
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
