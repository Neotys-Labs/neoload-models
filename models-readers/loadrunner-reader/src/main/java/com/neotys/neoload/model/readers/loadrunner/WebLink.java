package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.ImmutablePage;


public class WebLink extends WebRequest {
	private WebLink() {}
	

	
    public static Element toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        final ImmutablePage.Builder pageBuilder = ImmutablePage.builder();        
        pageBuilder.addChilds(buildGetFollowLinkRequest(visitor, "TODO seb"));
        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
	
	
}
