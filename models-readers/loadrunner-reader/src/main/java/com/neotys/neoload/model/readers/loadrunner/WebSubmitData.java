package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.*;

import java.net.URL;
import java.util.Optional;

public class WebSubmitData extends WebRequest {
	
	protected static final Request.HttpMethod DEFAULT_HTTP_METHOD = Request.HttpMethod.POST;
	
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

	/**
	 *
	 * @param method represent the LR "web_submit_data" function
	 * @return the associate POST_REQUEST
	 */
	public static PostRequest buildPostRequest(final LoadRunnerVUVisitor visitor, final MethodCall method) {
		URL mainUrl = Preconditions.checkNotNull(getUrl(visitor.getLeftBrace(), visitor.getRightBrace(), method));

		ImmutablePostFormRequest.Builder requestBuilder = ImmutablePostFormRequest.builder()
				.name(mainUrl.getPath())
				.path(mainUrl.getPath())
				.server(getServer(visitor.getReader(), mainUrl))
				.httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
				.recordedFiles(getRecordedFilesFromSnapshotFile(visitor.getLeftBrace(), visitor.getRightBrace(), method, visitor.getReader().getCurrentScriptFolder()));

		requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
		requestBuilder.addAllValidators(visitor.getCurrentValidators());
		requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
		visitor.getCurrentHeaders().clear();
		requestBuilder.addAllHeaders(visitor.getGlobalHeaders());

		MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.ITEMDATA.toString())
				.ifPresent(stringList -> buildPostParamsFromExtract(stringList).forEach(requestBuilder::addPostParameters));

		MethodUtils.queryToParameterList(mainUrl.getQuery()).forEach(requestBuilder::addParameters);

		return requestBuilder.build();
	}
}
