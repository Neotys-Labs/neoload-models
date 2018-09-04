package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.error.YAMLException;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;

public class CustomActionMappingLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomActionMappingLoader.class);
	private static final String CUSTOM_ACTION_MAPPING_YAML = "custom-action-mapping.yaml";

	private static final Yaml YAML = new Yaml();
	
	private static final Supplier<Map<String, Map<String, Object>>> MAPPING = Suppliers.memoize(CustomActionMappingLoader::getCustomActionMapping);

	private CustomActionMappingLoader() {
	}

	public static Map<String, Map<String, Object>> getMapping() {
		return MAPPING.get();
	}

	private static Path getCustomActionMappingFile() {
		final Path customActionMappingFile = Paths.get(CUSTOM_ACTION_MAPPING_YAML);
		if (customActionMappingFile.toFile().exists()) {
			return customActionMappingFile;
		}
		return new File(CustomActionMappingLoader.class.getResource(CUSTOM_ACTION_MAPPING_YAML).getFile()).toPath();
	}

	private static Map<String, Map<String, Object>> parseYaml(final String content) {
		try {
			return YAML.loadAs(content, Map.class);
		} catch (final YAMLException yamlException) {
			LOGGER.error("Error while parsing file " + CUSTOM_ACTION_MAPPING_YAML, yamlException);
			return Maps.newHashMap();
		}
	}
	
	private static Map<String, Map<String, Object>> getCustomActionMapping(){
		final Path customActionMappingFile = getCustomActionMappingFile();
		final String content;
		try {
			content = new String(Files.readAllBytes(customActionMappingFile));
		} catch (final IOException ioException) {
			LOGGER.error("Error while reading file " + CUSTOM_ACTION_MAPPING_YAML, ioException);
			return Maps.newHashMap();
		}
		return parseYaml(content);
	}
}
