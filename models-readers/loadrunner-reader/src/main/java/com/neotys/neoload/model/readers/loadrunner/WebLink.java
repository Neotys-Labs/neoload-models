package com.neotys.neoload.model.readers.loadrunner;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.method.LoadRunnerMethod;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.Page;


public class WebLink extends WebRequest {
	
	private static final String TEXT_PARAM = "Text";
	
	private WebLink() {}
	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
        Preconditions.checkNotNull(method);
        final ImmutablePage.Builder pageBuilder = ImmutablePage.builder();        
        final Optional<String> text = MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, TEXT_PARAM);
        if(!text.isPresent()){
        	visitor.readSupportedFunctionWithWarn(method.getName(), ctx, LoadRunnerMethod.METHOD + method.getName() + " should have a parameter " + TEXT_PARAM);
			return null;
        }
        visitor.readSupportedFunction(method.getName(), ctx);        
        final String name = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0));
        pageBuilder.addChilds(buildGetFollowLinkRequest(visitor, method, name, text.get()));
        return pageBuilder.name(name).thinkTime(0).build();
    }
    
	
	
}
