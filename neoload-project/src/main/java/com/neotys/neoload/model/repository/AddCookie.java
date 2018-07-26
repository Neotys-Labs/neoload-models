package com.neotys.neoload.model.repository;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@Value.Immutable
@JsonDeserialize(as = ImmutableAddCookie.class)
public interface AddCookie extends Element {
	String getCookieName();
	String getCookieValue();
	Server getServer();
	Optional<String> getExpires();
	Optional<String> getPath();
}
