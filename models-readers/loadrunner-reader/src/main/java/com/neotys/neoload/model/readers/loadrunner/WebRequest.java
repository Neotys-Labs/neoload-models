package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.neotys.neoload.model.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.neotys.neoload.model.readers.loadrunner.MethodUtils.getParameterValueWithName;
import static com.neotys.neoload.model.readers.loadrunner.MethodUtils.ITEM_BOUNDARY.ITEMDATA;
import static java.nio.file.Files.newInputStream;


public abstract class WebRequest {

    static final Logger LOGGER = LoggerFactory.getLogger(WebRequest.class);
    
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
			return Request.HttpMethod.valueOf(getParameterValueWithName(leftBrace, rightBrace, method, "Method").orElse(DEFAULT_HTTP_METHOD.toString()));
		}catch(IllegalArgumentException e) {
			return DEFAULT_HTTP_METHOD;
		}
	}

	/**
	 * Find the url in the LR function and parse it to an URL Object
	 * @param method represent the LR function
	 * @param urlTag tag used to find the correct keywords in the function (also used for parsing)
	 */
    @VisibleForTesting
    protected static URL getUrlFromMethodParameters(final String leftBrace, final String rightBrace, MethodCall method, String urlTag) {
        Optional<String> urlParam = MethodUtils.getParameterWithName(method, urlTag);
        try {
        	String urlString = MethodUtils.unquote(urlParam.orElseThrow(IllegalArgumentException::new));
			//the +1 is for the "=" that follows the url tag
			return getUrlFromParameterString(leftBrace, rightBrace, urlString.substring(urlTag.length()+1));
        }catch (IllegalArgumentException e) {
            LOGGER.error("Cannot find URL in WebUrl Node:"+method.getName()  + "\nThe error is : " + e);
        }
        return null;
    }

    @VisibleForTesting
    protected static URL getUrlFromParameterString(final String leftBrace, final String rightBrace, String urlParam) {
		String urlStr=null;
		try {
			urlStr = MethodUtils.normalizeString(leftBrace, rightBrace, urlParam);
			return URI.create(urlStr).toURL();
		}catch(IllegalArgumentException | MalformedURLException e) {
			LOGGER.error("Invalid URL in LR project:" + urlStr + "\nThe error is : " + e);
			if(urlParam.startsWith(leftBrace)) {
				// the protocol is in a variable like {BaseUrl}/index.html, try to do something
				return getUrlFromParameterString(leftBrace, rightBrace, "http://" + urlParam);
			}
			// Since JDK 20 (JDK-8293590), new URL(String) rejects NeoLoad variable placeholders such as
			// "${host}" in the host part ("Illegal character found in host"). Rebuild the URL with a
			// lenient URLStreamHandler, which is exempt from that validation, to keep the variable host.
			return buildUrlWithVariableHost(urlStr);
		}
	}

	/**
	 * Builds a URL whose host is a NeoLoad variable (e.g. {@code http://${host}/path}). The built-in
	 * JDK stream handlers reject such hosts since JDK 20, so a lenient {@link URLStreamHandler} is used
	 * to preserve the previous (JDK 11) behavior across JDK versions.
	 */
	@VisibleForTesting
	@SuppressWarnings({"deprecated","squid:S1874"}) //Not possible to use URI since it strictly enforces RFC 2396/3986 syntax, we keep deprecated URL
    protected static URL buildUrlWithVariableHost(final String urlStr) {
		if (urlStr == null || !urlStr.contains("://")) {
			return null;
		}
		final String protocol = urlStr.substring(0, urlStr.indexOf("://"));
		final int defaultPort = "https".equalsIgnoreCase(protocol) ? 443 : ("http".equalsIgnoreCase(protocol) ? 80 : -1);
		try {
			return new URL(null, urlStr, new VariableHostUrlStreamHandler(defaultPort));
		} catch (final MalformedURLException e) {
			LOGGER.error("Cannot build URL with variable host from: " + urlStr + "\nThe error is : " + e);
			return null;
		}
	}

	/**
	 * Lenient stream handler that parses a URL without the host validation the JDK built-in handlers
	 * perform since JDK 20. Only used for URLs carrying a NeoLoad variable in the host; such URLs are
	 * never opened.
	 */
	private static final class VariableHostUrlStreamHandler extends URLStreamHandler {
		private final int defaultPort;

		private VariableHostUrlStreamHandler(final int defaultPort) {
			this.defaultPort = defaultPort;
		}

		@Override
		protected int getDefaultPort() {
			return defaultPort;
		}

		@Override
		protected URLConnection openConnection(final URL url) {
			throw new UnsupportedOperationException("Cannot open a connection to a URL with a variable host: " + url);
		}

		@Override
		protected void parseURL(final URL url, final String spec, final int start, final int limit) {
			final String content = spec.substring(start, limit);
			final String rest = content.substring(content.indexOf("://") + 3);
			final int pathStart = rest.indexOf('/');
			final String authority = (pathStart >= 0) ? rest.substring(0, pathStart) : rest;
			final String file = (pathStart >= 0) ? rest.substring(pathStart) : "";
			String host = authority;
			int port = -1;
			final int portSeparator = authority.lastIndexOf(':');
			if (portSeparator >= 0) {
				try {
					port = Integer.parseInt(authority.substring(portSeparator + 1));
					host = authority.substring(0, portSeparator);
				} catch (final NumberFormatException ignored) {
					// ':' belongs to the host (or its variable): keep the whole authority as host.
				}
			}
			String path = file;
			String query = null;
			final int queryStart = file.indexOf('?');
			if (queryStart >= 0) {
				path = file.substring(0, queryStart);
				query = file.substring(queryStart + 1);
			}
			setURL(url, url.getProtocol(), host, port, authority, null, path, query, null);
		}
	}
    
    /**
	 * generate URLs from extrares extract of a "web_url" or "web_submit_data"
	 * @param extraresPart the extract that contains an "EXTRARES" section
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
			String spec = item.getAttribute(urlTag).get();
			URI resolvedURI = context != null ? context.toURI().resolve(spec) : URI.create(spec);
			return Optional.of(resolvedURI.toURL());
		} catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
			LOGGER.warn("Invalid URL found in request, could be a variable in the host");
		}
		return Optional.empty();
	}
    	
    /**
     * Generate an immutable request from a given URL object
     */
    @VisibleForTesting
    protected static GetPlainRequest buildGetRequestFromURL(final LoadRunnerVUVisitor visitor,
															final URL url,
															final Optional<RecordedFiles> recordedFiles,
															final List<Header> recordedHeaders) {
    	final ImmutableGetPlainRequest.Builder requestBuilder = ImmutableGetPlainRequest.builder()
				// Just create a unique name, no matter the request name, should just be unique under a page
                .name(UUID.randomUUID().toString())
                .path(url.getPath())
                .server(visitor.getReader().getServer(url))
                .httpMethod(Request.HttpMethod.GET)
				.recordedFiles(recordedFiles);

    	requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
    	requestBuilder.addAllValidators(visitor.getCurrentValidators());

		requestBuilder.addAllHeaders(recordedHeaders);
    	requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
    	visitor.getCurrentHeaders().clear();
    	requestBuilder.addAllHeaders(visitor.getGlobalHeaders());
    	
    	MethodUtils.queryToParameterList(url.getQuery()).forEach(requestBuilder::addParameters);    	
        
        return requestBuilder.build();
    }

    /**
     *
     * @param method represent the LR "web_submit_data" function
     * @return the associate POST_REQUEST
     */
    public static PostRequest buildPostFormRequest(final LoadRunnerVUVisitor visitor, final MethodCall method) {
    	final URL mainUrl = Preconditions.checkNotNull(getUrl(visitor.getLeftBrace(), visitor.getRightBrace(), method));

    	final Optional<Properties> snapshotProperties = getSnapshotProperties(visitor, method); 
    	final Optional<RecordedFiles> recordedFiles = getRecordedFilesFromSnapshotProperties(visitor, snapshotProperties);
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(recordedFiles.flatMap(RecordedFiles::recordedRequestHeaderFile));

		final ImmutablePostFormRequest.Builder requestBuilder = ImmutablePostFormRequest.builder()
                .name(mainUrl.getPath())
                .path(mainUrl.getPath())
                .server(visitor.getReader().getServer(mainUrl))
                .httpMethod(getMethod(visitor.getLeftBrace(), visitor.getRightBrace(), method))
				.recordedFiles(recordedFiles);
		getContentType(snapshotProperties).ifPresent(requestBuilder::contentType);
    	requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
    	requestBuilder.addAllValidators(visitor.getCurrentValidators());

    	requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
    	visitor.getCurrentHeaders().clear();
    	requestBuilder.addAllHeaders(visitor.getGlobalHeaders());
    	requestBuilder.addAllHeaders(recordedHeaders);

    	MethodUtils.extractItemListAsStringList(visitor, method.getParameters(), ITEMDATA, Optional.empty())
				.ifPresent(stringList -> buildPostParamsFromExtract(stringList).forEach(requestBuilder::addPostParameters));

    	MethodUtils.queryToParameterList(mainUrl.getQuery()).forEach(requestBuilder::addParameters);

        return requestBuilder.build();
    }

    protected static Optional<Properties> getSnapshotProperties(final LoadRunnerVUVisitor visitor, final MethodCall method) {
    	final String snapshotFileName = getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method, "Snapshot").orElse("");
		if (isNullOrEmpty(snapshotFileName)) {
			return Optional.empty();
		}
		try{
			final Properties properties = new Properties();
			properties.load(newInputStream(visitor.getReader().getCurrentScriptDataFolder().resolve(snapshotFileName)));
			return Optional.of(properties);			
		} catch (final IOException e) {
			LOGGER.warn("Cannot find snapshot properties files: ", e);
		}
		return Optional.empty();
	}

	/**
     * Generate an immutable request of type Follow link
     */
    @VisibleForTesting
    protected static GetFollowLinkRequest buildGetFollowLinkRequest(final LoadRunnerVUVisitor visitor, final MethodCall method, final String name, final String textFollowLink) {
		final Optional<Properties> snapshotProperties = getSnapshotProperties(visitor, method); 
    	final Optional<RecordedFiles> recordedFiles = getRecordedFilesFromSnapshotProperties(visitor, snapshotProperties);
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(recordedFiles.flatMap(RecordedFiles::recordedRequestHeaderFile));
		
		final ImmutableGetFollowLinkRequest.Builder requestBuilder = ImmutableGetFollowLinkRequest.builder()
				.name(name)      
				.path(name)
                .text(textFollowLink)
                .httpMethod(Request.HttpMethod.GET)
				.recordedFiles(recordedFiles);

		getContentType(snapshotProperties).ifPresent(requestBuilder::contentType);
    	requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
    	requestBuilder.addAllValidators(visitor.getCurrentValidators());

    	requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
    	visitor.getCurrentHeaders().clear();
    	requestBuilder.addAllHeaders(visitor.getGlobalHeaders());
    	requestBuilder.addAllHeaders(recordedHeaders);

    	visitor.getCurrentRequest().ifPresent(requestBuilder::referer);
    	visitor.getCurrentRequest().ifPresent(cr -> requestBuilder.server(cr.getServer()));    	
    	requestBuilder.path(buildExtractorPath(snapshotProperties));   	
        return requestBuilder.build();
    }

	/**
     * Generate an immutable request of type Submit form
     */
    @VisibleForTesting
    protected static PostSubmitFormRequest buildPostSubmitFormRequest(final LoadRunnerVUVisitor visitor, final MethodCall method, final String name) {
    	final Optional<Properties> snapshotProperties = getSnapshotProperties(visitor, method); 
    	final Optional<RecordedFiles> recordedFiles = getRecordedFilesFromSnapshotProperties(visitor, snapshotProperties);
		final List<Header> recordedHeaders = getHeadersFromRecordedFile(recordedFiles.flatMap(RecordedFiles::recordedRequestHeaderFile));

		final ImmutablePostSubmitFormRequest.Builder requestBuilder = ImmutablePostSubmitFormRequest.builder()
				.name(name)
				.path(name)
                .httpMethod(Request.HttpMethod.POST)
				.recordedFiles(recordedFiles);
		getContentType(snapshotProperties).ifPresent(requestBuilder::contentType);
    	requestBuilder.addAllExtractors(visitor.getCurrentExtractors());
    	requestBuilder.addAllValidators(visitor.getCurrentValidators());

    	requestBuilder.addAllHeaders(visitor.getCurrentHeaders());
    	visitor.getCurrentHeaders().clear();
    	requestBuilder.addAllHeaders(visitor.getGlobalHeaders());
		requestBuilder.addAllHeaders(recordedHeaders);

    	visitor.getCurrentRequest().ifPresent(requestBuilder::referer);
    	visitor.getCurrentRequest().ifPresent(cr -> requestBuilder.server(cr.getServer()));    	
    	requestBuilder.path(buildExtractorPath(snapshotProperties));   	
    	
    	MethodUtils.extractItemListAsStringList(visitor, method.getParameters(), ITEMDATA, Optional.empty())
				.ifPresent(stringList -> buildPostParamsFromExtract(stringList).forEach(requestBuilder::addPostParameters));
        return requestBuilder.build();
    }

	private static Optional<String> buildExtractorPath(final Optional<Properties> snapshotProperties) {
		if(!snapshotProperties.isPresent()){
			return Optional.empty();
		}		
		return extractPathFromUrl(snapshotProperties.get().getProperty("URL1", ""));		
	}

	@VisibleForTesting
	static Optional<String> extractPathFromUrl(final String url){
		if(!Strings.isNullOrEmpty(url)){
			try {
				return Optional.of(URI.create(url).toURL().getPath());
			} catch (IllegalArgumentException | MalformedURLException e) {
				LOGGER.error("Cannot extract path from URL :"+url  + "\nThe error is : " + e);
			}
		}	
		return Optional.empty();
	}

	protected static Optional<RecordedFiles> getRecordedFilesFromSnapshotProperties(final LoadRunnerVUVisitor visitor, final Optional<Properties> snapshotProperties) {
		if(!snapshotProperties.isPresent()){
			return Optional.empty();
		}
		final Path projectDataPath = visitor.getReader().getCurrentScriptDataFolder();
		final Optional<String> requestHeaderFile = getRecordedFileName(snapshotProperties.get(), "RequestHeaderFile", projectDataPath);
		final Optional<String> requestBodyFile = getRecordedFileName(snapshotProperties.get(), "RequestBodyFile", projectDataPath);
		final Optional<String> responseHeaderFile = getRecordedFileName(snapshotProperties.get(),"ResponseHeaderFile", projectDataPath);
		final Optional<String> responseBodyFile = getRecordedFileName(snapshotProperties.get(),"FileName1", projectDataPath);

		return Optional.of(ImmutableRecordedFiles.builder()
				.recordedRequestHeaderFile(requestHeaderFile)
				.recordedRequestBodyFile(requestBodyFile)
				.recordedResponseHeaderFile(responseHeaderFile)
				.recordedResponseBodyFile(responseBodyFile)
				.build());		
	}

	private static Optional<String> getRecordedFileName(final Properties properties, final String key, Path projectDataPath) {
		final String propertyValue = properties.getProperty(key, "");
		if (isNullOrEmpty(propertyValue) || "NONE".equals(propertyValue)) {
			return Optional.empty();
		}
		return Optional.ofNullable(projectDataPath.resolve(propertyValue).toString());
	}

	static List<Header> getHeadersFromRecordedFile(final Optional<String> recordedRequestHeaderFile){
    	if(!recordedRequestHeaderFile.isPresent()){
    		return ImmutableList.of();
		}
		try (final Stream<String> linesStream = Files.lines(Paths.get(recordedRequestHeaderFile.get()))) {
			final String contentWithoutFirstLine = linesStream.skip(1).collect(Collectors.joining(System.lineSeparator()));
			try (final Reader reader = CharSource.wrap(contentWithoutFirstLine).openStream()) {
				final Properties properties = new Properties();
				properties.load(reader);
				return properties.entrySet().stream()
						.filter(entry -> entry.getKey() instanceof String && entry.getValue() instanceof String)
						.map(entry -> ImmutableHeader.builder().headerName((String) entry.getKey()).headerValue((String) entry.getValue()).build())
						.collect(Collectors.toList());
			}
		} catch (final IOException e) {
			LOGGER.error("Can not read recorded request headers", e);
		}
		return ImmutableList.of();
	}

    protected static URL getUrl(final String leftBrace, final String rightBrace, MethodCall method) {
		return getUrlFromMethodParameters(leftBrace, rightBrace, method, "Action");
	}

    /**
   	 * generate parameters from the "ITEM_DATA" of a "web_submit_data"
   	 * @param extractPart the extract containing only the "ITEM_DATA" of the "web_submit_data"
   	 */
    @VisibleForTesting
   	public static List<Parameter> buildPostParamsFromExtract(List<String> extractPart) {

           List<Item> items = MethodUtils.parseItemList(extractPart);
           return items.stream()
                   .filter(item -> item.getAttribute("Name").isPresent())
                   .<Parameter>map(item -> ImmutableParameter.builder().name(item.getAttribute("Name").get()).value(item.getAttribute("Value")).build())
                   .collect(Collectors.toList());
   	}
    
    private static Optional<String> getContentType(final Optional<Properties> snapshotProperties) {
		if(!snapshotProperties.isPresent()){
			return Optional.empty();
		}
		final Object property = snapshotProperties.get().get("ContentType");
		if(property == null){
			return Optional.empty();
		}
		return Optional.of(property.toString());
	}
}
