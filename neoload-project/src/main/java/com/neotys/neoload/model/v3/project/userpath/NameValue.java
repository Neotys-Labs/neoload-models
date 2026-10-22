package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.NameValueDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.NameValueSerializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * A name with an optional value, used for headers and parameters. An absent value means a
 * name-only entry ({@code name}); a present empty string means an explicit empty value
 * ({@code name=}).
 */
@JsonSerialize(using = NameValueSerializer.class)
@JsonDeserialize(using = NameValueDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface NameValue {
	@RequiredCheck(groups = {NeoLoad.class})
	String getName();

	Optional<String> getValue();

	class Builder extends ImmutableNameValue.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
