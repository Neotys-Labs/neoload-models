package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.List;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableContainer;

public class LrstarttransactionMethod  implements LoadRunnerMethod {
	
	public LrstarttransactionMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		if (method == null) {
			return Collections.emptyList();
		}
		if (method.getParameters() == null || method.getParameters().isEmpty()) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " must have a parameter.");
			return Collections.emptyList();
		}		
		visitor.readSupportedFunction(method.getName(), ctx);
		final String firstParameter = method.getParameters().get(0);
		final String transactionName = MethodUtils.normalizeName(firstParameter);
		final ImmutableContainer.Builder currentContainer = ImmutableContainer.builder().name(transactionName);
		visitor.getCurrentContainers().add(currentContainer);		
		return Collections.emptyList();
	}
}
