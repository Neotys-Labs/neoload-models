package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ContainerForMulti;
import com.neotys.neoload.model.repository.ImmutableContainerForMulti;

public class LrendtransactionMethod implements LoadRunnerMethod {

	public LrendtransactionMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		final int size = visitor.getCurrentContainers().size();
		if (size <= 1) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot end non existing transaction.");
			return Collections.emptyList();
		}		
		final Object last = visitor.getCurrentContainers().get(size - 1);
		if(last instanceof ImmutableContainerForMulti.Builder){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, "Cannot end a transaction that has not be started in current block.");
			return Collections.emptyList();	
		}
		visitor.readSupportedFunction(method.getName(), ctx);
		return ImmutableList.of(LoadRunnerVUVisitor.toContainer(visitor.getCurrentContainers().remove(size - 1)));
	}
}
