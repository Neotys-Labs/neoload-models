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
 * The response settings of the Runtime parameters.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonSerialize(as = ImmutableResponsesSettings.class)
@JsonDeserialize(as = ImmutableResponsesSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ResponsesSettings {
	String CONTENT_ASSERTIONS = "content_assertions";

	@JsonProperty(CONTENT_ASSERTIONS)
	Optional<ContentAssertionsSettings> getContentAssertions();

	class Builder extends ImmutableResponsesSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
