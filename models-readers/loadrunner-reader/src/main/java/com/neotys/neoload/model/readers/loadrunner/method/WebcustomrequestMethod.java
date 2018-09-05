package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebCustomRequest;
import com.neotys.neoload.model.repository.Page;

public class WebcustomrequestMethod  implements LoadRunnerMethod {
	
	public WebcustomrequestMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {		
		final Page page = WebCustomRequest.toElement(visitor, method, ctx);
		visitor.getCurrentExtractors().clear();
		visitor.getCurrentValidators().clear();
		visitor.getCurrentHeaders().clear();
		visitor.setCurrentRequestFromPage(page);
		visitor.addInCurrentContainer(page);
		return page;
	}

}
