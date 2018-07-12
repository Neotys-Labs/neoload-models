package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Optional;

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

	private static final String NAME_VALUE_COOKIE_SEPARATOR = "=";
	private static final String PARAMETER_DOMAIN = "domain";
	private static final String PARAMETER_EXPIRES = "expires";
	private static final String PARAMETER_PATH = "path";
	private static final String METHOD = "Method ";
	
	public WebAddCookieMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		if(method.getParameters() == null || method.getParameters().size() < 2){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have at least 2 parameters.");
			return null;
		}
		final String nameAndValue = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		if(Strings.isNullOrEmpty(nameAndValue) || !nameAndValue.contains(NAME_VALUE_COOKIE_SEPARATOR)){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have the first parameter parameter with format 'name=VALUE'.");
			return null;
		}
		final String[] nameAndValueArray = nameAndValue.split(NAME_VALUE_COOKIE_SEPARATOR);
		final String name = nameAndValueArray[0];
		final String value = nameAndValueArray[1];
		final Builder builder = ImmutableAddCookie.builder().name(name).value(value);
		final Optional<String> domain = MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, PARAMETER_DOMAIN);
		if(domain.isPresent()){
			builder.domain(domain.get());
		} else {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have a " + PARAMETER_DOMAIN + " attribute.");
		}		
		MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, PARAMETER_EXPIRES).ifPresent(builder::expires);
		MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, PARAMETER_PATH).ifPresent(builder::path);
		
		return builder.build();
	}
}
