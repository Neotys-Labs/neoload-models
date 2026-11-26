package com.neotys.neoload.model.v3.project.server;

import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * The "HTTP URL rewriting" settings of an http server: NeoLoad carries the session id the server puts in the URLs
 * (for example {@code jsessionid}) to the next requests. A server with this block has URL rewriting enabled.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonSerialize(as = ImmutableUrlRewriting.class)
@JsonDeserialize(as = ImmutableUrlRewriting.class)
@JsonPropertyOrder({UrlRewriting.SESSION_ARGUMENT, UrlRewriting.IN_PATH, UrlRewriting.PATH_SEPARATOR, UrlRewriting.EQUALS_SIGN})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UrlRewriting {
	String SESSION_ARGUMENT = "session_argument";
	String IN_PATH = "in_path";
	String PATH_SEPARATOR = "path_separator";
	String EQUALS_SIGN = "equals_sign";

	String DEFAULT_PATH_SEPARATOR = ";";

	/** The name of the session argument, for example {@code jsessionid}. */
	@JsonProperty(SESSION_ARGUMENT)
	@RequiredCheck(groups = {NeoLoad.class})
	String getSessionArgument();

	/** {@code true} when the session id is in the path ({@code /a;jsessionid=X}), {@code false} when it is a parameter. */
	@JsonProperty(IN_PATH)
	Optional<Boolean> getInPath();

	/** The separator in front of the session argument in the path. Only with {@code in_path}. */
	@JsonProperty(PATH_SEPARATOR)
	Optional<String> getPathSeparator();

	/** {@code false} when there is no "=" between the session argument and its value. Only with {@code in_path}. */
	@JsonProperty(EQUALS_SIGN)
	Optional<Boolean> getEqualsSign();

	@JsonIgnore
	default boolean isInPath() {
		return getInPath().orElse(false);
	}

	@JsonIgnore
	default String pathSeparator() {
		return getPathSeparator().orElse(DEFAULT_PATH_SEPARATOR);
	}

	@JsonIgnore
	default boolean isEqualsSign() {
		return getEqualsSign().orElse(true);
	}

	/** Default values are not written back: not in path, ";" separator, equals sign. */
	@Value.Check
	default UrlRewriting dropDefaultValues() {
		final boolean defaultInPath = getInPath().filter(Boolean.FALSE::equals).isPresent();
		final boolean defaultSeparator = getPathSeparator().filter(DEFAULT_PATH_SEPARATOR::equals).isPresent();
		final boolean defaultEqualsSign = getEqualsSign().filter(Boolean.TRUE::equals).isPresent();
		if (!defaultInPath && !defaultSeparator && !defaultEqualsSign) {
			return this;
		}
		return ImmutableUrlRewriting.copyOf(this)
				.withInPath(defaultInPath ? Optional.empty() : getInPath())
				.withPathSeparator(defaultSeparator ? Optional.empty() : getPathSeparator())
				.withEqualsSign(defaultEqualsSign ? Optional.empty() : getEqualsSign());
	}

	class Builder extends ImmutableUrlRewriting.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
