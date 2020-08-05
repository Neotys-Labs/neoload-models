package com.neotys.neoload.model.v3.binding.io;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.MINIMIZE_QUOTES;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.USE_NATIVE_TYPE_ID;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Strings;


public final class IO {
	private static final Set<String> YAML_EXTENSIONS;
	static {
		YAML_EXTENSIONS = new HashSet<>();
		YAML_EXTENSIONS.add("yaml");
		YAML_EXTENSIONS.add("yml");
	}
	private static final Set<String> JSON_EXTENSIONS;
	static {
		JSON_EXTENSIONS = new HashSet<>();
		JSON_EXTENSIONS.add("json");
	}
	
	private static final String YAML_STARTS_WITH = "---";
	private static final String JSON_STARTS_WITH = "{";
	private static final String JSON_ENDS_WITH = "}";
	
	private static final Format DEFAULT_FORMAT = Format.YAML;
	
	public enum Format {
		YAML,
		JSON;
	}
	
	private ObjectMapper yamlMapper = null;
	private ObjectMapper jsonMapper = null;
	
	public IO() {
		super();
	}
	
	public ProjectDescriptor read(final File file) throws IOException {
		return read(new String(Files.readAllBytes(Paths.get(file.toURI()))), getFormat(file), ProjectDescriptor.class);
	}
	
	public ProjectDescriptor read(final String content) throws IOException {
		return read(content, getFormat(content), ProjectDescriptor.class);
	}
	
	private <T> T read(final String content, final Format format, final Class<T> type) throws IOException {
		// Gets the mapper from the format
		final ObjectMapper mapper = getMapper(format);
		// Deserialize
		return mapper.readValue(content, type);
	}

	public String write(final Object value, final Format format) throws IOException {
		// Gets the mapper from the format
		final ObjectMapper mapper = getMapper(format);
		// Deserialize
		return mapper.writeValueAsString(value);
	}

	protected Format getFormat(final File file) {
		if (file == null) return null;
		
		// Gets the extension from the specified file
		final String extension = FilenameUtils.getExtension(file.getAbsolutePath());
		if (Strings.isNullOrEmpty(extension)) {
			throw new IllegalArgumentException("The extension of the file must be 'yaml', 'yml' or 'json'");
		}
		
		// Convert extension to format
		if (YAML_EXTENSIONS.contains(extension.toLowerCase())) {
			return Format.YAML;
		}
		if (JSON_EXTENSIONS.contains(extension.toLowerCase())) {
			return Format.JSON;
		}
		
		// Format is unknown
		throw new IllegalArgumentException("The extension of the file must be 'yaml', 'yml' or 'json'");
	}

	protected Format getFormat(final String content) {
		if (Strings.isNullOrEmpty(content)) return null;
		
		final String tmp = content.trim();
		if (tmp.isEmpty()) return null;
		
		if (tmp.startsWith(YAML_STARTS_WITH)) {
			return Format.YAML;
		}
		if ((tmp.startsWith(JSON_STARTS_WITH)) && (tmp.endsWith(JSON_ENDS_WITH))) {
			return Format.JSON;
		}
		return DEFAULT_FORMAT;
	}

	protected synchronized ObjectMapper getMapper(final Format format) {
		if (format == null) {
			throw new IllegalArgumentException("The format is unknown");
		}
		
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
		final Jdk8Module jdk8Module = new Jdk8Module();
		jdk8Module.configureAbsentsAsNulls(true);
        objectMapper.registerModule(jdk8Module);
	}
}
