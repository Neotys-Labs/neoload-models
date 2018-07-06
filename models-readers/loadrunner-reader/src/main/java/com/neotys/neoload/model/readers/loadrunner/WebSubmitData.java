package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.*;

public class WebSubmitData extends WebRequest {
	
	protected static final Request.HttpMethod DEFAULT_HTTP_METHOD = Request.HttpMethod.POST;
	
	private WebSubmitData() {}

	protected static URL getUrl(final String leftBrace, final String rightBrace, MethodCall method) {
		return getUrlFromMethodParameters(leftBrace, rightBrace, method, "Action");
	}
	
    public static Element toElement(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, final MethodCall method, List<VariableExtractor> extractors, List<Validator>validators) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

        pageBuilder.addChilds(buildPostRequest(reader, leftBrace, rightBrace, method, extractors, validators));
        
        MethodUtils.extractItemListAsStringList(leftBrace, rightBrace, method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).ifPresent(stringList ->
        		getUrlList(stringList, getUrl(leftBrace, rightBrace, method)).stream().forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(reader, url))));
        
        return pageBuilder.name(MethodUtils.normalizeString(leftBrace, rightBrace, method.getParameters().get(0)))
                .thinkTime(0)
                .build();
    }
    
    /**
     * 
     * @param method represent the LR "web_submit_data" function
     * @return the associate POST_REQUEST
     */
    public static PostRequest buildPostRequest(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, final MethodCall method, final List<VariableExtractor> extractors, final List<Validator>validators) {
    	URL mainUrl = Preconditions.checkNotNull(getUrl(leftBrace, rightBrace, method));
    	
    	ImmutablePostFormRequest.Builder requestBuilder = ImmutablePostFormRequest.builder()
                .name(mainUrl.getPath())
                .path(mainUrl.getPath())
                .server(getServer(reader, mainUrl))
                .httpMethod(getMethod(leftBrace, rightBrace, method));

    	if (extractors != null && !extractors.isEmpty()) requestBuilder.addAllExtractors(extractors);
    	if (validators != null && !validators.isEmpty()) requestBuilder.addAllValidators(validators);
    	
    	MethodUtils.extractItemListAsStringList(leftBrace, rightBrace, method.getParameters(), MethodUtils.ITEM_BOUNDARY.ITEMDATA.toString()).ifPresent(stringList -> buildPostParamsFromExtract(stringList)
				.stream().forEach(requestBuilder::addPostParameters));
        
    	MethodUtils.queryToParameterList(mainUrl.getQuery()).forEach(requestBuilder::addParameters);
        
        return requestBuilder.build();
    }

    /**
	 * generate parameters from the "ITEM_DATA" of a "web_submit_data"
	 * @param extractPart the extract containing only the "ITEM_DATA" of the "web_submit_data"
	 * @return
	 */
    @VisibleForTesting
	public static List<Parameter> buildPostParamsFromExtract(List<String> extractPart) {

        List<Item> items = MethodUtils.parseItemList(extractPart);
        return items.stream()
                .filter(item -> item.getAttribute("Name").isPresent())
                .<Parameter>map(item -> ImmutableParameter.builder().name(item.getAttribute("Name").get()).value(item.getAttribute("Value")).build())
                .collect(Collectors.toList());

	}

}
