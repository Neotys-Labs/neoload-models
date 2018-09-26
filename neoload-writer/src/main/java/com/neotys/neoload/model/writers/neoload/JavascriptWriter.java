package com.neotys.neoload.model.writers.neoload;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.Javascript;

public class JavascriptWriter extends ElementWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptWriter.class);

	private static final String SCRIPTS_FOLDER = "scripts";
	private static final String XML_TAG_NAME = "js-action";
	private static final String XML_ATTRIBUTE_FILENAME = "filename";
	private static final String XML_ATTRIBUTE_NAME = "name";
	private static final String XML_ATTRIBUTE_TS = "ts";	

	public JavascriptWriter(com.neotys.neoload.model.core.Element javascript) {
		super(javascript);
	}

	public JavascriptWriter(com.neotys.neoload.model.repository.Javascript javascript) {
		super(javascript);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element xmlJS = document.createElement(XML_TAG_NAME);
		super.writeXML(document, xmlJS, outputFolder);
		final String uid = xmlJS.getAttribute(XML_UID_TAG);
		writeJavascriptFile(new File(outputFolder), uid);
		xmlJS.setAttribute(XML_ATTRIBUTE_FILENAME, "scripts/jsAction_" + uid + ".js");
		xmlJS.setAttribute(XML_ATTRIBUTE_NAME, element.getName());
		xmlJS.setAttribute(XML_ATTRIBUTE_TS, Long.toString(System.currentTimeMillis()));		
		currentElement.appendChild(xmlJS);
	}

	private void writeJavascriptFile(final File projectFolder, final String uid) {
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
			final String strContent = getJavascriptContent();
			final byte[] content = strContent == null ? new byte[0] : strContent.getBytes();
			Files.write(content, jsFile);
		} catch (IOException e) {
			LOGGER.error("An error occured while writing the Javascript file \"" + jsFile.getAbsolutePath() + "\":\n" + e);
			return;
		}
	}
	
	protected String getJavascriptContent(){
		return ((Javascript) element).getContent();
	}
}
