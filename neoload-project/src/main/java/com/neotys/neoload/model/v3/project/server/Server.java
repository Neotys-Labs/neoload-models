package com.neotys.neoload.model.v3.project.server;


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
import com.neotys.neoload.model.v3.binding.serializer.ServerDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonDeserialize(using = ServerDeserializer.class)
@JsonPropertyOrder({Server.NAME, Server.HOST, Server.PORT, Server.SCHEME})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Server extends Element {
	String NAME = "name";
	String HOST = "host";
	String PORT = "port";
	String SCHEME = "scheme";

	Scheme DEFAULT_SCHEME = Scheme.HTTP;
	String HTTP_SCHEME_VALUE = "http";
	String HTTPS_SCHEME_VALUE = "https";
	String DEFAULT_HTTP_PORT = "80";
	String DEFAULT_HTTPS_PORT = "443";

	enum Scheme {
		@JsonProperty(Server.HTTP_SCHEME_VALUE)
		HTTP,
		@JsonProperty(Server.HTTPS_SCHEME_VALUE)
		HTTPS
	}

	@RequiredCheck(groups = {NeoLoad.class})
	String getHost();

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "^((\\d+)|(\\$\\{.+\\}))$", groups = {NeoLoad.class})
	String getPort();

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default Scheme getScheme() {
		return DEFAULT_SCHEME;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	Optional<Authentication> getAuthentication();

	class Builder extends ImmutableServer.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
