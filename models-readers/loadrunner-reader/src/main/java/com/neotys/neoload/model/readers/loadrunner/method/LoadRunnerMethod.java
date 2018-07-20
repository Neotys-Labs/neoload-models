package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;

public interface LoadRunnerMethod {
	
	static final String METHOD = "Method ";
	
	Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final CPP14Parser.MethodcallContext ctx);
}
