package com.neotys.neoload.model.readers.loadrunner.method;

import java.net.HttpCookie;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableAddCookie;
import com.neotys.neoload.model.repository.ImmutableAddCookie.Builder;
public class WebAddCookieMethod implements LoadRunnerMethod {
		
	public WebAddCookieMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		if(method.getParameters() == null || method.getParameters().size() < 1){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have at least 1 parameter.");
			return null;
		}
		final String cookie = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final List<HttpCookie> httpCookies;
		try{
			httpCookies = HttpCookie.parse(cookie);
		} catch (final Exception exception){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot parse cookie (" + cookie + "): " + exception.getMessage());
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
		final String name = "Set cookie " + cookieName + " for server " + cookieDomain;
		final Builder builder = ImmutableAddCookie.builder()
				.name(name)
				.cookieName(cookieName)
				.cookieValue(cookieValue)
				.domain(cookieDomain);	
		final String path = httpCookie.getPath();	
		if(!Strings.isNullOrEmpty(path)){
			builder.domain(path);
		}		
		final long maxAge = httpCookie.getMaxAge();
		if(maxAge != -1L){
			builder.expires(Long.toString(maxAge));
		}	
		return builder.build();
	}
}
