package com.neotys.neoload.model.v3.project.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ServerDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.Optional;

@JsonInclude(value = Include.NON_EMPTY)
@JsonDeserialize(using = ServerDeserializer.class)
@JsonPropertyOrder({Server.NAME, Server.HOST, Server.PORT, Server.SCHEME})
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Server extends Element {
	String NAME = "name";
	String HOST = "hostname";
	String PORT = "port";
	String SCHEME = "scheme";

	Scheme DEFAULT_SCHEME = Scheme.HTTP;
	long DEFAULT_PORT = 80;

	enum Scheme {
		HTTP,
		HTTPS
	}

	String getName();

	String getHost();

	@Value.Default
	default Long getPort() {
		return DEFAULT_PORT;
	}

	@Value.Default
	default Scheme getScheme() {
		return DEFAULT_SCHEME;
	}

	Optional<Authentication> getAuthentication();

	class Builder extends ImmutableServer.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
