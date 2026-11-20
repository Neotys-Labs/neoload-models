package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.WsPartCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * A part of the SOAP message to sign or encrypt: identified either by {@code id}, or by {@code name} and an optional {@code namespace}.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsPart.ID, WsPart.NAME, WsPart.NAMESPACE, WsPart.ENCODING})
@JsonSerialize(as = ImmutableWsPart.class)
@JsonDeserialize(as = ImmutableWsPart.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@WsPartCheck(groups = {NeoLoad.class})
public interface WsPart {
	String ID = "id";
	String NAME = "name";
	String NAMESPACE = "namespace";
	String ENCODING = "encoding";

	@JsonProperty(ID)
	Optional<String> getId();

	@JsonProperty(NAME)
	Optional<String> getName();

	@JsonProperty(NAMESPACE)
	Optional<String> getNamespace();

	@JsonProperty(ENCODING)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsPartEncoding.DefaultFilter.class)
	@Value.Default
	default WsPartEncoding getEncoding() {
		return WsPartEncoding.ELEMENT;
	}

	class Builder extends ImmutableWsPart.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
