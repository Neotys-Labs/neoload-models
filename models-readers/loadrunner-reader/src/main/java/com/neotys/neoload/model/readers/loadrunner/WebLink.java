package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.ImmutablePage;


public class WebLink extends WebRequest {
	private WebLink() {}
	

	
    public static Element toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        final ImmutablePage.Builder pageBuilder = ImmutablePage.builder();        
        pageBuilder.addChilds(buildGetRequest(visitor, Optional.empty()));
        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString());
        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
	
	
}
