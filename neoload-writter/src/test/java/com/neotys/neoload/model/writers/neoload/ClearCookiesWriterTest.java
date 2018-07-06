package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ClearCookies;
import com.neotys.neoload.model.repository.ImmutableClearCookies;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ClearCookiesWriterTest {

	@Test
	public void writeClearCookiesXmlTest() throws ParserConfigurationException, TransformerException {
		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);

		final ClearCookies clearCookies = ImmutableClearCookies.builder().name("web_cleanup_cookies").build();

		ClearCookiesWriter.of(clearCookies).writeXML(doc, root, "web_cleanup_cookies", Files.createTempDir().getAbsolutePath());
		String generatedResult = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedResult.substring(generatedResult.indexOf("ts=") + 4, generatedResult.indexOf("ts=") + 17);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"scripts/jsAction_d78b0b1faea953556ceba40841e8f3c456e6c14435a730d7f830f71b0ec1046a.js\" "
				+ "name=\"web_cleanup_cookies\" ts=\"" + timestamp + "\" "
				+ "uid=\"d78b0b1faea953556ceba40841e8f3c456e6c14435a730d7f830f71b0ec1046a\"/></test-root>";

		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
	}

}
