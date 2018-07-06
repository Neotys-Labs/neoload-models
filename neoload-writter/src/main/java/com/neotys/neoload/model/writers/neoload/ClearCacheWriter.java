package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.ClearCache;
import com.neotys.neoload.model.repository.ImmutableJavascript;

public class ClearCacheWriter extends JavascriptWriter {
	
	private static final String CONTENT = "// Deletes all cache information for the Virtual User instance.\ncontext.currentVU.clearCache();";

	public ClearCacheWriter(final ClearCache clearCache) {		
		super(ImmutableJavascript.builder().name(clearCache.getName()).content(CONTENT).build());
	}
	
	public static ClearCacheWriter of(final ClearCache clearCache) {
		return new ClearCacheWriter(clearCache);
	}

}
