package com.neotys.neoload.model.readers.loadrunner;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.ImmutablePostFormRequest;
import com.neotys.neoload.model.repository.Parameter;
import com.neotys.neoload.model.repository.PostRequest;
import com.neotys.neoload.model.repository.Request;

public class WebSubmitData extends WebRequest {
	
	protected static final Request.HttpMethod DEFAULT_HTTP_METHOD = Request.HttpMethod.POST;
	
	private WebSubmitData() {}

	protected static URL getUrl(final String leftBrace, final String rightBrace, MethodCall method) {
		return getUrlFromMethodParameters(leftBrace, rightBrace, method, "Action");
	}
	
    public static Element toElement(final LoadRunnerVUVisitor visitor, final MethodCall method) {
        Preconditions.checkNotNull(method);
        ImmutablePage.Builder pageBuilder = ImmutablePage.builder();

        pageBuilder.addChilds(buildPostRequest(visitor, method));
        
        MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.EXTRARES.toString()).ifPresent(stringList ->
        		getUrlList(stringList, getUrl(visitor.getLeftBrace(), visitor.getRightBrace(), method)).stream().forEach(url -> pageBuilder.addChilds(buildGetRequestFromURL(visitor, url))));
        
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
                .httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method));

    	requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
    	requestBuilder.addAllValidators(visitor.getCurrentValidators());
    	requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
    	visitor.getCurrentHeaders().clear();
    	requestBuilder.addAllHeaders(visitor.getGlobalHeaders());
    	
    	MethodUtils.extractItemListAsStringList(visitor.getLeftBrace(), visitor.getRightBrace(), method.getParameters(), MethodUtils.ITEM_BOUNDARY.ITEMDATA.toString()).ifPresent(stringList -> buildPostParamsFromExtract(stringList)
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
