package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.neotys.neoload.model.readers.loadrunner.customaction.CustomActionMappingLoader;
import com.neotys.neoload.model.readers.loadrunner.customaction.ImmutableMappingMethod;

public class LoadRunnerSupportedMethods {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadRunnerSupportedMethods.class);
	private final CustomActionMappingLoader customActionMappingLoader;
	private final Map<String, LoadRunnerMethod> supportedMethods;
	
	public LoadRunnerSupportedMethods(final String additionalCustomActionMappingContent) {
		super();
		this.customActionMappingLoader = new CustomActionMappingLoader(additionalCustomActionMappingContent);
		this.supportedMethods = new HashMap<>();
	}
	
	public LoadRunnerMethod get(final String methodName) {
		LoadRunnerMethod method = supportedMethods.get(methodName);
		if(supportedMethods.containsKey(method)){
			return method;
		}
		try{
			method = internalLoadMethod(methodName);
		} catch(final Exception exception){
			LOGGER.error("Error while loading method " + methodName, exception);
		}		
		supportedMethods.put(methodName, method);
		return method;
	}
	
	public ImmutableMappingMethod getCustomActionMappingMethod(String methodName) {
		return customActionMappingLoader.getMethod(methodName);
	}
	
	private final LoadRunnerMethod internalLoadMethod(final String methodName) {
		if (methodName == null) {
			throw new InvalidCacheLoadException("Method name is null");
		}										
		if (customActionMappingLoader.getMethod(methodName)!=null) {
			return new CustomActionMethod();
		}
		final String lowerCaseMethodName = methodName.replaceAll("_", "").toLowerCase();
		if (lowerCaseMethodName.length() < 2) {
			throw new InvalidCacheLoadException("Method name has less than 2 characters");
		}
		final String fullQualifiedMethodName = "com.neotys.neoload.model.readers.loadrunner.method."
				+ lowerCaseMethodName.substring(0, 1).toUpperCase() + lowerCaseMethodName.substring(1) + "Method";
		try {
			return (LoadRunnerMethod) Class.forName(fullQualifiedMethodName).getConstructor().newInstance();
		} catch (Exception e) {
		}
		if("lr_start_sub_transaction".equals(methodName)) return new LrstarttransactionMethod();
		if("lr_end_sub_transaction".equals(methodName)) return new LrendtransactionMethod();
		if("lr_param_sprintf".equals(methodName)) return new SprintfMethod();
		throw new InvalidCacheLoadException("Method not supported");
	}	
}
