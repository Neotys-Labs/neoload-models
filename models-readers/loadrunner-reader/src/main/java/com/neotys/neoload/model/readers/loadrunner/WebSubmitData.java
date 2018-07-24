package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.Request;

import java.util.Optional;

public class WebSubmitData extends WebRequest {
	
	protected static final Request.HttpMethod DEFAULT_HTTP_METHOD = Request.HttpMethod.POST;
	
	private WebSubmitData() {}
	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

        pageBuilder.addChilds(buildPostFormRequest(visitor, method));
        
        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString())
				.ifPresent(stringList -> getUrlList(stringList, getUrl(visitor.getLeftBrace(), visitor.getRightBrace(), method))
						.forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url, Optional.empty()))));
        
        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }

}
