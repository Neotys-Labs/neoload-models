package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.*;

import java.util.List;
import java.util.Optional;
import static com.neotys.neoload.model.readers.loadrunner.MethodUtils.ITEM_BOUNDARY.EXTRARES;
public class WebSubmitData extends WebRequest {
	
	private WebSubmitData() {}
	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

		final PostRequest postRequest = buildPostFormRequest(visitor, method);
		pageBuilder.addChilds(postRequest);

		// we use the request headers of the main request for the resources.
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(postRequest.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile));

        MethodUtils.extractItemListAsStringList(visitor,method.getParameters(), EXTRARES, Optional.of(pageBuilder))
				.ifPresent(stringList -> getUrlList(stringList, getUrl(visitor.getLeftBrace(), visitor.getRightBrace(), method))
						.forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url, Optional.empty(), recordedHeaders))));
        final String name = MethodUtils.normalizeName(MethodUtils.normalizeString(
                visitor.getLeftBrace(),
                visitor.getRightBrace(),
                method.getParameters().get(0)
        ));
        return pageBuilder.name(name)
                .thinkTime(0)
                .build();
    }
}
