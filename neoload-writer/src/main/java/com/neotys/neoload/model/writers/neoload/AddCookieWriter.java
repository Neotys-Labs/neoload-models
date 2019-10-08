package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.AddCookie;

public class AddCookieWriter extends JavascriptWriter {

	private static final String CONTENT_FIRST_PART = "// Add cookie to a HTTP server.\ncontext.currentVU.setCookieForServer(";

	public AddCookieWriter(final AddCookie addCookie) {		
		super(addCookie);
	}
	
	@Override
	protected String getJavascriptContent() {
		final StringBuilder content = new StringBuilder(CONTENT_FIRST_PART);
		content.append("\"").append(((AddCookie)element).getServer().getName()).append("\"");
		content.append(",\"").append(buildCookie(((AddCookie)element))).append("\"");
		content.append(");");
		return content.toString();
	}
		
	static String buildCookie(final AddCookie addCookie){
		final StringBuilder content = new StringBuilder();
		content.append(addCookie.getCookieName()).append("=").append(addCookie.getCookieValue());
		addCookie.getExpires().ifPresent(s -> content.append("; expires=").append(s));
		addCookie.getPath().ifPresent(s -> content.append("; path=").append(s));
		return content.toString();
	}	
	
	public static AddCookieWriter of(final AddCookie addCookie) {
		return new AddCookieWriter(addCookie);
	}
}
