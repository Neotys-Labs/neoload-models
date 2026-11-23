package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.DynatraceSettings;
import com.neotys.neoload.model.v3.project.settings.ProjectSettings;
import com.neotys.neoload.model.v3.project.settings.QTestSettings;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class ProjectSettingsWriter {
		
	private static final String PROJECT_SETTINGS_DEFAULT_FILE = "defaultProjectSettings.properties";	
	private static final String SETTINGS_XML_FILE = NeoLoadWriter.ConfigFiles.SETTINGS.getFileName();

	private ProjectSettingsWriter() {
	}

	public static void writeSettingsXML(final String nlProjectFolder, final Optional<ProjectSettings> projectSettings) throws IOException {
		writeSettingsXML(nlProjectFolder, toTokens(projectSettings));
	}

	/**
	 * The typed settings as the {@code group.setting} tokens of the settings.xml template, such as {@code qtest.enabled}.
	 */
	static Map<String, String> toTokens(final Optional<ProjectSettings> projectSettings) {
		final Map<String, String> tokens = new LinkedHashMap<>();
		projectSettings.flatMap(ProjectSettings::getDynatrace).ifPresent(dynatrace -> {
			final String group = ProjectSettings.DYNATRACE + ".";
			putToken(tokens, group + DynatraceSettings.ENABLED, dynatrace.getEnabled());
			putToken(tokens, group + DynatraceSettings.URL, dynatrace.getUrl());
			putToken(tokens, group + DynatraceSettings.TOKEN, dynatrace.getToken());
		});
		projectSettings.flatMap(ProjectSettings::getQtest).ifPresent(qtest -> {
			final String group = ProjectSettings.QTEST + ".";
			putToken(tokens, group + QTestSettings.ENABLED, qtest.getEnabled());
			putToken(tokens, group + QTestSettings.URL, qtest.getUrl());
			putToken(tokens, group + QTestSettings.TOKEN, qtest.getToken());
			putToken(tokens, group + QTestSettings.USERNAME, qtest.getUsername());
			putToken(tokens, group + QTestSettings.PASSWORD, qtest.getPassword());
			putToken(tokens, group + QTestSettings.AUTH_METHOD, qtest.getAuthMethod());
			putToken(tokens, group + QTestSettings.PROJECT_ID, qtest.getProjectId());
			putToken(tokens, group + QTestSettings.CYCLE_ID, qtest.getCycleId());
		});
		return tokens;
	}

	private static void putToken(final Map<String, String> tokens, final String token, final Optional<?> value) {
		value.ifPresent(v -> tokens.put(token, v instanceof Enum ? ((Enum<?>) v).name() : v.toString()));
	}

	public static void writeSettingsXML(final String nlProjectFolder, final Map<String,String> overwrittenProjectSettings) throws IOException{
		final Map<String,String> allProjectSettings = readProjectSettingsDefault();
		allProjectSettings.putAll(overwrittenProjectSettings);	    
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
}
