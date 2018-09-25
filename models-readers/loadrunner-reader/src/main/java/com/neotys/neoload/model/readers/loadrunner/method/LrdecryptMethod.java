package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableEvalString;

public class LrdecryptMethod implements LoadRunnerMethod {

	public LrdecryptMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		visitor.readUnsupportedFunction(method.getName(), ctx);
		final String returnValue = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
		return ImmutableList.of(ImmutableEvalString.builder().name(MethodUtils.unquote(method.getName())).returnValue(returnValue).build());
	}	
}	
