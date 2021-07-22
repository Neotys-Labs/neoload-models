package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.Header;
import com.neotys.neoload.model.v3.project.userpath.Page;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	
	@Test
    public void writeGetRequestWithAssertionsTest() throws ParserConfigurationException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root>"
    			+ "<http-action actionType=\"1\" followRedirects=\"false\" method=\"GET\" "
    			+ "name=\"GET_REQUEST_TEST\" path=\"/loadtest/\" "
    			+ "serverUid=\"jack\" slaProfileEnabled=\"false\" uid=\""
    			+ WriterUtils.getElementUid(WrittingTestUtils.GET_REQUEST_WITH_ASSERTIONS_TEST)+ "\">"
    			+ "<assertions>"
    			+ "<assertion-content name=\"assertion_1\" notType=\"false\" pattern=\"request_contains_1\"/>"
    			+ "</assertions>"
    			+ "</http-action>"
    			+ "</test-root>";
    	
    	(new RequestWriter(WrittingTestUtils.GET_REQUEST_WITH_ASSERTIONS_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

        XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
    }


    @Test
	public void writeDynamicRequest() throws ParserConfigurationException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);

		Request request = Request.builder()
				.name("request_test")
				.url("/test_path?param_name=param_value")
				.server("server_test")
				.method("GET")
				.isDynamic(true)
				.addHeaders(Header.builder().name("Content-Type").value("Text/Html").build())
				.build();

		new RequestWriter(request).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		final Set<Map.Entry<com.neotys.neoload.model.v3.project.Element, String>> generatedUids = WriterUtils.getGeneratedUids();
		assertEquals(2,generatedUids.size());
		final Optional<String> pageId = generatedUids.stream().filter(c -> c.getKey() instanceof Page).map(Map.Entry::getValue).findAny();
		final Optional<String> requestId = generatedUids.stream().filter(c -> c.getKey() instanceof Request).map(Map.Entry::getValue).findAny();
		assertTrue(pageId.isPresent());
		assertTrue(requestId.isPresent());
		String expectedResult =
				"<test-root>" +
					"<http-page executeResourcesDynamically=\"true\" name=\"/test_path\" slaProfileEnabled=\"false\" uid=\""+pageId.get()+"\">" +
						"<embedded-action>"+requestId.get()+"</embedded-action>" +
					"</http-page>" +
					"<http-action actionType=\"1\" contentType=\"Text/Html\" followRedirects=\"false\" method=\"GET\" name=\"request_test\" path=\"/test_path\" serverUid=\"server_test\" slaProfileEnabled=\"false\" uid=\""+requestId.get()+"\"><parameter name=\"param_name\" separator=\"=\" value=\"param_value\"/>" +
						"<header name=\"Content-Type\" value=\"Text/Html\"/>" +
					"</http-action>" +
				"</test-root>" ;


		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

}
