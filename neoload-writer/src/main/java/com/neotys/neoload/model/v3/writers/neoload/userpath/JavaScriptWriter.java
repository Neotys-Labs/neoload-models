package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.userpath.JavaScript;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;

public class JavaScriptWriter extends ElementWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptWriter.class);

	private static final String SCRIPTS_FOLDER = "scripts";
	private static final String XML_TAG_NAME = "js-action";
	private static final String XML_ATTRIBUTE_FILENAME = "filename";
	private static final String XML_ATTRIBUTE_NAME = "name";
	private static final String XML_ATTRIBUTE_TS = "ts";	

	public JavaScriptWriter(com.neotys.neoload.model.v3.project.Element javascript) {
		super(javascript);
	}

	public JavaScriptWriter(JavaScript javascript) {
		super(javascript);
	}

	public static JavaScriptWriter of(final JavaScript javascript) {
		return new JavaScriptWriter(javascript);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element xmlJS = document.createElement(XML_TAG_NAME);
		super.writeXML(document, xmlJS, outputFolder);
		final String uid = xmlJS.getAttribute(XML_UID_TAG);
		writeJavaScriptFile(new File(outputFolder), uid);
		xmlJS.setAttribute(XML_ATTRIBUTE_FILENAME, "scripts/jsAction_" + uid + ".js");
		xmlJS.setAttribute(XML_ATTRIBUTE_NAME, element.getName());
		xmlJS.setAttribute(XML_ATTRIBUTE_TS, Long.toString(System.currentTimeMillis()));		
		currentElement.appendChild(xmlJS);
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
		final File jsFile = new File(scriptFolder.getAbsolutePath() + File.separator + "jsAction_" + uid + ".js");
		try {
			if (!jsFile.exists() && !jsFile.createNewFile()) {
				LOGGER.error("An error occured while writing the Javascript file \"" + jsFile.getAbsolutePath() + ".");
				return;				
			}
			final String strContent = getJavaScriptContent();
			final byte[] content = strContent == null ? new byte[0] : strContent.getBytes();
			Files.write(content, jsFile);
		} catch (IOException e) {
			LOGGER.error("An error occured while writing the Javascript file \"" + jsFile.getAbsolutePath() + "\":\n" + e);
		}
	}
	
	protected String getJavaScriptContent(){
		return ((JavaScript) element).getScript();
	}
}
