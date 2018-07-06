package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.Validator;
import com.neotys.neoload.model.repository.VariableExtractor;


public class WebUrl extends WebRequest {
	private WebUrl() {}
	

	
    public static Element toElement(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, MethodCall method, List<VariableExtractor> extractors, List<Validator>validators) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();
        
        final URL mainUrl = Preconditions.checkNotNull(getUrlFromMethodParameters(leftBrace, rightBrace, method));
        pageBuilder.addChilds(buildGetRequestFromURL(reader, mainUrl, extractors, validators));

        MethodUtils.extractItemListAsStringList(leftBrace, rightBrace, method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).ifPresent(stringList ->
                pageBuilder.addAllChilds(getUrlList(stringList, mainUrl).stream().map(url -> WebRequest.buildGetRequestFromURL(reader, url)).collect(Collectors.toList())));

        return pageBuilder.name(MethodUtils.normalizeString(leftBrace, rightBrace, method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
	
	
}
