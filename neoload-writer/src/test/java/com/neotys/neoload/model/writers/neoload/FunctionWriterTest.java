package com.neotys.neoload.model.writers.neoload;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.neotys.neoload.model.function.Atoi;
import com.neotys.neoload.model.function.ImmutableAtoi;
import com.neotys.neoload.model.repository.EvalString;
import com.neotys.neoload.model.repository.ImmutableEvalString;

public class FunctionWriterTest {

	@Test
	public void testEvalStringWriteXml() throws ParserConfigurationException, TransformerException {
		final EvalString evalString = ImmutableEvalString.builder().name("lr_eval_string").returnValue("${think_time}").build();
		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root name=\"lr_eval_string\" uid=\"" + WriterUtils.getElementUid(evalString) + "\"/>";
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		EvalStringWriter.of(evalString).writeXML(doc, WrittingTestUtils.generateTestRootElement(doc), Files.createTempDir().getAbsolutePath());
		final String generatedXML = WrittingTestUtils.getXmlString(doc);
		Assertions.assertThat(generatedXML).isEqualTo(expectedResult);		
	}
	
	@Test
	public void testAtoiWriteXml() throws ParserConfigurationException, TransformerException {
		final Atoi atoi = ImmutableAtoi.builder().name("atoi_1").returnValue("${atoi_1}").args(ImmutableList.of("1")).build();
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		AtoiWriter.of(atoi).writeXML(doc, WrittingTestUtils.generateTestRootElement(doc), Files.createTempDir().getAbsolutePath());
		final String generatedXML = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedXML.substring(generatedXML.indexOf("ts=") + 4, generatedXML.indexOf("ts=") + 17);		
		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"scripts/jsAction_" + WriterUtils.getElementUid(atoi)+ ".js\" "
				+ "name=\"atoi_1\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(atoi)+ "\"/></test-root>";	
		
		Assertions.assertThat(generatedXML).isEqualTo(expectedResult);	
	}
}
