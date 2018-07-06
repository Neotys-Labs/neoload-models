package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.WebRegFind;

public class WebRegFindMethod  implements LoadRunnerMethod {
	
	public WebRegFindMethod() {
		super();
	}

	@Override
	public Element getElement(LoadRunnerVUVisitor visitor, MethodCall method, CPP14Parser.MethodcallContext ctx) {
		visitor.readSupportedFunction(method.getName(), ctx);
		visitor.getCurrentValidators().add(WebRegFind.toValidator(visitor.getLeftBrace(), visitor.getRightBrace(), method));
		return null;		
	}

}
