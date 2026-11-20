package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsKeystore.PATH, WsKeystore.PASSWORD})
@JsonSerialize(as = ImmutableWsKeystore.class)
@JsonDeserialize(as = ImmutableWsKeystore.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsKeystore {
	String PATH = "path";
	String PASSWORD = "password";

	@JsonProperty(PATH)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPath();

	@JsonProperty(PASSWORD)
	Optional<String> getPassword();

	class Builder extends ImmutableWsKeystore.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
