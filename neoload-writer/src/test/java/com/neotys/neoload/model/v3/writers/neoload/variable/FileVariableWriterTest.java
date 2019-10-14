package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;


public class FileVariableWriterTest {
	
	@Test
	public void writeXmlTest1() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-file delimiters=\",\" "
    			+ "filename=\"path_du_fichier\" name=\"variable_test\" "
    			+ "offset=\"2\" order=\"1\" policy=\"5\" range=\"1\" "
    			+ "useFirstLine=\"true\" whenOutOfValues=\"CYCLE_VALUES\">"
    			+ "<column name=\"colonneTest\" number=\"0\"/>"
    			+ "</variable-file></test-root>";
    	
    	(new FileVariableWriter(WrittingTestUtils.VARIABLE_TEST)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}


	@Test
	public void writeXmlTest2() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-file delimiters=\",\" "
    			+ "filename=\"path_du_fichier\" name=\"variable_test\" "
    			+ "offset=\"2\" order=\"2\" policy=\"1\" range=\"2\" "
    			+ "useFirstLine=\"true\" whenOutOfValues=\"STOP_TEST\">"
    			+ "<column name=\"colonneTest\" number=\"0\"/>"
    			+ "</variable-file></test-root>";

    	(new FileVariableWriter(WrittingTestUtils.VARIABLE_TEST2)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTest3() throws ParserConfigurationException {
    	Document doc = WrittingTestUtils.generateEmptyDocument();
    	Element root = WrittingTestUtils.generateTestRootElement(doc);
    	String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    			+ "<test-root><variable-file delimiters=\",\" "
    			+ "filename=\"path_du_fichier\" name=\"variable_test\" "
    			+ "offset=\"2\" order=\"1\" policy=\"4\" range=\"4\" "
    			+ "useFirstLine=\"true\" whenOutOfValues=\"CYCLE_VALUES\">"
    			+ "<column name=\"colonneTest\" number=\"0\"/>"
    			+ "</variable-file></test-root>";

    	(new FileVariableWriter(WrittingTestUtils.VARIABLE_TEST3)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTestForColumnsInFile() throws ParserConfigurationException {

		final FileVariable fileVariable = new FileVariable.Builder()
				.name("variable_test")
				.delimiter(";")
				.path("src/test/resources/com/neotys/neoload/model/v3/writers/neoload/filevariable.csv")
				.startFromLine(1)
				.order(Variable.Order.SEQUENTIAL)
				.changePolicy(Variable.ChangePolicy.EACH_USER)
				.isFirstLineColumnNames(true)
				.scope(Variable.Scope.UNIQUE)
				.outOfValue(Variable.OutOfValue.CYCLE)
				.build();

		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><variable-file delimiters=\";\" "
				+ "filename=\"src/test/resources/com/neotys/neoload/model/v3/writers/neoload/filevariable.csv\" name=\"variable_test\" "
				+ "offset=\"1\" order=\"1\" policy=\"4\" range=\"4\" "
				+ "useFirstLine=\"true\" whenOutOfValues=\"CYCLE_VALUES\">"
				+ "<column name=\"myfirstcol\" number=\"0\"/>"
				+ "<column name=\"mysecondcol\" number=\"1\"/>"
				+ "<column name=\"mythirdcol\" number=\"2\"/>"
				+ "</variable-file></test-root>";

		(new FileVariableWriter(fileVariable)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}

	@Test
	public void writeXmlTestForColumnsWithoutFile() throws ParserConfigurationException {

		final FileVariable fileVariable = new FileVariable.Builder()
				.name("variable_test")
				.delimiter(";")
				.path("INVALIDFILE.csv")
				.startFromLine(1)
				.order(Variable.Order.SEQUENTIAL)
				.changePolicy(Variable.ChangePolicy.EACH_USER)
				.isFirstLineColumnNames(true)
				.scope(Variable.Scope.UNIQUE)
				.outOfValue(Variable.OutOfValue.CYCLE)
				.build();

		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><variable-file delimiters=\";\" "
				+ "filename=\"INVALIDFILE.csv\" name=\"variable_test\" "
				+ "offset=\"1\" order=\"1\" policy=\"4\" range=\"4\" "
				+ "useFirstLine=\"true\" whenOutOfValues=\"CYCLE_VALUES\"/>"
				+ "</test-root>";

		(new FileVariableWriter(fileVariable)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		XmlAssert.assertThat(Input.fromDocument(doc)).and(Input.fromString(expectedResult)).areSimilar();
	}


	@Test
	public void getColumnsFromFileTest() throws IOException {
		System.out.println(System.getProperty("user.dir"));
		Assertions.assertThat(FileVariableWriter.getColumnsFromFile("src/test/resources/com/neotys/neoload/model/v3/writers/neoload/filevariable.csv", ";")).containsExactly("myfirstcol", "mysecondcol", "mythirdcol");
	}

	@Test
	public void getColumsFromFirstLineTest() {
		Assertions.assertThat(FileVariableWriter.getColumsFromFirstLine(Optional.empty(), ",")).isEmpty();
		Assertions.assertThat(FileVariableWriter.getColumsFromFirstLine(Optional.of(""), ",")).containsExactly("");
		Assertions.assertThat(FileVariableWriter.getColumsFromFirstLine(Optional.of("test,test2"), ",")).containsExactly("test", "test2");
		Assertions.assertThat(FileVariableWriter.getColumsFromFirstLine(Optional.of("test, test2"), ",")).containsExactly("test", "test2");
		Assertions.assertThat(FileVariableWriter.getColumsFromFirstLine(Optional.of("test,test2"), ";")).containsExactly("test,test2");
		Assertions.assertThat(FileVariableWriter.getColumsFromFirstLine(Optional.of("test.test2"), ".")).containsExactly("test","test2");
	}
}
