package com.neotys.neoload.model.v3.util;

import java.util.Optional;

import org.immutables.value.Value;

import com.neotys.neoload.model.v3.project.server.Server;

@Value.Immutable
public interface URL {
	Optional<Server> getServer();
	String getRawPath();
	Optional<String> getRawQuery();
	
	class Builder extends ImmutableURL.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
