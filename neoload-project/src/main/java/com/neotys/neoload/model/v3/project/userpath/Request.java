package com.neotys.neoload.model.v3.project.userpath;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.project.userpath.assertion.SizeAssertionElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_DEFAULT)
@JsonPropertyOrder({Request.NAME, Request.URL, Request.SERVER, Request.METHOD, Request.HEADERS, Request.BODY, Request.EXTRACTORS, AssertionsElement.ASSERTIONS, SizeAssertionElement.SIZE_ASSERTION, Request.FOLLOW_REDIRECTS, SlaElement.SLA_PROFILE})
@JsonSerialize(as = ImmutableRequest.class)
@JsonDeserialize(as = ImmutableRequest.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface Request extends Step, SlaElement, AssertionsElement, SizeAssertionElement {
	String NAME = "name";
	String URL = "url";
	String SERVER = "server";
	String METHOD = "method";
	String HEADERS = "headers";
	String BODY = "body";
	String BODYBINARY = "bodybinary";
	String EXTRACTORS = "extractors";
	String FOLLOW_REDIRECTS = "followRedirects";
	
	String DEFAULT_NAME = "#request#";
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

	@JsonProperty(URL)
	@RequiredCheck(groups={NeoLoad.class})
	@Pattern(regexp="^((http[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)", groups={NeoLoad.class})
	String getUrl();
	
	@JsonProperty(SERVER)
	Optional<String> getServer();
	
	@JsonProperty(METHOD)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultMethodFilter.class)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getMethod() {
		return DEFAULT_METHOD;
	}
	
	@JsonProperty(HEADERS)
	@Valid
	List<Header> getHeaders();
	
	@JsonProperty(BODY)
	Optional<String> getBody();

	@JsonProperty(BODYBINARY)
	Optional<byte[]> getBodyBinary();

	Optional<List<Part>> getParts();
	
	@JsonProperty(EXTRACTORS)
	@Valid
	List<VariableExtractor> getExtractors();
	
	@JsonProperty(FOLLOW_REDIRECTS)
	@Valid
	@Value.Default
	default Boolean getFollowRedirects() { return false; }

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
