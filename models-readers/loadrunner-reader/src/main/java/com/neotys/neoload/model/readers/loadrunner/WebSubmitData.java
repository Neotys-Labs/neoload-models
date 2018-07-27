package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.*;

import java.util.List;
import java.util.Optional;

public class WebSubmitData extends WebRequest {
	
	private WebSubmitData() {}
	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

		final PostRequest postRequest = buildPostFormRequest(visitor, method);
		pageBuilder.addChilds(postRequest);

		// we use the request headers of the main request for the resources.
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(postRequest.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile));

        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString())
				.ifPresent(stringList -> getUrlList(stringList, getUrl(visitor.getLeftBrace(), visitor.getRightBrace(), method))
						.forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url, Optional.empty(), recordedHeaders))));
        
        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
}
