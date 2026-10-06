package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;

/**
 * Settings shared by the requests sending an HTTP call to a URL ({@link Request}, {@link SoapRequest}).
 * {@code charset} only applies when the request has a body: implementations drop it otherwise.
 */
public interface HttpBasedRequest extends UrlServerElement {
	String FOLLOW_REDIRECTS = "follow_redirects";
	String DEPRECATED_FOLLOW_REDIRECTS = "followRedirects";
	String CHARSET = "charset";
	String RESPONSE_STORAGE = "response_storage";

	@JsonProperty(FOLLOW_REDIRECTS)
	@Value.Default
	default Boolean getFollowRedirects() {
		return false;
	}

	@JsonProperty(CHARSET)
	Optional<String> getCharset();

	@JsonProperty(RESPONSE_STORAGE)
	@Valid
	Optional<ResponseStorage> getResponseStorage();

	boolean hasBody();
}
