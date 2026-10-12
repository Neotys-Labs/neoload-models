package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ProjectSettingsWriter {
		
	private static final String PROJECT_SETTINGS_DEFAULT_FILE = "defaultProjectSettings.properties";	
	private static final String SETTINGS_XML_FILE = NeoLoadWriter.ConfigFiles.SETTINGS.getFileName();
	private static final String GLOBAL_ASSERTIONS_CONTENT_TYPES = "global_assertions.content_types";
	// Derived token: the comma-separated content types expanded into one <contentType> element each
	private static final String GLOBAL_ASSERTIONS_CONTENT_TYPES_ELEMENTS = "global_assertions.content_types.elements";

	private ProjectSettingsWriter() {
	}

	public static void writeSettingsXML(final String nlProjectFolder, final Map<String,String> overwrittenProjectSettings) throws IOException{
		final Map<String,String> allProjectSettings = readProjectSettingsDefault();
		allProjectSettings.putAll(overwrittenProjectSettings);
		allProjectSettings.put(GLOBAL_ASSERTIONS_CONTENT_TYPES_ELEMENTS, toContentTypeElements(allProjectSettings.get(GLOBAL_ASSERTIONS_CONTENT_TYPES)));
	    final Reader source = new InputStreamReader(ProjectSettingsWriter.class.getResourceAsStream(SETTINGS_XML_FILE));
	    try(final Reader reader = new TokenReplacingReader(source, allProjectSettings)){
		    int intValueOfChar;
	        final StringBuilder buffer = new StringBuilder();
	        while ((intValueOfChar = reader.read()) != -1) {
	            buffer.append((char) intValueOfChar);
	        }	      
	        writeSettingsXML(nlProjectFolder, buffer.toString());	        
	    }
	}

	public static void writeSettingsXML(final String nlProjectFolder, final String content) throws IOException {
		final File targetFile = new File(nlProjectFolder,SETTINGS_XML_FILE);
        if(!targetFile.createNewFile()) {
        	throw new IOException("Cannot create "+ NeoLoadWriter.ConfigFiles.SETTINGS.getFileName()+" file");
        } 
		try (Writer targetFileWriter = new FileWriter(targetFile)) {
			targetFileWriter.write(content);
		}		
	}


	private static Map<String,String> readProjectSettingsDefault() throws IOException {
		final Properties properties = new Properties();
		try (final InputStream in = ProjectSettingsWriter.class.getResourceAsStream(PROJECT_SETTINGS_DEFAULT_FILE)){
			properties.load(in);
		} 
		return properties.entrySet().stream().collect(
			    Collectors.toMap(
			            e -> e.getKey().toString(),
			            e -> e.getValue().toString()
			       )
			   );
	}

	private static String toContentTypeElements(final String contentTypes) {
		if (contentTypes == null) {
			return "";
		}
		return Arrays.stream(contentTypes.split(","))
				.map(String::trim)
				.filter(contentType -> !contentType.isEmpty())
				.map(contentType -> "<contentType value=\"" + contentType + "\"/>\n")
				.collect(Collectors.joining());
	}
}
