package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Request.URL, Request.SERVER, Request.METHOD, Request.HEADERS, Request.BODY, Request.EXTRACTORS, Request.FOLLOW_REDIRECTS, SlaElement.SLA_PROFILE})
@JsonDeserialize(as = ImmutableRequest.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Request extends Step, SlaElement {
	String URL = "url";
	String SERVER = "server";
	String METHOD = "method";
	String HEADERS = "headers";
	String BODY = "body";
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
	
	@JsonProperty(EXTRACTORS)
	@Valid
	List<VariableExtractor> getExtractors();

	@JsonProperty(FOLLOW_REDIRECTS)
	@Valid
	@Value.Default
	default Boolean getFollowRedirects() { return false; }

	class Builder extends ImmutableRequest.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
