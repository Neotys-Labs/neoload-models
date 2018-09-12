package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.ImmutableClearCache;

public class WebcachecleanupMethod implements LoadRunnerMethod {

	public WebcachecleanupMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		visitor.readSupportedFunction(method.getName(), ctx);		
		return ImmutableList.of(ImmutableClearCache.builder().name(method.getName()).build());
	}

}
