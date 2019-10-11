package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.util.RegExpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileVariableWriter extends VariableWriter {

	private static final String VARIABLE_DIRECTORY = "variables";

	private static final String XML_TAG_NAME = "variable-file";
	private static final String XML_ATTR_FILENAME = "filename";
	private static final String XML_ATTR_OFFSET = "offset";

	private static final String XML_ATTR_USE_FIRST_LINE = "useFirstLine";
	private static final String XML_ATTR_DELIMITER = "delimiters";

	private static final String XML_TAG_COLOMN = "column";
	private static final String XML_COLOMN_ATTR_NAME = "name";
	private static final String XML_COLOMN_ATTR_NUMBER = "number";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileVariableWriter.class);
    
	public FileVariableWriter(FileVariable variable) {
		super(variable);
	}
	
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;

		FileVariable fileVariable = (FileVariable) element;
		xmlVariable.setAttribute(XML_ATTR_DELIMITER, fileVariable.getDelimiter());
		xmlVariable.setAttribute(XML_ATTR_USE_FIRST_LINE, Boolean.toString(fileVariable.isFirstLineColumnNames()));
		xmlVariable.setAttribute(XML_ATTR_OFFSET, Integer.toString(fileVariable.getStartFromLine()));

		//generate Column nodes
		List<String> cols = fileVariable.getColumnNames();
		if(cols.isEmpty() && fileVariable.isFirstLineColumnNames()) {
			// read the first line and get the columns names
			try {
				cols = getColumnsFromFile(fileVariable.getPath(), fileVariable.getDelimiter());
			}catch(IOException e) {
				LOGGER.warn("Cannot read columns in file variable "+fileVariable.getPath(), e);
			}
		}
		int counter = 0;
		for(String columnName : cols) {
			org.w3c.dom.Element xmlColumn = document.createElement(XML_TAG_COLOMN);
			xmlColumn.setAttribute(XML_COLOMN_ATTR_NAME, columnName);
			xmlColumn.setAttribute(XML_COLOMN_ATTR_NUMBER, Integer.toString(counter++));
			xmlVariable.appendChild(xmlColumn);
		}
		writeDescription(document, xmlVariable);

		// copy file
		final Path newFilename = writeFile(fileVariable);

		xmlVariable.setAttribute(XML_ATTR_FILENAME, (newFilename != null) ? newFilename.toString() : fileVariable.getPath());

		currentElement.appendChild(xmlVariable);
	}

	private Path writeFile(final FileVariable fileVariable) {
		final Path path = Paths.get(fileVariable.getPath());
		final Path filename = path.getFileName();
		Path destination = null;
		try {
			if (filename != null) {
				destination = Paths.get(VARIABLE_DIRECTORY + File.separator + filename);
				if (!destination.equals(path)) { // if source and destination are the same, the copy is useless
					Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error occurred when copying file " + fileVariable.getPath() + " to " + VARIABLE_DIRECTORY, e);
			// copy failed, we still keep the filename of the source in the attribute of the xml variable
			destination = null;
		}

		return destination;
	}

	static List<String> getColumnsFromFile(String fileName, String columnsDelimiter) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			return getColumsFromFirstLine(stream.findFirst(), columnsDelimiter);
		}
	}

	static List<String> getColumsFromFirstLine(Optional<String> firstLine, String columnsDelimiter) {
        return firstLine.map(s -> Arrays.stream(s.split(RegExpUtils.escape(columnsDelimiter))).map(String::trim).collect(Collectors.toList())).orElseGet(ImmutableList::of);
    }
}
