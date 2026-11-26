package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.util.RequestUtils;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

/**
 * What a step needs to send an HTTP call: {@link UrlServerElement} ({@code url}, {@code server}), {@code headers} and {@code parameters}.
 * <p>
 * A query written in the {@code url} is not kept there: an implementation normalizes itself in a
 * {@link org.immutables.value.Value.Check} that calls {@link #hasUrlQuery()}, then replaces its url by
 * {@link #urlWithoutQuery()} and its parameters by {@link #parametersWithUrlQuery()}.
 */
public interface HttpBasedRequest extends UrlServerElement {
	String HEADERS = "headers";
	String PARAMETERS = "parameters";

	@JsonProperty(HEADERS)
	@JsonInclude(Include.NON_EMPTY)
	@Valid
	List<NameValue> getHeaders();

	@JsonProperty(PARAMETERS)
	@JsonInclude(Include.NON_EMPTY)
	@Valid
	List<NameValue> getParameters();

	default boolean hasUrlQuery() {
		return getUrl() != null && getUrl().indexOf('?') >= 0 && urlQuery().isPresent();
	}

	/**
	 * The url cut at the first {@code ?} or {@code #}: the query is moved to the parameters, and a fragment is dropped
	 * because it is never sent to the server.
	 */
	default String urlWithoutQuery() {
		final String url = getUrl();
		final int fragment = url.indexOf('#');
		final int query = url.indexOf('?');
		final int end = fragment >= 0 && (query < 0 || fragment < query) ? fragment : query;
		return url.substring(0, end);
	}

	/** The parameters written in the url query, in order, followed by the declared {@code parameters}. */
	default List<NameValue> parametersWithUrlQuery() {
		return ImmutableList.<NameValue>builder()
				.addAll(RequestUtils.getParameters(urlQuery().orElse(null)))
				.addAll(getParameters())
				.build();
	}

	private Optional<String> urlQuery() {
		try {
			return Optional.of(RequestUtils.parseUrl(getUrl()).getQuery().orElse(""));
		} catch (final IllegalArgumentException e) {
			return Optional.empty();
		}
	}
}
