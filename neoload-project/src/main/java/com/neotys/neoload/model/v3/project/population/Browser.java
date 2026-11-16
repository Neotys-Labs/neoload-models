package com.neotys.neoload.model.v3.project.population;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.PositiveCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * The browser emulated by the Virtual Users of a User Path in a Population, as the "Browser" and
 * "HTTP/2" columns of the designer's Population table.
 * <p>
 * The profile is stored by value, like in a NeoLoad project: {@link #getName()} is only the label shown
 * in NeoLoad. Without {@link #getUserAgent()}, the browser is the recorded one, with the other settings
 * applied to it.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Browser.NAME, Browser.USER_AGENT, Browser.CONNECTIONS, Browser.COOKIES, Browser.HTTP2, Browser.ACCEPT_ENCODING})
@JsonSerialize(as = ImmutableBrowser.class)
@JsonDeserialize(as = ImmutableBrowser.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Browser {
	String NAME = "name";
	String USER_AGENT = "user_agent";
	String CONNECTIONS = "connections";
	String COOKIES = "cookies";
	String HTTP2 = "http2";
	String ACCEPT_ENCODING = "accept_encoding";

	int DEFAULT_CONNECTIONS = 6;
	boolean DEFAULT_COOKIES = true;
	boolean DEFAULT_HTTP2 = true;

	/**
	 * The content encodings the browser accepts, named as in NeoLoad.
	 */
	enum Encoding {
		@JsonProperty("gzip") GZIP,
		@JsonProperty("xgzip") XGZIP,
		@JsonProperty("zip") ZIP,
		@JsonProperty("deflate") DEFLATE,
		@JsonProperty("deflate_rfc1951") DEFLATE_RFC1951,
		@JsonProperty("exi") EXI,
		@JsonProperty("compress") COMPRESS,
		@JsonProperty("pack200_gzip") PACK200_GZIP,
		@JsonProperty("bzip2") BZIP2,
		@JsonProperty("lzma") LZMA,
		@JsonProperty("peerdist") PEERDIST,
		@JsonProperty("xz") XZ,
		@JsonProperty("brotli") BROTLI,
		@JsonProperty("identity") IDENTITY
	}

	/**
	 * The label of the browser profile shown in NeoLoad. Empty: the profile is shown as customized
	 * (or as the recorded one without user agent).
	 */
	@JsonProperty(NAME)
	Optional<String> getName();

	/**
	 * The User-Agent sent by the browser. Empty: the one of the recording.
	 */
	@JsonProperty(USER_AGENT)
	Optional<String> getUserAgent();

	/**
	 * The maximum number of connections opened at the same time to a server.
	 */
	@JsonProperty(CONNECTIONS)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultConnectionsFilter.class)
	@PositiveCheck(unit = "connection", groups = {NeoLoad.class})
	@Value.Default
	default int getConnections() {
		return DEFAULT_CONNECTIONS;
	}

	/**
	 * Whether the browser stores and sends the cookies.
	 */
	@JsonProperty(COOKIES)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTrueFilter.class)
	@Value.Default
	default boolean getCookies() {
		return DEFAULT_COOKIES;
	}

	/**
	 * Whether the browser uses HTTP/2 when the server supports it.
	 */
	@JsonProperty(HTTP2)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultTrueFilter.class)
	@Value.Default
	default boolean getHttp2() {
		return DEFAULT_HTTP2;
	}

	/**
	 * The content encodings the browser accepts. Empty: the ones of the recording.
	 */
	@JsonProperty(ACCEPT_ENCODING)
	List<Encoding> getAcceptEncoding();

	// Jackson value filters excluding each property's default value from serialization:
	// a property is omitted when the filter's equals(value) returns true.
	class DefaultConnectionsFilter {
		@Override
		public boolean equals(final Object value) { return Integer.valueOf(DEFAULT_CONNECTIONS).equals(value); }
		@Override
		public int hashCode() { return Integer.hashCode(DEFAULT_CONNECTIONS); }
	}

	class DefaultTrueFilter {
		@Override
		public boolean equals(final Object value) { return Boolean.TRUE.equals(value); }
		@Override
		public int hashCode() { return Boolean.TRUE.hashCode(); }
	}

	class Builder extends ImmutableBrowser.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
