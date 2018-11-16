package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.FileVariable;
import com.neotys.neoload.model.repository.ImmutableFileVariable;
import org.apache.log4j.lf5.util.ResourceUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FileVariableWriterTest {
	
	@Test
	public void writeXmlTest1() throws ParserConfigurationException, TransformerException {
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
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
	}
	
	
	@Test
	public void writeXmlTest2() throws ParserConfigurationException, TransformerException {
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
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
	}
	
	@Test
	public void writeXmlTest3() throws ParserConfigurationException, TransformerException {
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
    	
    	String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
	}

	@Test
	public void writeXmlTestForColumnsInFile() throws ParserConfigurationException, TransformerException {

		final FileVariable fileVariable = ImmutableFileVariable.builder()
				.name("variable_test")
				.columnsDelimiter(";")
				.fileName("src/test/resources/com/neotys/neoload/model/writers/neoload/filevariable.csv")
				.numOfFirstRowData(1)
				.order(FileVariable.VariableOrder.SEQUENTIAL)
				.policy(FileVariable.VariablePolicy.EACH_VUSER)
				.firstLineIsColumnName(true)
				.scope(FileVariable.VariableScope.UNIQUE)
				.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.CYCLE)
				.build();

		Document doc = WrittingTestUtils.generateEmptyDocument();
		Element root = WrittingTestUtils.generateTestRootElement(doc);
		String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<test-root><variable-file delimiters=\";\" "
				+ "filename=\"src/test/resources/com/neotys/neoload/model/writers/neoload/filevariable.csv\" name=\"variable_test\" "
				+ "offset=\"1\" order=\"1\" policy=\"4\" range=\"4\" "
				+ "useFirstLine=\"true\" whenOutOfValues=\"CYCLE_VALUES\">"
				+ "<column name=\"myfirstcol\" number=\"0\"/>"
				+ "<column name=\"mysecondcol\" number=\"1\"/>"
				+ "<column name=\"mythirdcol\" number=\"2\"/>"
				+ "</variable-file></test-root>";

		(new FileVariableWriter(fileVariable)).writeXML(doc, root, Files.createTempDir().getAbsolutePath());

		String generatedResult = WrittingTestUtils.getXmlString(doc);
		assertEquals(expectedResult, generatedResult);
	}

	@Test
	public void writeXmlTestForColumnsWithoutFile() throws ParserConfigurationException, TransformerException {

		final FileVariable fileVariable = ImmutableFileVariable.builder()
				.name("variable_test")
				.columnsDelimiter(";")
				.fileName("INVALIDFILE.csv")
				.numOfFirstRowData(1)
				.order(FileVariable.VariableOrder.SEQUENTIAL)
				.policy(FileVariable.VariablePolicy.EACH_VUSER)
				.firstLineIsColumnName(true)
				.scope(FileVariable.VariableScope.UNIQUE)
				.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.CYCLE)
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

		String generatedResult = WrittingTestUtils.getXmlString(doc);
		assertEquals(expectedResult, generatedResult);
	}
	
	@Test
	public void dumpDataInFileTest1() {
		
		List<String> columns = new ArrayList<>();
		columns.add("col_name_1");
		columns.add("col_name_2");
		String[][] theData = {
				{"val 1", "Val 2"},
				{"val 3", "Val 4"}
		};
		
		File tmpDir = Files.createTempDir();
		String fileName = FileVariableWriter.dumpDataInFile(tmpDir, "variable_name", columns, ",", theData);
		
        assertTrue(new File(tmpDir,fileName).exists());
		
	}

	@Test
	public void getColumnsFromFileTest() throws IOException {
		System.out.println(System.getProperty("user.dir"));
		Assertions.assertThat(FileVariableWriter.getColumnsFromFile("src/test/resources/com/neotys/neoload/model/writers/neoload/filevariable.csv", ";")).containsExactly("myfirstcol", "mysecondcol", "mythirdcol");
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
	
	@Test
	public void dumpDataInFileTest2() {
		
		List<String> columns = new ArrayList<>();
		columns.add("col_name_1");
		columns.add("col_name_2");
		String[][] theData = {
				{"val 1", "Val 2"},
				{"val 3", "Val 4"}
		};
		String fileName = FileVariableWriter.dumpDataInFile(null, "variable_name", columns, ",", theData);
		assertTrue(fileName == null);
	}

	@Test
	public void dumpDataInFileTest3() throws IOException {

		List<String> columns = new ArrayList<>();
		columns.add("col_name_1");
		columns.add("col_name_2");
		String[][] theData = {
				{"val 1", "Val 2"},
				{"val 3", "Val 4"}
		};

		File tmpFile = File.createTempFile("pre", "suf");
		String fileName = FileVariableWriter.dumpDataInFile(tmpFile, "variable_name", columns, ",", theData);
		assertTrue(fileName == null);
	}
}
