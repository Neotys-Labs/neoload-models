package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebSubmitForm;
import com.neotys.neoload.model.repository.Page;

public class WebsubmitformMethod implements LoadRunnerMethod {

	public WebsubmitformMethod() {	
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {		
		final Page page = WebSubmitForm.toElement(visitor, method, ctx);
		visitor.getCurrentExtractors().clear();
		visitor.getCurrentValidators().clear();
		visitor.getCurrentHeaders().clear();
		visitor.setCurrentRequestFromPage(page);
		visitor.addInCurrentContainer(page);
		return page;
	}
}
