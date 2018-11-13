package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.neotys.neoload.model.repository.FileVariable;
import com.opencsv.CSVWriter;

public class FileVariableWriter	extends VariableWriter {

	public static final String VARIABLE_DIRECTORY = "variables"; 
	
	public static final String DATA_FILE_BASE_NAME = "Variable";
	public static final String DATA_FILE_BASE_EXT = ".csv";
	
	public static final String XML_TAG_NAME = "variable-file";
    public static final String XML_ATTR_FILENAME = "filename";
    public static final String XML_ATTR_OFFSET = "offset";

    public static final String XML_ATTR_USE_FIRST_LINE = "useFirstLine";
    public static final String XML_ATTR_DELIMITER = "delimiters";
    
    public static final String XML_TAG_COLOMN = "column";
    public static final String XML_COLOMN_ATTR_NAME = "name";
    public static final String XML_COLOMN_ATTR_NUMBER = "number";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileVariableWriter.class);
    
	public FileVariableWriter(FileVariable variable) {
		super(variable);
	}
	
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;

		FileVariable theFileVariable = (FileVariable) variable;
		xmlVariable.setAttribute(XML_ATTR_DELIMITER, theFileVariable.getColumnsDelimiter());
		xmlVariable.setAttribute(XML_ATTR_USE_FIRST_LINE, Boolean.toString(theFileVariable.getFirstLineIsColumnName()));
		xmlVariable.setAttribute(XML_ATTR_OFFSET, Integer.toString(theFileVariable.getNumOfFirstRowData()));
		
		xmlVariable.setAttribute(XML_ATTR_FILENAME,
				// Here we don't use the "orElse" method because if the value is not present we don't want
				// the execution of the method "dumpDataInFile" 
				theFileVariable.getFileName().isPresent() ? theFileVariable.getFileName().get() :
						dumpDataInFile(new File(outputFolder),
								theFileVariable.getName(),
								theFileVariable.getColumnsNames(),
								theFileVariable.getColumnsDelimiter(),
								theFileVariable.getData().orElseThrow(IllegalStateException::new)
						)
		);
		
		//generate Column nodes
		int counter = 0;
		for(String columnName : theFileVariable.getColumnsNames()) {
			org.w3c.dom.Element xmlColumn = document.createElement(XML_TAG_COLOMN);
			xmlColumn.setAttribute(XML_COLOMN_ATTR_NAME, columnName);
			xmlColumn.setAttribute(XML_COLOMN_ATTR_NUMBER, Integer.toString(counter++));
			xmlVariable.appendChild(xmlColumn);
		}
		
		currentElement.appendChild(xmlVariable);
	}
	
	
	//generate the file
	static String dumpDataInFile(File folder, String variableName, List<String> columnsNames, String delimiter, String [][] data) {
		if(folder == null || !folder.isDirectory()) {
			if (folder == null) {
				LOGGER.error("the output folder does not exists");
			} else {
				LOGGER.error("the folder \"" + folder.getAbsolutePath() + "\" does not exists");
			}
			return null;
		}
		
		boolean notFound = true;
		int numFile = 0;
		File dataFile = new File(folder.getAbsolutePath() + File.separator + VARIABLE_DIRECTORY);
		if (! dataFile.exists()) {
			dataFile.mkdir();
		}
		
		//find file path
		while (notFound) {
			numFile++;
			dataFile = new File(folder.getAbsolutePath() +
					File.separator + VARIABLE_DIRECTORY +
					File.separator + DATA_FILE_BASE_NAME +
					"_" + variableName +
					Integer.toString(numFile) + DATA_FILE_BASE_EXT);
			if(!dataFile.exists()) {
				notFound = false;
			}
		}
		
		try(Writer writer = Files.newBufferedWriter(Paths.get(dataFile.getPath()));
				CSVWriter csvWriter = new CSVWriter(writer,
				delimiter.charAt(0),
				CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER,
				CSVWriter.DEFAULT_LINE_END);
	        ) {
			
			String[] headerRecord = new String[columnsNames.size()];
			columnsNames.toArray(headerRecord);
			
			csvWriter.writeNext(headerRecord);
			Arrays.asList(data).stream().forEach(csvWriter::writeNext);
			
		} catch (IOException e) {
			LOGGER.error("An error occured while writing the parameter File \"" + dataFile.getAbsolutePath() + "\":\n" + e);
		}
		
		return VARIABLE_DIRECTORY + File.separator + dataFile.getName();
	}
	
}
