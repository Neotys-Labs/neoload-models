package com.neotys.neoload.model.readers.loadrunner.method;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableAddCookie;
import com.neotys.neoload.model.repository.ImmutableAddCookie.Builder;
import com.neotys.neoload.model.repository.Server;
public class WebaddcookieMethod implements LoadRunnerMethod {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(WebaddcookieMethod.class);
	
	public WebaddcookieMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		if(method.getParameters() == null || method.getParameters().isEmpty()){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have at least 1 parameter.");
			return Collections.emptyList();
		}
		final String cookie = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final List<HttpCookie> httpCookies;
		try{
			httpCookies = HttpCookie.parse(cookie);
		} catch (final Exception exception){
			final String warnMessage = "Cannot parse cookie (" + cookie + "): " + exception.getMessage();
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, warnMessage);
			LOGGER.warn(warnMessage, exception);
			return Collections.emptyList();
		}
		if(httpCookies == null || httpCookies.size() != 1){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot parse cookie (" + cookie + ").");			
			return Collections.emptyList();
		}	
		visitor.readSupportedFunction(method.getName(), ctx);
		final HttpCookie httpCookie = httpCookies.get(0);
		final String cookieName = httpCookie.getName();
		final String cookieValue = httpCookie.getValue();
		final String cookieDomain = httpCookie.getDomain();
		final Builder builder = ImmutableAddCookie.builder();
		URL url;
		try{
			url = new URL(cookieDomain);
		} catch(final MalformedURLException e1){
			try{
				url = new URL("http://" + cookieDomain);
			} catch(final MalformedURLException e2){
				url = null;
			}			
		}
		if(url == null){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot parse URL (" + cookieDomain + ").");			
			return Collections.emptyList();
		}
		final Server server = visitor.getReader().getServer(url);		
				
		builder.name("Set cookie " + cookieName + " for server " + server.getName())
				.cookieName(cookieName)
				.cookieValue(cookieValue)
				.server(server);
		
		final long maxAge = httpCookie.getMaxAge();
		if(maxAge != -1L){
			builder.expires(Long.toString(maxAge));
		}	
		return ImmutableList.of(builder.build());
	}
}
