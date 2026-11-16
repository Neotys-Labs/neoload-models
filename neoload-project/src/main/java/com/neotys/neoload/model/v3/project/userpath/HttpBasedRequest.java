package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;

/**
 * Settings shared by the requests sending an HTTP call to a URL ({@link Request}, {@link SoapRequest}).
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

	/**
	 * The charset used to encode the request body.
	 * <p>
	 * It only applies when the request has a body. An implementation whose body is optional must empty it
	 * when there is no body, with a {@link Value.Check} returning a copy without charset
	 * (see {@link Request#dropCharsetWithoutBody()}).
	 */
	@JsonProperty(CHARSET)
	Optional<String> getCharset();

	@JsonProperty(RESPONSE_STORAGE)
	@Valid
	Optional<ResponseStorage> getResponseStorage();
}
