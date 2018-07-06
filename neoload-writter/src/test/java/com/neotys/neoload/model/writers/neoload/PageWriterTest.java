package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class PageWriterTest {

	@Test
    public void writePageXmlWithOneRequestTest() throws ParserConfigurationException, TransformerException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><http-page name=\"page_name\" thinkTime=\"0\" uid=\"4bb170840413c41ce5d19e9adb4fc8b8f25baf5f1a2f8f67991621b3de13f434\">"
    			+ "<embedded-action>6792b0a7f82494dd48e979a2b0860d23e2576b7cd9e71b138674bed7f5ba4e9e</embedded-action></http-page>"
    			+ "<http-action actionType=\"1\" "
    			+ "method=\"GET\" name=\"request_test\" "
    			+ "path=\"/test_path\" serverUid=\"server_test\" "
    			+ "uid=\"6792b0a7f82494dd48e979a2b0860d23e2576b7cd9e71b138674bed7f5ba4e9e\"/>"
    			+ "</test-root>";
    	
    	(new PageWriter(WrittingTestUtils.PAGE_TEST)).writeXML(doc, root, "pageNameParent", Files.createTempDir().getAbsolutePath());
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }

}
