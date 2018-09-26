package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.ImmutableStop;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class StopWriterTest {

	@Test
	public void writeStopWithStartNewVirtualUserTest() throws ParserConfigurationException, TransformerException {
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		final ImmutableStop stopWithStartNewVirtualUser = ImmutableStop.builder().name("stop").startNewVirtualUser(true).build();

		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><stopvu-action name=\"stop\" startNewVu=\"true\" uid=\""
				+ WriterUtils.getElementUid(stopWithStartNewVirtualUser)
				+ "\"/></test-root>";

		StopWriter.of(stopWithStartNewVirtualUser).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
		final String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
	}

	@Test
	public void writeStopWithoutStartNewVirtualUserTest() throws ParserConfigurationException, TransformerException {
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final Element root = WrittingTestUtils.generateTestRootElement(doc);

		final ImmutableStop stopWithoutStartNewVirtualUser = ImmutableStop.builder().name("stop").startNewVirtualUser(false).build();

		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><stopvu-action name=\"stop\" startNewVu=\"false\" uid=\""
				+ WriterUtils.getElementUid(stopWithoutStartNewVirtualUser)
				+ "\"/></test-root>";

		StopWriter.of(stopWithoutStartNewVirtualUser).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
		final String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
	}
}
