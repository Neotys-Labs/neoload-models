package com.neotys.neoload.model.v3.project.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.ServerDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.Optional;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(using = ServerDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Server extends Element {
	String NAME 	= "name";
	String SCHEME 	= "scheme";
	String HOST 	= "hostname";
	String PORT 	= "port";
	String AUTHENTICATION = "authentication";

	Scheme DEFAULT_SCHEME = Scheme.HTTP;
	long DEFAULT_PORT = 80;

	enum Scheme {
		HTTP,
		HTTPS
	}

	@RequiredCheck
	String getName();

	@RequiredCheck
	@Value.Default
	default Scheme getScheme() {
		return DEFAULT_SCHEME;
	}

	@RequiredCheck
    String getHost();

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
