package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class VariableExtractorTest {


	@Test
	public void writeXmlTestHeaders() throws ParserConfigurationException {
		VariableExtractor inputExtractor = VariableExtractor.builder()
				.name("webRegSaveParam_TEST")
				.regexp("left boundary(.*)right boundary")
				.matchNumber(2)
				.from(VariableExtractor.From.HEADER)
				.build();


		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"1\" endsWithSimple=\"\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"1\" extractTypeAdv=\"0\" extractTypeSimple=\"1\" matchNumber=\"2\" matchNumberAdv=\"2\" matchNumberSimple=\"2\" name=\"webRegSaveParam_TEST\" "
    			+ "regExp=\"left boundary(.*)right boundary\" "
    			+ "regExpAdv=\"left boundary(.*)right boundary\" setDefaultValue=\"false\" startsWithSimple=\"\" "
    			+ "template=\"$1$\" templateAdv=\"$1$\">"
				+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";


		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		VariableExtractorWriter.of(inputExtractor).writeXML(doc, root);
		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTestBody() throws ParserConfigurationException, TransformerException {
		VariableExtractor inputExtractor = VariableExtractor.builder()
				.name("webRegSaveParam_TEST")
				.regexp("left boundary(.*)right boundary")
				.matchNumber(2)
				.from(VariableExtractor.From.BODY)
				.build();


		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"1\" endsWithSimple=\"\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"0\" extractTypeAdv=\"0\" extractTypeSimple=\"0\" matchNumber=\"2\" matchNumberAdv=\"2\" matchNumberSimple=\"2\" name=\"webRegSaveParam_TEST\" "
    			+ "regExp=\"left boundary(.*)right boundary\" "
    			+ "regExpAdv=\"left boundary(.*)right boundary\" setDefaultValue=\"false\" startsWithSimple=\"\" "
    			+ "template=\"$1$\" templateAdv=\"$1$\">"
				+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";


		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		VariableExtractorWriter.of(inputExtractor).writeXML(doc, root);
		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTestBoth() throws ParserConfigurationException {
		VariableExtractor inputExtractor = VariableExtractor.builder()
				.name("webRegSaveParam_TEST")
				.regexp("left boundary(.*?)right boundary")
				.matchNumber(2)
				.from(VariableExtractor.From.BOTH)
				.build();


		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"1\" endsWithSimple=\"\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"5\" extractTypeAdv=\"0\" extractTypeSimple=\"5\" matchNumber=\"2\" matchNumberAdv=\"2\" matchNumberSimple=\"2\" "
    			+ "name=\"webRegSaveParam_TEST\" "
    			+ "regExp=\"left boundary(.*?)right boundary\" "
    			+ "regExpAdv=\"left boundary(.*?)right boundary\" setDefaultValue=\"false\" startsWithSimple=\"\" "
    			+ "template=\"$1$\" templateAdv=\"$1$\">"
				+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";


		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		VariableExtractorWriter.of(inputExtractor).writeXML(doc, root);
		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTestRegexp() throws ParserConfigurationException {
		VariableExtractor inputExtractor = VariableExtractor.builder()
				.name("SecurityString")
				.regexp("\"[A-Z0-9a-z\\\\+]+==\\|([0-9]+)\"\\];")
				.template("$3$")
				.from(VariableExtractor.From.BOTH)
				.build();


		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" "
    			+ "defaultValue=\"&lt;NOT FOUND&gt;\" displayMode=\"1\" endsWithSimple=\"\" "
    			+ "extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"5\" extractTypeAdv=\"0\" extractTypeSimple=\"5\" matchNumber=\"1\" matchNumberAdv=\"1\" matchNumberSimple=\"1\" name=\"SecurityString\" "
    			+ "regExp=\"&quot;[A-Z0-9a-z\\\\+]+==\\|([0-9]+)&quot;\\];\" "
    			+ "regExpAdv=\"&quot;[A-Z0-9a-z\\\\+]+==\\|([0-9]+)&quot;\\];\" setDefaultValue=\"false\" "
    			+ "startsWithSimple=\"\" template=\"$3$\" templateAdv=\"$3$\">"
    			+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" "
    			+ "type=\"4\"/></variable-extractor></test-root>";

		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		VariableExtractorWriter.of(inputExtractor).writeXML(doc, root);
		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTestXPath() throws ParserConfigurationException {
		VariableExtractor inputExtractor = VariableExtractor.builder()
				.name("ExtractedVariable_0")
				.xpath("myXPATH")
				.build();


		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" defaultValue=\"&lt;NOT FOUND&gt;\" "
    			+ "displayMode=\"1\" endsWithSimple=\"\" extractFromVarName=\"\" extractFromVarNameAdv=\"\" extractFromVarNameSimple=\"\" "
    			+ "extractType=\"3\" extractTypeAdv=\"0\" extractTypeSimple=\"3\" matchNumber=\"1\" "
    			+ "matchNumberAdv=\"1\" matchNumberSimple=\"1\" name=\"ExtractedVariable_0\" "
    			+ "regExp=\"(.*)\" regExpAdv=\"(.*)\" "
    			+ "setDefaultValue=\"false\" startsWithSimple=\"\" template=\"$1$\" templateAdv=\"$1$\" "
    			+ "xpath=\"myXPATH\">"
    			+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";

		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		VariableExtractorWriter.of(inputExtractor).writeXML(doc, root);
		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}
	
	@Test
	public void writeXmlTestJsonPath() throws ParserConfigurationException {
		VariableExtractor inputExtractor = VariableExtractor.builder()
				.name("ExtractedVariable_0")
				.jsonPath("myJSONPath")
				.build();
		
		final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-extractor assertionOnNoMatch=\"true\" defaultValue=\"&lt;NOT FOUND&gt;\" "
    			+ "displayMode=\"1\" endsWithSimple=\"\" extractFromVarName=\"\" extractFromVarNameAdv=\"\" "
    			+ "extractFromVarNameSimple=\"\" extractType=\"4\" extractTypeAdv=\"0\" extractTypeSimple=\"4\" "
    			+ "jsonpath=\"myJSONPath\" matchNumber=\"1\" matchNumberAdv=\"1\" matchNumberSimple=\"1\" "
    			+ "name=\"ExtractedVariable_0\" regExp=\"(.*)\" "
    			+ "regExpAdv=\"(.*)\" setDefaultValue=\"false\" startsWithSimple=\"\" template=\"$1$\" "
    			+ "templateAdv=\"$1$\">"
    			+ "<variable-extractor-group extract=\"true\" occurs=\"1\" pattern=\"\" type=\"4\"/>"
    			+ "</variable-extractor></test-root>";
	    	
		
		Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
		VariableExtractorWriter.of(inputExtractor).writeXML(doc, root);
		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}
	
}
