package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.Header;
import com.neotys.neoload.model.repository.ImmutableHeader;

public class WebaddautoheaderMethod implements LoadRunnerMethod {
		
	public WebaddautoheaderMethod() {
		super();
	}

	@Override
	public final Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		
		if(method.getParameters() == null || method.getParameters().size() != 2){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, METHOD + method.getName() + " should have 2 parameters: " + method.getName() + "(const char *Header, const char *Content)");
			return null;
		}
		final String headerName = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		final String headerValue = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(1));
		final Header header = ImmutableHeader.builder().headerName(headerName).headerValue(headerValue).build();
		visitor.getGlobalHeaders().add(header);
		visitor.readSupportedFunction(method.getName(), ctx);		
		return null;
	}	
}
