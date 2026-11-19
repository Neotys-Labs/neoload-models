package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertionElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertionElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value=Include.NON_DEFAULT)
@JsonPropertyOrder({Request.NAME, HttpBasedRequest.URL, HttpBasedRequest.SERVER, Request.METHOD, HttpBasedRequest.PARAMETERS, HttpBasedRequest.HEADERS, Request.BODY, Request.BODYBINARY, Request.BINARY_SOURCE_FILE, Request.PARTS, HttpBasedRequestWithResponse.EXTRACTORS, AssertionsElement.CONTENT_ASSERTIONS, DurationAssertionElement.DURATION_ASSERTION, SizeAssertionElement.SIZE_ASSERTION, HttpBasedRequestWithResponse.FOLLOW_REDIRECTS, Request.CHARSET, HttpBasedRequestWithResponse.RESPONSE_STORAGE, Request.INTERNAL_RECORDED_ID, SlaElement.SLA_PROFILE})
@JsonSerialize(as = ImmutableRequest.class)
@JsonDeserialize(as = ImmutableRequest.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Request extends Step, HttpBasedRequestWithResponse, SlaElement {
	String NAME = "name";
	String METHOD = "method";
	String BODY = "body";
	String BODYBINARY = "bodybinary";
	String BINARY_SOURCE_FILE = "binary_source_file";
	String PARTS = "parts";
	String CHARSET = "charset";
	String INTERNAL_RECORDED_ID = "_internal_recorded_id";

	String DEFAULT_NAME = "request";
	String DEFAULT_METHOD = Method.GET.name();

    enum Method {
        GET,
        POST,
        HEAD,
        PUT,
        DELETE,
        OPTIONS,
        TRACE,
        CUSTOM;
    	
    	public static Method of(final String name) {
    		if (Strings.isNullOrEmpty(name)) {
    			throw new IllegalArgumentException("The parameter 'name' must not be null or empty.");
    		}
    		
    		try {
    			return Method.valueOf(name.toUpperCase());
    		}
    		catch (final IllegalArgumentException iae) {
				return Method.CUSTOM;
			}    		
    	}
    }

    @JsonProperty(NAME)
    @JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
    @RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(METHOD)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMethodFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getMethod() {
		return DEFAULT_METHOD;
	}
	
	@JsonProperty(BODY)
	Optional<String> getBody();

	@JsonProperty(BODYBINARY)
	Optional<byte[]> getBodyBinary();

	@JsonProperty(BINARY_SOURCE_FILE)
	Optional<String> getBinarySourceFile();

	@JsonProperty(PARTS)
	@Valid
	Optional<List<Part>> getParts();
	
	/**
	 * The charset used to encode the request body.
	 * <p>
	 * It only applies when the request has a body: it is dropped when there is none (see {@link #normalize()}).
	 */
	@JsonProperty(CHARSET)
	Optional<String> getCharset();

	@Value.Check
	default Request normalize() {
		return dropCharsetWithoutBody(moveUrlQueryToParameters());
	}

	private Request moveUrlQueryToParameters() {
		return hasUrlQuery() ? ImmutableRequest.copyOf(this).withUrl(urlWithoutQuery()).withParameters(parametersWithUrlQuery()) : this;
	}

	private static Request dropCharsetWithoutBody(final Request request) {
		final boolean hasBody = Method.POST.name().equalsIgnoreCase(request.getMethod()) || Method.PUT.name().equalsIgnoreCase(request.getMethod());
		return !hasBody && request.getCharset().isPresent() ? ImmutableRequest.copyOf(request).withCharset(Optional.empty()) : request;
	}

	@JsonProperty(INTERNAL_RECORDED_ID)
	Optional<String> getInternalRecordedId();

	// Jackson value filters excluding the default name / method from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	class DefaultMethodFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_METHOD.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_METHOD.hashCode();
		}
	}

	class Builder extends ImmutableRequest.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
