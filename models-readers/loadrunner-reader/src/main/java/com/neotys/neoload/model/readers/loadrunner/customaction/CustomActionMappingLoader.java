package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	private static final Supplier<Map<String, ImmutableMappingMethod>> MAPPING = Suppliers.memoize(CustomActionMappingLoader::getCustomActionMapping);

	private CustomActionMappingLoader() {
	}

	public static Map<String, ImmutableMappingMethod> getMapping() {
		return MAPPING.get();
	}
	
	private static Map<String, ImmutableMappingMethod> getCustomActionMapping(){
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

	private static Path getCustomActionMappingFile() {
		final Path customActionMappingFile = Paths.get(CUSTOM_ACTION_MAPPING_YAML);
		if (customActionMappingFile.toFile().exists()) {
			return customActionMappingFile;
		}
		return new File(CustomActionMappingLoader.class.getResource(CUSTOM_ACTION_MAPPING_YAML).getFile()).toPath();
	}

	private static Map<String, ImmutableMappingMethod> parseYaml(final String content) {
		final Map<String, ImmutableMappingMethod> methodMappings = new HashMap<>();
		try {			
			for(Entry<?,?> entry : ((Map<?,?>)YAML.loadAs(content, Map.class)).entrySet()){
				final Object methodName = entry.getKey();
				final Object methodMappingMap = entry.getValue();
				if(methodName == null || !(methodMappingMap instanceof Map)){
					LOGGER.error("Error while parsing file " + CUSTOM_ACTION_MAPPING_YAML);
					return Maps.newHashMap();
				}
				final ImmutableMappingMethod methodMapping = ImmutableMappingMethod.build((Map<?,?>) methodMappingMap);
				if(methodMapping == null){
					return Maps.newHashMap();
				}
				methodMappings.put(methodName.toString(), methodMapping);				
			}		
			return methodMappings;
		} catch (final YAMLException yamlException) {
			LOGGER.error("Error while parsing file " + CUSTOM_ACTION_MAPPING_YAML, yamlException);
			return Maps.newHashMap();
		}		
	}	
}
