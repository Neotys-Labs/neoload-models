package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.variable.JavaScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JavaScriptVariableWriter extends VariableWriter {

	private static final String SCRIPTS_FOLDER = "scripts";

	private static final String XML_TAG_NAME = "variable-javascript";

	private static final String XML_ATTR_OFFSET = "offset";
	private static final int DEFAULT_OFFSET_VALUE = 1;

    // element column
	private static final String XML_TAG_COLUMN = "column";
	private static final String XML_COLUMN_ATTR_NAME = "name";
	private static final String DEFAULT_COLUMN_NAME = "col_0";
	private static final String XML_COLUMN_ATTR_NUMBER = "number";
	private static final String DEFAULT_COLUMN_NUMBER = "0";

    // element script
	private static final String XML_TAG_SCRIPT_NAME = "script";
	private static final String XML_ATTR_FILENAME_NAME = "filename";
	private static final String XML_UID_TAG = "uid";
	private static final String XML_ATTR_NAME = "name";
	private static final String XML_ATTR_TS = "ts";

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptVariableWriter.class);

    public JavaScriptVariableWriter(final JavaScriptVariable variable) {
        super(variable);
    }

	public static JavaScriptVariableWriter of(final JavaScriptVariable javaScriptVariable) {
		return new JavaScriptVariableWriter(javaScriptVariable);
	}

	@Override
    public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {

        org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
        super.writeXML(xmlVariable);
		super.writeXML(document, xmlVariable, outputFolder);

        xmlVariable.setAttribute(XML_ATTR_OFFSET, Integer.toString(DEFAULT_OFFSET_VALUE));

        //generate Column node
        final org.w3c.dom.Element xmlColumn = document.createElement(XML_TAG_COLUMN);
        xmlColumn.setAttribute(XML_COLUMN_ATTR_NAME, DEFAULT_COLUMN_NAME);
        xmlColumn.setAttribute(XML_COLUMN_ATTR_NUMBER, DEFAULT_COLUMN_NUMBER);
        xmlVariable.appendChild(xmlColumn);

		//generate Script node
		xmlVariable.appendChild(writeJSElement(document, outputFolder));

		writeDescription(document, xmlVariable);

        currentElement.appendChild(xmlVariable);
    }

    private Element writeJSElement(final Document document, final String outputFolder) {
		final Element xmlJS = document.createElement(XML_TAG_SCRIPT_NAME);

		final String uid = UUID.randomUUID().toString();

		writeJavaScriptFile(new File(outputFolder), uid);

		xmlJS.setAttribute(XML_ATTR_FILENAME_NAME, "scripts/jsVariable_" + uid + ".js");
		xmlJS.setAttribute(XML_ATTR_NAME, element.getName());
		xmlJS.setAttribute(XML_UID_TAG, uid);
		xmlJS.setAttribute(XML_ATTR_TS, Long.toString(System.currentTimeMillis()));

		return xmlJS;
	}

	private void writeJavaScriptFile(final File projectFolder, final String uid) {
		if (projectFolder == null) {
			LOGGER.error("The output folder does not exists.");
			return;
		}
		if (!projectFolder.isDirectory()) {
			LOGGER.error("The output folder is not a directory (" + projectFolder.getAbsolutePath() + ").");
			return;
		}
		final File scriptFolder = new File(projectFolder.getAbsolutePath() + File.separator + SCRIPTS_FOLDER);
		if (!scriptFolder.exists()) {
			scriptFolder.mkdir();
		}
		final File jsFile = new File(scriptFolder.getAbsolutePath() + File.separator + "jsVariable_" + uid + ".js");
		try {
			if (!jsFile.exists() && !jsFile.createNewFile()) {
				LOGGER.error("An error occured while writing the JavaScript file \"" + jsFile.getAbsolutePath() + ".");
				return;
			}
			final String strContent = getJavascriptContent();
			final byte[] content = strContent == null ? new byte[0] : strContent.getBytes();
			Files.write(content, jsFile);
		} catch (IOException e) {
			LOGGER.error("An error occured while writing the JavaScript file \"" + jsFile.getAbsolutePath() + "\":\n" + e);
		}
	}

	protected String getJavascriptContent(){
		return ((JavaScriptVariable) element).getScript();
	}}
