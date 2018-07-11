package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.AddCookie;
import com.neotys.neoload.model.repository.ImmutableJavascript;

public class AddCookieWriter extends JavascriptWriter {

	private static final String CONTENT_FIRST_PART = "// Add cookie to a HTTP server.\ncontext.currentVU.setCookieForServer(";

	public AddCookieWriter(final AddCookie addCookie) {		
				super(ImmutableJavascript.builder().name("setCookieForServer " + addCookie.getName()).content(buildJavascript(addCookie)).build());
	}
	
	static String buildJavascript(final AddCookie addCookie){
		final StringBuilder content = new StringBuilder(CONTENT_FIRST_PART);
		content.append("\"").append(addCookie.getDomain()).append("\"");
		content.append(",\"").append(buildCookie(addCookie)).append("\"");
		return content.toString();
	}
	
	static String buildCookie(final AddCookie addCookie){
		final StringBuilder content = new StringBuilder();
		content.append(addCookie.getName()).append("=").append(addCookie.getValue());
		if(addCookie.getExpires().isPresent()){
			content.append(";expires=").append(addCookie.getExpires());				
		}
		if(addCookie.getPath().isPresent()){
			content.append(";path=").append(addCookie.getPath());				
		}
		return content.toString();
	}	
	
	public static AddCookieWriter of(final AddCookie addCookie) {
		return new AddCookieWriter(addCookie);
	}
}
