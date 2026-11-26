package com.neotys.neoload.model.v3.project.server;


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
import com.neotys.neoload.model.v3.binding.serializer.ServerDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.ServerSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * A server of the project, referenced by name in requests. An {@code http} server (the default) is the GUI "Http
 * Server": scheme, authentications and URL rewriting. A {@code tcp} server is the GUI regular "Server": a host and a
 * port only.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonSerialize(using = ServerSerializer.class)
@JsonDeserialize(using = ServerDeserializer.class)
@JsonPropertyOrder({Server.NAME, Server.TYPE, Server.HOST, Server.SCHEME, Server.PORT, Server.AUTHENTICATIONS, Server.URL_REWRITING})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Server extends Element {
	String NAME = "name";
	String TYPE = "type";
	String HOST = "host";
	String PORT = "port";
	String SCHEME = "scheme";
	String AUTHENTICATIONS = "authentications";
	String URL_REWRITING = "url_rewriting";

	Type DEFAULT_TYPE = Type.HTTP;
	String HTTP_TYPE_VALUE = "http";
	String TCP_TYPE_VALUE = "tcp";

	Scheme DEFAULT_SCHEME = Scheme.HTTP;
	String HTTP_SCHEME_VALUE = "http";
	String HTTPS_SCHEME_VALUE = "https";
	String DEFAULT_HTTP_PORT = "80";
	String DEFAULT_HTTPS_PORT = "443";

	/** A whole number from 1 to 65535, or a variable. */
	String PORT_PATTERN = "^(([1-9][0-9]{0,3})|([1-5][0-9]{4})|(6[0-4][0-9]{3})|(65[0-4][0-9]{2})|(655[0-2][0-9])|(6553[0-5])|(\\$\\{.+\\}))$";

	enum Type {
		@JsonProperty(Server.HTTP_TYPE_VALUE)
		HTTP,
		@JsonProperty(Server.TCP_TYPE_VALUE)
		TCP
	}

	enum Scheme {
		@JsonProperty(Server.HTTP_SCHEME_VALUE)
		HTTP,
		@JsonProperty(Server.HTTPS_SCHEME_VALUE)
		HTTPS
	}

	@Value.Default
	default Type getType() {
		return DEFAULT_TYPE;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	String getHost();

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default Scheme getScheme() {
		return DEFAULT_SCHEME;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = PORT_PATTERN, groups = {NeoLoad.class}, message = "must be a port between 1 and 65535, or a variable.")
	String getPort();

	/** The authentication written with one of the {@code *_authentication} keys: at most one, Default preferred protocol. */
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	Optional<Authentication> getAuthentication();

	/** The authentications written with the {@code authentications} list: one item per row of the GUI table. */
	@Valid
	List<ServerAuthentication> getAuthentications();

	@Valid
	Optional<UrlRewriting> getUrlRewriting();

	class Builder extends ImmutableServer.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
