package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.neotys.neoload.model.readers.loadrunner.customaction.CustomActionMappingLoader;

public class LoadRunnerSupportedMethods {

	private static final LoadingCache<String, LoadRunnerMethod> SUPPORTED_METHODS = CacheBuilder.newBuilder().build(
			CacheLoader.from(LoadRunnerSupportedMethods::internalLoadMethod));

	private LoadRunnerSupportedMethods() {
		super();
	}

	public static LoadRunnerMethod get(final String methodName) {
		try {
			return SUPPORTED_METHODS.get(methodName);
		} catch (final Exception e) {
			return null;
		}
	}
	
	private static final LoadRunnerMethod internalLoadMethod(final String methodName) throws InvalidCacheLoadException {
		if (methodName == null) {
			throw new InvalidCacheLoadException("Method name is null");
		}										
		if (CustomActionMappingLoader.getMapping().get(methodName)!=null) {
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
