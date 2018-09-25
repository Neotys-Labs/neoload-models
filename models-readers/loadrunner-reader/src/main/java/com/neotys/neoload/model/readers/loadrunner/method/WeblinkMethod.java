package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebLink;
import com.neotys.neoload.model.repository.Page;

public class WeblinkMethod implements LoadRunnerMethod {

	public WeblinkMethod() {	
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {		
		final Page page = WebLink.toElement(visitor, method, ctx);
		visitor.getCurrentExtractors().clear();
		visitor.getCurrentValidators().clear();
		visitor.getCurrentHeaders().clear();
		visitor.setCurrentRequestFromPage(page);
		return ImmutableList.of(page);
	}
}
