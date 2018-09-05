package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;

public class LrendtransactionMethod implements LoadRunnerMethod {

	public LrendtransactionMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		final int size = visitor.getCurrentContainers().size();
		if (size <= 1) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot end non existing transaction.");
			return null;
		}
		visitor.readSupportedFunction(method.getName(), ctx);
		final Element transaction = visitor.getCurrentContainers().remove(size - 1).build();
		visitor.addInCurrentContainer(transaction);
		return transaction;
	}
}
