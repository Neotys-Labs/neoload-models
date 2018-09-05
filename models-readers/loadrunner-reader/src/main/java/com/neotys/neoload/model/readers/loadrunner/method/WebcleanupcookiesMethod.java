package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableClearCookies;

public class WebcleanupcookiesMethod implements LoadRunnerMethod {

	public WebcleanupcookiesMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		visitor.readSupportedFunction(method.getName(), ctx);		
		final Element clearCookies = ImmutableClearCookies.builder().name(method.getName()).build();
		visitor.addInCurrentContainer(clearCookies);
		return clearCookies;
	}
}
