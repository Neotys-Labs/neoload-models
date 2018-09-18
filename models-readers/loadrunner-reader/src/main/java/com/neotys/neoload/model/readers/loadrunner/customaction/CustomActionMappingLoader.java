package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.error.YAMLException;
import com.google.common.base.Strings;

public class CustomActionMappingLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomActionMappingLoader.class);
	private static final String CUSTOM_ACTION_MAPPING_YAML = "custom-action-mapping.yaml";
	
	private static final Yaml YAML = new Yaml();
	
	private final Map<String, ImmutableMappingMethod> mapping;
		
	public CustomActionMappingLoader(final String additionalContent) {
		this.mapping = new HashMap<>();
		this.mapping.putAll(parseYaml(getCustomActionMappingFileContent()));
		this.mapping.putAll(parseYaml(additionalContent));
	}
	
	public ImmutableMappingMethod getMethod(final String methodName){
		return mapping.get(methodName);
	}	

	private static String getCustomActionMappingFileContent() {
		final InputStream in = CustomActionMappingLoader.class.getResourceAsStream(CUSTOM_ACTION_MAPPING_YAML);
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(in))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		} catch(final Exception exception){
			LOGGER.error("Error while reading file " + CUSTOM_ACTION_MAPPING_YAML, exception);
			return "";
		}
	}

	private static Map<String, ImmutableMappingMethod> parseYaml(final String content) {
		final Map<String, ImmutableMappingMethod> methodMappings = new HashMap<>();
		if(Strings.isNullOrEmpty(content)){
			return methodMappings;
		}
		try {			
			for(Entry<?,?> entry : ((Map<?,?>)YAML.loadAs(content, Map.class)).entrySet()){
				final Object methodName = entry.getKey();
				final Object methodMappingMap = entry.getValue();
				if(methodName == null || !(methodMappingMap instanceof Map)){
					continue;
				}
				final ImmutableMappingMethod methodMapping = ImmutableMappingMethod.build((Map<?,?>) methodMappingMap);
				if(methodMapping == null){
					continue;
				}
				methodMappings.put(methodName.toString(), methodMapping);				
			}					
		} catch (final YAMLException yamlException) {
			LOGGER.error("Error while parsing file " + CUSTOM_ACTION_MAPPING_YAML, yamlException);			
		}
		return methodMappings;
	}	
}
