package com.neotys.neoload.model.v3.project.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * The Runtime parameters of the {@code project_settings}.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonSerialize(as = ImmutableRuntimeSettings.class)
@JsonDeserialize(as = ImmutableRuntimeSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface RuntimeSettings {
	String RESPONSES = "responses";

	@JsonProperty(RESPONSES)
	Optional<ResponsesSettings> getResponses();

	class Builder extends ImmutableRuntimeSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
