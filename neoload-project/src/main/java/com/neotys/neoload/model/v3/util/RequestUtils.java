package com.neotys.neoload.model.v3.util;

import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTPS_PORT;
import static com.neotys.neoload.model.v3.project.server.Server.DEFAULT_HTTP_PORT;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
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



public class RequestUtils {
	private static final Pattern URL_PATTERN = Pattern.compile("^((http[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)"); // <scheme>://<host>:<port><file> | <file>
	private static final int URL_SERVER_GROUP = 1;
	private static final int URL_SCHEME_GROUP = 2;
	private static final int URL_HOST_GROUP = 3;
	private static final int URL_PORT_GROUP = 6;
	private static final int URL_FILE_GROUP = 9;
	
	private static final String FAKE_SERVER_URL = "http://host";
	
	
	private static final String NL_VARIABLE_START = "${";
	private static final String NL_VARIABLE_END = "}";
	
	static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	private RequestUtils() {}

	
	public static String unquote(String param) {
		if (param.startsWith("\"") && param.endsWith("\"")) {
			return param.substring(1, param.length() - 1);
		}
		return param;
	}
	
	public static String getVariableSyntax(final String variableName) {
		if (variableName.startsWith(NL_VARIABLE_START) && variableName.endsWith(NL_VARIABLE_END)) {
			return variableName;
		}
		return NL_VARIABLE_START + variableName + NL_VARIABLE_END;
	}
	
	public static String getVariableName(final String variableSyntax) {
		if (variableSyntax!=null && variableSyntax.startsWith(NL_VARIABLE_START) && variableSyntax.endsWith(NL_VARIABLE_END)) {
			return variableSyntax.substring(NL_VARIABLE_START.length(), variableSyntax.length() - NL_VARIABLE_END.length());
		}
		return variableSyntax;
	}
	
	public static boolean isVariableSyntax(final String variableSyntax) {
		if (!Strings.isNullOrEmpty(variableSyntax) && variableSyntax.startsWith(NL_VARIABLE_START) && variableSyntax.endsWith(NL_VARIABLE_END)) {
			return true;
		}
		return false;
	}	

	public static String normalizeName(final String name) {
		if(name == null){
			return name;
		}
		return unquote(name.trim()).replaceAll("[^a-zA-Z_0-9 \\-_\\.]","_");
	}

//	/**
//	 * Build or get the server from the list of server already build from the passed url
//	 * @param url extracting the server from this url
//	 * @return the corresponding server
//	 * @throws MalformedURLException
//	 */
//	public Server getServer(final String url) throws MalformedURLException {
//		return getServer(new URL(url));
//	}
//
//	/**
//	* Build or get the server from the list of server already build from the passed url
//	* @param url extracting the server from this url
//	* @return the corresponding server
//	*/
//	public Server getServer(final URL url) {
//		return getOrAddServerIfNotExist(
//				MethodUtils.normalizeName(url.getHost()),
//				url.getHost(),
//				String.valueOf(url.getPort() != -1 ? url.getPort() : url.getDefaultPort()),
//				Optional.of(url.getProtocol()));
//	}

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
					.name(isVariableSyntax(host) ? normalizeName(getVariableName(host)) : normalizeName(host))
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
	 * Gets the url parameters from URL query.
	 * @param query the part of the URL that contains all the parameters
	 * @return list
	 */
	public static List<Parameter> getUrlParameters(final String query) {
		final List<Parameter> urlParameters = new ArrayList<>();
		if (Strings.isNullOrEmpty(query))
			return urlParameters;
		String decodedQuery = query; 
		try {
			decodedQuery = URLDecoder.decode(query, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			logger.warn("Request Parameters are not encode in UTF-8 : " + e);
		}
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
	
	private static Scheme getScheme(final String scheme) {
		return Scheme.valueOf(scheme.toUpperCase());
	}

	private static String getPort(final String scheme) {
		return getScheme(scheme) == Server.Scheme.HTTPS ? DEFAULT_HTTPS_PORT : DEFAULT_HTTP_PORT;
	}
}
