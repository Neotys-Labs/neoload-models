package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebUrl;
import com.neotys.neoload.model.repository.Page;

public class WebUrlMethod implements LoadRunnerMethod {

	public WebUrlMethod() {	
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		visitor.readSupportedFunction(method.getName(), ctx);
		final Page page = WebUrl.toElement(visitor, method);
		visitor.getCurrentExtractors().clear();
		visitor.getCurrentValidators().clear();
		visitor.getCurrentHeaders().clear();
		visitor.setCurrentRequestFromPage(page);
		visitor.addInCurrentContainer(page);
		return page;
	}
}
