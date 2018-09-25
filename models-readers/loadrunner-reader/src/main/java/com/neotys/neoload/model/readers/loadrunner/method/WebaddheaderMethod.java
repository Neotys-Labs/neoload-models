package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.Header;
import com.neotys.neoload.model.repository.ImmutableHeader;

public class WebaddheaderMethod implements LoadRunnerMethod {
	
	public WebaddheaderMethod() {
		super();
	}

	@Override
	public final List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		
		if(method.getParameters() == null || method.getParameters().size() != 2){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have 2 parameters: " + method.getName() + "(const char *Header, const char *Content)");
			return Collections.emptyList();
		}
		final String headerName = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final String headerValue = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(1));
		final Header header = ImmutableHeader.builder().headerName(headerName).headerValue(headerValue).build();
		visitor.getCurrentHeaders().add(header);
		visitor.readSupportedFunction(method.getName(), ctx);		
		return Collections.emptyList();
	}	
}
