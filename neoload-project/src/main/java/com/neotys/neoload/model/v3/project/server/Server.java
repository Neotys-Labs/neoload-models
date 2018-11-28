package com.neotys.neoload.model.v3.project.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.Optional;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Server.SCHEME, Server.HOST, Server.PORT, Server.AUTHENTICATION})
@JsonDeserialize(as = ImmutableServer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Server extends Element {
	String SCHEME 	= "scheme";
	String HOST 	= "hostname";
	String PORT 	= "port";
	String AUTHENTICATION = "authentication";

	String SCHEME_HTTP = "http";
	String SCHEME_HTTPS = "https";
	Scheme DEFAULT_SCHEME = Scheme.HTTP;

	long DEFAULT_PORT = 80;

	enum Scheme {
		@JsonProperty(Server.SCHEME_HTTP)
		HTTP,
		@JsonProperty(Server.SCHEME_HTTPS)
		HTTPS
	}

	@RequiredCheck(groups={NeoLoad.class})
	String getName();

	@JsonProperty(SCHEME)
	@RequiredCheck
	@Value.Default
	default Scheme getScheme() {
		return DEFAULT_SCHEME;
	}

	@JsonProperty(HOST)
	@RequiredCheck(groups={NeoLoad.class})
    String getHost();

	@JsonProperty(PORT)
//	FIXME NNA @RangeCheck(min = 1, max = 65535)
	@RequiredCheck
	@Value.Default
    default long getPort() {
    	return DEFAULT_PORT;
    }

	@RequiredCheck
	@Valid
	Optional<Authentication> getAuthentication();

	class Builder extends ImmutableServer.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
