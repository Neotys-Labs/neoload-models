package com.neotys.neoload.model.server;


import java.util.Optional;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableServer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Server {
	public static final String NAME = "name";
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String SCHEME = "scheme";
	
	public static final String AUTHENTICATION = "authentication";
	public static final String BASIC_AUTHENTICATION = "basic_authentication";
	public static final String NTLM_AUTHENTICATION = "ntlm_authentication";
	public static final String NEGOCIATE_AUTHENTICATION = "negotiate_authentication";
	
	public static final String DEFAULT_PORT = "80";
	public static final Scheme DEFAULT_SCHEME = Scheme.HTTP;
	
	public static final String SCHEME_HTTP = "http";
	public static final String SCHEME_HTTPS = "https";
	
	enum Scheme {
		@JsonProperty(Server.SCHEME_HTTP)
		HTTP,
		@JsonProperty(Server.SCHEME_HTTPS)
		HTTPS
	}

	@RequiredCheck(groups={NeoLoad.class})
	String getName();
	@RequiredCheck(groups={NeoLoad.class})
    String getHost();
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
    default String getPort() {
    	return DEFAULT_PORT;
    }
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
    default Scheme getScheme() {
    	return DEFAULT_SCHEME;
    }
    
    @Valid
    Optional<Authentication> getAuthentication();
    
	class Builder extends ImmutableServer.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
