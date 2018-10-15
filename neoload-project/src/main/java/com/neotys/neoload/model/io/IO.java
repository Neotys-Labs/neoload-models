package com.neotys.neoload.model.io;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.MINIMIZE_QUOTES;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.USE_NATIVE_TYPE_ID;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Strings;


public final class IO {
	private static final Map<String, String> PROPERTIES_DICTIONARY;
	static {
		PROPERTIES_DICTIONARY = new HashMap<>();
		PROPERTIES_DICTIONARY.put("workgroupName", "workgroup");
	}

	private static final String YAML_STARTS_WITH = "---";
	private static final String JSON_STARTS_WITH = "{";
	private static final String JSON_ENDS_WITH = "}";
	
	private static final Format DEFAULT_FORMAT = Format.YAML;
	
	public enum Format {
		YAML,
		JSON
	}
	
	private ObjectMapper yamlMapper = null;
	private ObjectMapper jsonMapper = null;
	
	public IO() {
		super();
	}
	
	public <T> T read(final File file, final Class<T> type) throws IOException {
		return read(new String(Files.readAllBytes(Paths.get(file.toURI()))), getFormat(file), type);
	}
	
	public <T> T read(final String content, final Class<T> type) throws IOException {
		return read(content, getFormat(content), type);
	}
	
	private <T> T read(final String content, final Format format, final Class<T> type) throws IOException {
		// Gets the mapper from the format
		final ObjectMapper mapper = getMapper(format);
		// Deserialize
		return mapper.readValue(content, type);
	}

	public <T> void write(final File file, final T object) throws IOException {
		// Retrieve the format (Yaml or Json) of the file
		final Format format = getFormat(file);
		// Convert
		final String content = write(object, format);
		// Write the file
		Files.write(Paths.get(file.toURI()), content.getBytes(), StandardOpenOption.CREATE);
	}
	
	public <T> String write(final T object, final Format format) throws IOException {
		// Gets the mapper from the format
		final ObjectMapper mapper = getMapper(format);
		// Serialize 
		return mapper.writeValueAsString(object);
	}

	protected Format getFormat(final File file) {
		// Gets the extension from the specified file
		final String extension = FilenameUtils.getExtension(file.getAbsolutePath());
		if (Strings.isNullOrEmpty(extension)) {
			throw new IllegalArgumentException("The extension of the file must be 'yaml' or 'json'");
		}
		// Convert extension to format
		return Format.valueOf(extension.toUpperCase());
	}

	protected Format getFormat(final String content) {
		if ((content == null) || (content.isEmpty())) {
			return null;
		}
		
		final String tmp = content.trim();
		if (tmp.startsWith(YAML_STARTS_WITH)) {
			return Format.YAML;
		}
		if ((tmp.startsWith(JSON_STARTS_WITH)) && (tmp.endsWith(JSON_ENDS_WITH))) {
			return Format.JSON;
		}
		return DEFAULT_FORMAT;
	}

	protected synchronized ObjectMapper getMapper(final Format format) {
		switch (format) {
			case YAML:
				if (yamlMapper == null) {
					yamlMapper = newYamlMapper();
				}
				return yamlMapper;
			case JSON:
				if (jsonMapper == null) {
					jsonMapper = newJsonMapper();
				}
				return jsonMapper;
			default:
				throw new IllegalArgumentException("The format " + format +" is unknown");
		}
	}
	
	private static ObjectMapper newYamlMapper() {
		// Configures Yaml Factory
		final YAMLFactory yamlFactory = new YAMLFactory();
		yamlFactory.configure(WRITE_DOC_START_MARKER, false);
		yamlFactory.configure(USE_NATIVE_TYPE_ID, false);
		yamlFactory.configure(MINIMIZE_QUOTES, true);
		// Constructs Yaml Mapper 
		final ObjectMapper yamlMapper = new YAMLMapper(yamlFactory);
		// Register modules
		registerModules(yamlMapper);
		
		return yamlMapper;
	}
	
	private static ObjectMapper newJsonMapper() {
		// Constructs Object Mapper 
		final ObjectMapper objectMapper =  new ObjectMapper();
		// Register modules
		registerModules(objectMapper);
		
		return objectMapper;
	}
	
	private static void registerModules(final ObjectMapper objectMapper) {
		objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new Jdk8Module());
	}
}
