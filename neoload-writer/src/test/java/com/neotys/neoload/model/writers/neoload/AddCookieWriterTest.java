package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.AddCookie;
import com.neotys.neoload.model.repository.ImmutableAddCookie;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Server;
public class AddCookieWriterTest {
	
	public static final Server SERVER_TEST = ImmutableServer.builder()
            .name("server_test")
            .host("host_test.com")
            .port("8080")
            .scheme("http")
            .build();
	
	@Test
	public void writeAddCookieXmlTest() throws ParserConfigurationException, TransformerException, IOException {
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		final AddCookie addCookie = ImmutableAddCookie.builder().name("setCookieForServer cookieName").cookieName("cookieName").cookieValue("cookieValue").server(SERVER_TEST).expires("Thu, 2 Aug 2007 20:47:11 UTC").path("cookiePath").build();
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		AddCookieWriter.of(addCookie).writeXML(doc, root, outputfolder);
		final String generatedResultXML = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedResultXML.substring(generatedResultXML.indexOf("ts=") + 4, generatedResultXML.indexOf("ts=") + 17);
		final String jsFile = "scripts/jsAction_" + WriterUtils.getElementUid(addCookie) + ".js";
		final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"" + jsFile + "\" "
				+ "name=\"setCookieForServer cookieName\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(addCookie)+ "\"/></test-root>";
		final String expectedResultJS = "// Add cookie to a HTTP server.\ncontext.currentVU.setCookieForServer(\"server_test\",\"cookieName=cookieValue; expires=Thu, 2 Aug 2007 20:47:11 UTC; path=cookiePath\");";
		final String generatedJS = Files.asCharSource(new File(outputfolder + File.separator + jsFile), Charset.defaultCharset()).read();		
		Assertions.assertThat(generatedResultXML).isEqualTo(expectedResultXML);
		Assertions.assertThat(generatedJS).isEqualTo(expectedResultJS);
	}
		
	@Test
	public void testbuildCookieNameValueDomain() {
		final AddCookie addCookie = ImmutableAddCookie.builder().name("name").cookieName("cookieName").cookieValue("cookieValue").server(SERVER_TEST).build();
		final String expectedResult = "cookieName=cookieValue";
		Assertions.assertThat(AddCookieWriter.buildCookie(addCookie)).isEqualTo(expectedResult);
	}
	
	@Test
	public void testbuildCookieNameValueDomainExpires() {
		final AddCookie addCookie = ImmutableAddCookie.builder().name("name").cookieName("cookieName").cookieValue("cookieValue").server(SERVER_TEST).expires("Thu, 2 Aug 2007 20:47:11 UTC").build();
		final String expectedResult = "cookieName=cookieValue; expires=Thu, 2 Aug 2007 20:47:11 UTC";
		Assertions.assertThat(AddCookieWriter.buildCookie(addCookie)).isEqualTo(expectedResult);
	}
	
	@Test
	public void testbuildCookieNameValueDomainPath() {
		final AddCookie addCookie = ImmutableAddCookie.builder().name("name").cookieName("cookieName").cookieValue("cookieValue").server(SERVER_TEST).path("cookiePath").build();
		final String expectedResult = "cookieName=cookieValue; path=cookiePath";
		Assertions.assertThat(AddCookieWriter.buildCookie(addCookie)).isEqualTo(expectedResult);
	}
		
	@Test
	public void testbuildCookieNameValueDomainExpiresPath() {
		final AddCookie addCookie = ImmutableAddCookie.builder().name("name").cookieName("cookieName").cookieValue("cookieValue").server(SERVER_TEST).expires("Thu, 2 Aug 2007 20:47:11 UTC").path("cookiePath").build();
		final String expectedResult = "cookieName=cookieValue; expires=Thu, 2 Aug 2007 20:47:11 UTC; path=cookiePath";
		Assertions.assertThat(AddCookieWriter.buildCookie(addCookie)).isEqualTo(expectedResult);
	}
	
	@Test
	public void testbuildJavascript() {
		
		final AddCookie addCookie = ImmutableAddCookie.builder().name("name").cookieName("cookieName").cookieValue("cookieValue").server(SERVER_TEST).expires("Thu, 2 Aug 2007 20:47:11 UTC").path("cookiePath").build();
		final String expectedResult = "// Add cookie to a HTTP server.\ncontext.currentVU.setCookieForServer(\"server_test\",\"cookieName=cookieValue; expires=Thu, 2 Aug 2007 20:47:11 UTC; path=cookiePath\");";
		Assertions.assertThat((new AddCookieWriter(addCookie)).getJavascriptContent()).isEqualTo(expectedResult);
	}
}
