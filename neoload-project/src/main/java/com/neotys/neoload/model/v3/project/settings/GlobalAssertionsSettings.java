package com.neotys.neoload.model.v3.project.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * The responses and actions the global assertions (the content assertions of a User Path, Container or Transaction)
 * are applied to, as in the Runtime parameters of the project settings.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonPropertyOrder({GlobalAssertionsSettings.CONTENT_TYPES, GlobalAssertionsSettings.APPLY_ON_CUSTOM_ACTIONS})
@JsonSerialize(as = ImmutableGlobalAssertionsSettings.class)
@JsonDeserialize(as = ImmutableGlobalAssertionsSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface GlobalAssertionsSettings {
	String CONTENT_TYPES = "content_types";
	String APPLY_ON_CUSTOM_ACTIONS = "apply_on_custom_actions";

	/** Comma-separated content types; an empty value applies the global assertions to no response. */
	@JsonProperty(CONTENT_TYPES)
	Optional<String> getContentTypes();

	@JsonProperty(APPLY_ON_CUSTOM_ACTIONS)
	Optional<Boolean> getApplyOnCustomActions();

	class Builder extends ImmutableGlobalAssertionsSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
