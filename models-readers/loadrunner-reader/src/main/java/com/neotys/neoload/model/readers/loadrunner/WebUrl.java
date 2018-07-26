package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableRecordedFiles;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.RecordedFiles;

import java.net.URL;
import java.util.stream.Collectors;

public class WebUrl extends WebRequest {
	private WebUrl() {}
	

	
    public static Page toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();
        
        final URL mainUrl = Preconditions.checkNotNull(getUrlFromMethodParameters(visitor.getLeftBrace(), visitor.getRightBrace(), method));
        final RecordedFiles recordedFiles = getRecordedFilesFromSnapshotFile(visitor.getLeftBrace(), visitor.getRightBrace(), method, visitor.getReader().getProjectFolder());
        pageBuilder.addChilds(buildGetRequestFromURL(visitor, mainUrl, recordedFiles));

        // we use the headers of the main request for the resources.
        final RecordedFiles resourceRecordedFiles = ImmutableRecordedFiles.builder().recordedRequestHeaderFile(recordedFiles.recordedRequestHeaderFile()).build();

        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).ifPresent(stringList ->
                pageBuilder.addAllChilds(getUrlList(stringList, mainUrl).stream().map(url -> WebRequest.buildGetRequestFromURL(visitor, url, resourceRecordedFiles)).collect(Collectors.toList())));

        return pageBuilder.name(MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
}
