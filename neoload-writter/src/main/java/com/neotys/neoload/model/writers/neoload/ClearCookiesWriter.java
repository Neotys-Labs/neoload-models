package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.ClearCookies;
import com.neotys.neoload.model.repository.ImmutableJavascript;

public class ClearCookiesWriter extends JavascriptWriter {

	private static final String CONTENT = "// Deletes all cookies for the Virtual User instance.\ncontext.currentVU.clearCookies();";

	public ClearCookiesWriter(final ClearCookies clearCookies) {		
		super(ImmutableJavascript.builder().name(clearCookies.getName()).content(CONTENT).build());
	}
	
	public static ClearCookiesWriter of(final ClearCookies clearCookies) {
		return new ClearCookiesWriter(clearCookies);
	}
}
