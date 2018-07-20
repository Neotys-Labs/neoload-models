package com.neotys.neoload.model.readers.loadrunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.neotys.neoload.model.repository.GetRequest;
import com.neotys.neoload.model.repository.ImmutableGetRequest;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Request;
import com.neotys.neoload.model.repository.Server;


public abstract class WebRequest {

    static Logger logger = LoggerFactory.getLogger(WebRequest.class);
    
    /**
     * item delimiter used in ITEMDATA and EXTRARES part for LR's functions
     */
	protected static final String END_ITEM_TAG = "ENDITEM";

	/**
	 * Default http method to be used if no method is declared
	 */
	protected static final Request.HttpMethod DEFAULT_HTTP_METHOD = Request.HttpMethod.GET;
	
	protected WebRequest() {}
	
	/**
	 * Default "get_url" function if the function is not redifined in upper class 
	 * @param method represent the LR function
	 * @return the url
	 */
	protected static URL getUrlFromMethodParameters(final String leftBrace, final String rightBrace, final MethodCall method) {
		return getUrlFromMethodParameters(leftBrace, rightBrace, method, "URL");
	}
	
	/**
	 * Get the used HTTP method in the LR request and return the corresponding model enum
	 * @param method represent the LR function
	 * @return the used request
	 */
	protected static Request.HttpMethod getMethod(final String leftBrace, final String rightBrace, MethodCall method) {
		try {
			return Request.HttpMethod.valueOf(MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "Method").orElse(DEFAULT_HTTP_METHOD.toString()));
		}catch(IllegalArgumentException e) {
			return DEFAULT_HTTP_METHOD;
		}
	}

	/**
	 * Find the url in the LR function and parse it to an URL Object
	 * @param method represent the LR function
	 * @param urlTag tag used to find the correct keywords in the function (also used for parsing)
	 * @return
	 */
    @VisibleForTesting
    protected static URL getUrlFromMethodParameters(final String leftBrace, final String rightBrace, MethodCall method, String urlTag) {
        Optional<String> urlParam = MethodUtils.getParameterWithName(method, urlTag);
        try {
        	String urlString = MethodUtils.unquote(urlParam.orElseThrow(IllegalArgumentException::new));
			//the +1 is for the "=" that follows the url tag
			return getUrlFromParameterString(leftBrace, rightBrace, urlString.substring(urlTag.length()+1));
        }catch (IllegalArgumentException e) {
            logger.error("Cannot find URL in WebUrl Node:"+method.getName()  + "\nThe error is : " + e);
        }
        return null;
    }

    @VisibleForTesting
    protected static URL getUrlFromParameterString(final String leftBrace, final String rightBrace, String urlParam) {
		String urlStr=null;
		try {
			urlStr = MethodUtils.normalizeString(leftBrace, rightBrace, urlParam);
			return new URL(urlStr);
		}catch(MalformedURLException e) {
			logger.error("Invalid URL in LR project:" + urlStr + "\nThe error is : " + e);
			if(e.getMessage().startsWith("no protocol") && urlParam.startsWith(leftBrace)) {
				// the protocol is in a variable like {BaseUrl}/index.html, try to do something
				return getUrlFromParameterString(leftBrace, rightBrace, "http://" + urlParam);
			}
		}
		return null;
	}

    /**
     * Build or get the server from the list of server already build from the passed url
     * @param url extracting the server from this url
     * @return the corresponding server
     */
    @VisibleForTesting
    protected static Server getServer(final LoadRunnerReader reader, URL url) {
        return reader.getOrAddServerIfNotExist(ImmutableServer.builder()
                .name(MethodUtils.normalizeName(url.getHost()))
                .host(url.getHost())
                .port(String.valueOf(url.getPort()!=-1 ? url.getPort() : url.getDefaultPort()))
                .scheme(url.getProtocol())
                .build());
    }
    
    /**
	 * generate URLs from extrares extract of a "web_url" or "web_submit_data"
	 * @param extraresPart the extract that contains an "EXTRARES" section
	 * @return
	 */
    @VisibleForTesting
	protected static List<URL> getUrlList(final List<String> extraresPart, final URL context) {

		return MethodUtils.parseItemList(extraresPart).stream()
				.filter(item -> item.getAttribute("URL").isPresent())
				.map(item -> getURLFromItem(context, item))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

	}

	private static Optional<URL> getURLFromItem(final URL context, final Item item) {
		return getURLFromItem(context, item, "URL");
	}

	private static Optional<URL> getURLFromItem(final URL context, final Item item,  final String urlTag) {
		try {
			return Optional.ofNullable(new URL(context, item.getAttribute(urlTag).get()));
		} catch (MalformedURLException e) {
			logger.warn("Invalid URL found in request, could be a variable in the host");
		}
		return Optional.empty();
	}
    	
    /**
     * Generate an immutable request from a given URL object
     * @param url
     * @return
     */
    @VisibleForTesting
    protected static GetRequest buildGetRequestFromURL(final LoadRunnerVUVisitor visitor, final URL url) {
    	ImmutableGetRequest.Builder requestBuilder = ImmutableGetRequest.builder()
				// Just create a unique name, no matter the request name, should just be unique under a page
                .name(UUID.randomUUID().toString())
                .path(url.getPath())
                .server(getServer(visitor.getReader(), url))
                .httpMethod(Request.HttpMethod.GET);

    	requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
    	requestBuilder.addAllValidators(visitor.getCurrentValidators());
    	requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
    	visitor.getCurrentHeaders().clear();
    	requestBuilder.addAllHeaders(visitor.getGlobalHeaders());
    	
        MethodUtils.queryToParameterList(url.getQuery()).forEach(requestBuilder::addParameters);
        
        return requestBuilder.build();
    }
}
