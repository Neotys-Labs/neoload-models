package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebSubmitData;
import com.neotys.neoload.model.repository.Page;

public class WebSubmitDataMethod  implements LoadRunnerMethod {
	
	public WebSubmitDataMethod() {
		super();
	}

	@Override
	public Element getElement(LoadRunnerVUVisitor visitor, MethodCall method, MethodcallContext ctx) {
		visitor.readSupportedFunction(method.getName(), ctx);
		final Page page = WebSubmitData.toElement(visitor, method);
		visitor.getCurrentExtractors().clear();
		visitor.getCurrentValidators().clear();
		visitor.getCurrentHeaders().clear();
		visitor.setCurrentRequestFromPage(page);
		visitor.addInCurrentContainer(page);
		return page;
	}

}
