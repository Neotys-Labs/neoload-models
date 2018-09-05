package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableAddCookie;
import com.neotys.neoload.model.repository.ImmutableAddCookie.Builder;
import com.neotys.neoload.model.repository.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
public class WebaddcookieMethod implements LoadRunnerMethod {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(WebaddcookieMethod.class);
	
	public WebaddcookieMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		if(method.getParameters() == null || method.getParameters().isEmpty()){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have at least 1 parameter.");
			return null;
		}
		final String cookie = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final List<HttpCookie> httpCookies;
		try{
			httpCookies = HttpCookie.parse(cookie);
		} catch (final Exception exception){
			final String warnMessage = "Cannot parse cookie (" + cookie + "): " + exception.getMessage();
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, warnMessage);
			LOGGER.warn(warnMessage, exception);
			return null;
		}
		if(httpCookies == null || httpCookies.size() != 1){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot parse cookie (" + cookie + ").");			
			return null;
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
			return null;
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
		final Element addCookie = builder.build();
		visitor.addInCurrentContainer(addCookie);
		return addCookie;
	}
}
