package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebUrl;

public class WebUrlMethod implements LoadRunnerMethod {

	public WebUrlMethod() {	
		super();
	}

	@Override
	public Element getElement(LoadRunnerVUVisitor visitor, MethodCall method, MethodcallContext ctx) {
		visitor.readSupportedFunction(method.getName(), ctx);
		final Element element = WebUrl.toElement(visitor.getReader(), visitor.getLeftBrace(),
				visitor.getRightBrace(), method, visitor.getCurrentExtractors(), visitor.getCurrentValidators());
		visitor.getCurrentExtractors().clear();
		visitor.getCurrentValidators().clear();
		return element;
	}

}
