package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.neotys.neoload.model.function.Atoi;
import com.neotys.neoload.model.function.ImmutableAtoi;
import com.neotys.neoload.model.function.ImmutableStrcmp;
import com.neotys.neoload.model.function.Strcmp;
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
	public void testAtoiWriteXml() throws ParserConfigurationException, TransformerException, IOException {
		final Atoi atoi = ImmutableAtoi.builder().name("atoi_1").returnValue("${atoi_1}").args(ImmutableList.of("1")).build();
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		AtoiWriter.of(atoi).writeXML(doc, WrittingTestUtils.generateTestRootElement(doc), outputfolder);
		final String generatedXML = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedXML.substring(generatedXML.indexOf("ts=") + 4, generatedXML.indexOf("ts=") + 17);
		final String jsFile = "scripts/jsAction_" + WriterUtils.getElementUid(atoi) + ".js";
		final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"" + jsFile + "\" "
				+ "name=\"atoi_1\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(atoi)+ "\"/></test-root>";	
		final String expectedResultJS = "context.variableManager.setValue(\"atoi_1\", parseInt(\"1\"));";
		final String generatedJS = Files.asCharSource(new File(outputfolder + File.separator + jsFile), Charset.defaultCharset()).read();
		Assertions.assertThat(generatedXML).isEqualTo(expectedResultXML);	
		Assertions.assertThat(generatedJS).isEqualTo(expectedResultJS);
	}
	
	@Test
	public void testStrcmpWriteXml() throws ParserConfigurationException, TransformerException, IOException {
		final Strcmp strcmp = ImmutableStrcmp.builder().name("strcmp_1").returnValue("${strcmp_1}").args(ImmutableList.of("\"A\"", "\"B\"")).build();
		final Document doc = WrittingTestUtils.generateEmptyDocument();
		final String outputfolder = Files.createTempDir().getAbsolutePath();
		StrcmpWriter.of(strcmp).writeXML(doc, WrittingTestUtils.generateTestRootElement(doc), outputfolder);
		final String generatedXML = WrittingTestUtils.getXmlString(doc);
		final String timestamp = generatedXML.substring(generatedXML.indexOf("ts=") + 4, generatedXML.indexOf("ts=") + 17);
		final String jsFile = "scripts/jsAction_" + WriterUtils.getElementUid(strcmp) + ".js";
		final String expectedResultXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><js-action filename=\"" + jsFile + "\" "
				+ "name=\"strcmp_1\" ts=\"" + timestamp + "\" "
				+ "uid=\"" + WriterUtils.getElementUid(strcmp)+ "\"/></test-root>";	
		final String expectedResultJS = "var strcmp = String(\"A\").localeCompare(String(\"B\"));\ncontext.variableManager.setValue(\"strcmp_1\", strcmp);";
		final String generatedJS = Files.asCharSource(new File(outputfolder + File.separator + jsFile), Charset.defaultCharset()).read();
		Assertions.assertThat(generatedXML).isEqualTo(expectedResultXML);	
		Assertions.assertThat(generatedJS).isEqualTo(expectedResultJS);
	}
}
