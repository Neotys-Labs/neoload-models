package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertionElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertionElement;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;

/**
 * An {@link HttpBasedRequest} whose response is read: extractors, assertions, redirections and response storage
 * ({@link Request}, {@link SoapRequest}, {@link WebSocketChannel}).
 */
public interface HttpBasedRequestWithResponse extends HttpBasedRequest, AssertionsElement, DurationAssertionElement, SizeAssertionElement {
	String EXTRACTORS = "extractors";
	String FOLLOW_REDIRECTS = "follow_redirects";
	String DEPRECATED_FOLLOW_REDIRECTS = "followRedirects";
	String RESPONSE_STORAGE = "response_storage";

	@JsonProperty(EXTRACTORS)
	@Valid
	List<VariableExtractor> getExtractors();

	@JsonProperty(FOLLOW_REDIRECTS)
	@JsonInclude(Include.NON_DEFAULT)
	@Value.Default
	default Boolean getFollowRedirects() {
		return false;
	}

	@JsonProperty(RESPONSE_STORAGE)
	@Valid
	Optional<ResponseStorage> getResponseStorage();
}
