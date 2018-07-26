package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.ClearCache;

public class ClearCacheWriter extends JavascriptWriter {
	
	private static final String CONTENT = "// Deletes all cache information for the Virtual User instance.\ncontext.currentVU.clearCache();";

	public ClearCacheWriter(final ClearCache clearCache) {		
		super(clearCache);
	}
	
	@Override
	protected String getJavascriptContent() {
		return CONTENT;
	}
	
	public static ClearCacheWriter of(final ClearCache clearCache) {
		return new ClearCacheWriter(clearCache);
	}

}
