package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.GoToNextIteration;
import com.neotys.neoload.model.repository.ImmutableGoToNextIteration;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class GoToNextIterationWriterTest {
	
	@Test
    public void writeGoToNextIterationTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);

    	final GoToNextIteration goToNextIteration = ImmutableGoToNextIteration.builder().name("goToNextIteration").build();

		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><gotonext-action name=\"goToNextIteration\" uid=\""
				+ WriterUtils.getElementUid(goToNextIteration)
				+ "\"/></test-root>";

    	GoToNextIterationWriter.of(goToNextIteration).writeXML(doc, root, Files.createTempDir().getAbsolutePath());
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedResult).isEqualTo(expectedResult);
    }
}
