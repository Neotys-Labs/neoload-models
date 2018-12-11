package com.neotys.neoload.model.v3.util;

import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTPS_PORT;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTP_PORT;
import static com.neotys.neoload.model.v3.util.VariableUtils.getVariableName;
import static com.neotys.neoload.model.v3.util.VariableUtils.isVariableSyntax;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.server.Server.Scheme;
import com.neotys.neoload.model.v3.project.userpath.Header;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;


public class RequestUtils {
	private static final Pattern URL_PATTERN = Pattern.compile("^((http[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)"); // <scheme>://<host>:<port><file> | <file>
	private static final int URL_SERVER_GROUP = 1;
	private static final int URL_SCHEME_GROUP = 2;
	private static final int URL_HOST_GROUP = 3;
	private static final int URL_PORT_GROUP = 6;
	private static final int URL_FILE_GROUP = 9;
	
	private static final String FAKE_SERVER_URL = "http://host";
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";

	public final static String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public final static String BINARY_CONTENT_TYPE = "application/octet-stream";
	
	private static final String FUNCTION_ENCODE_URL_START = "__encodeURL(";
	private static final String FUNCTION_ENCODE_URL_END = ")";
	
	
	static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	private RequestUtils() {}
	
	public static URL parseUrl(final String url) {
		// Check if url is null or empty
		if (Strings.isNullOrEmpty(url)) {
			throw new IllegalArgumentException("url must not be null or empty.");
		}
		
		// Check if url matches the URL pattern
		final Matcher matcher = URL_PATTERN.matcher(url);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("The url '" + url + "' does not match the URL pattern: " + URL_PATTERN.pattern());
		}

		// Retrieve scheme, host, port and file from url
		final String serverUrl = matcher.group(URL_SERVER_GROUP);
		final String scheme = matcher.group(URL_SCHEME_GROUP);
		final String host = matcher.group(URL_HOST_GROUP);
		final String port = matcher.group(URL_PORT_GROUP);
		final String file = matcher.group(URL_FILE_GROUP);
			
		// Retrieve path and query from file
		String rawPath = null;
		String rawQuery = null;
		try {
			final URI fakeUri = new URI(FAKE_SERVER_URL + file);
			rawPath = fakeUri.getRawPath();
			rawQuery = fakeUri.getRawQuery();
		}
		catch (final URISyntaxException e) {
			throw new IllegalArgumentException("The url '" + url + "' does not match a valid URL: " + e.getMessage());
		}
		
		Server server = null;
		if (!Strings.isNullOrEmpty(serverUrl)) {
			server = Server.builder()
					.name(isVariableSyntax(host) ? getVariableName(host) : host)
					.scheme(getScheme(scheme))
					.host(host)
					.port((port != null) ? port : getPort(scheme))
					.build();
		}
		
		return URL.builder()
				.server(Optional.ofNullable(server))
				.rawPath(rawPath)
				.rawQuery(Optional.ofNullable(rawQuery))
				.build();
	}
		
	/**
	 * Gets the parameters from URL query (<name>=<value>&<name>=<value>).
	 * @param query the part of the URL that contains all the parameters
	 * @return list
	 */
	public static List<Parameter> getParameters(final String query) {
		final List<Parameter> urlParameters = new ArrayList<>();
		if (Strings.isNullOrEmpty(query))
			return urlParameters;
		String decodedQuery = query; 
//		try {
//			decodedQuery = URLDecoder.decode(query, "UTF-8");
//		} catch (final UnsupportedEncodingException e) {
//			logger.warn("Request Parameters are not encode in UTF-8 : " + e);
//		}
		for (String param : decodedQuery.split("&")) {
			final Parameter.Builder parameterBuilder = Parameter.builder();
			if (param.contains("=")) {
				final String[] pair = param.split("=");
				if (pair.length > 1) {
					parameterBuilder.name(pair[0]).value(pair[1]);
				} else {
					parameterBuilder.name(pair[0]).value("");
				}
			} else {
				parameterBuilder.name(param);
			}
			urlParameters.add(parameterBuilder.build());
		}
		return urlParameters;
	}
	
	public static String getEncodeUrlValue(final String syntax) {
		if (syntax != null && syntax.startsWith(FUNCTION_ENCODE_URL_START) && syntax.endsWith(FUNCTION_ENCODE_URL_END)) {
			return syntax.substring(FUNCTION_ENCODE_URL_START.length(), syntax.length() - FUNCTION_ENCODE_URL_END.length());
		}
		return syntax;
	}

	public static boolean isEncodeUrlSyntax(final String syntax) {
		if (!Strings.isNullOrEmpty(syntax) && syntax.startsWith(FUNCTION_ENCODE_URL_START) && syntax.endsWith(FUNCTION_ENCODE_URL_END)) {
			return true;
		}
		return false;
	}	

	/**
	 * Check if this method is a GET like method.
	 * @return
	 */
	public static boolean isGetLikeMethod(final Method method) {
		if (method == null) {
			return false;
		}
		switch (method) {
			case GET:
			case HEAD:
			case OPTIONS:
			case TRACE:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Check if this method is a POST like method.
	 * @return
	 */
	public static boolean isPostLikeMethod(final Method method) {
		if (method == null) {
			return false;
		}
		switch (method) {
			case POST:
			case PUT:
			case DELETE:
			case CUSTOM:
				return true;
			default:
				return false;
		}
	}

    public static boolean containFormHeader(final List<Header> headers) {
    	final Optional<Header> contentType = findHeader(headers, RequestUtils.HEADER_CONTENT_TYPE);
    	if (contentType.isPresent()) {
    		return isForm(contentType.get().getValue().orElse(null));
    	}
    	return false;
    }

    public static Optional<Header> findHeader(final List<Header> headers, final String name) {
    	return headers.stream()
    			.filter(header -> name.equals(header.getName()))
    			.findFirst();
    }

	public static boolean isBinary(final String contentType) {
		if (contentType==null) return false;

		// use startsWith to handle the "; charset=" that may be there.
		return contentType.toLowerCase().startsWith(BINARY_CONTENT_TYPE);
	}
	
	public static boolean isForm(final String contentType) {
		if (contentType==null) return false;

		// use startsWith to handle the "; charset=" that may be there.
		return contentType.toLowerCase().startsWith(FORM_CONTENT_TYPE);
	}

	private static Scheme getScheme(final String scheme) {
		return Scheme.valueOf(scheme.toUpperCase());
	}

	private static String getPort(final String scheme) {
		return getScheme(scheme) == Server.Scheme.HTTPS ? DEFAULT_HTTPS_PORT : DEFAULT_HTTP_PORT;
	}
}
