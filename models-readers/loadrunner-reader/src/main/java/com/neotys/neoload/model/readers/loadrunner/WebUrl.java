package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.ImmutablePage;


public class WebUrl extends WebRequest {
	private WebUrl() {}
	

	
    public static Element toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();
        
        final URL mainUrl = Preconditions.checkNotNull(getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method));
        pageBuilder.addChilds(buildGetRequest(visitor, Optional.of(mainUrl)));

        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).ifPresent(stringList ->
                pageBuilder.addAllChilds(getUrlList(stringList, mainUrl).stream().map(url -> WebRequest.buildGetRequest(visitor, Optional.of(url))).collect(Collectors.toList())));

        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
	
	
}
