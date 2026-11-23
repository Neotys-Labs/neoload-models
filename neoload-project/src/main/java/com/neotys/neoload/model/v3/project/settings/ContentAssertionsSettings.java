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
 * The responses and actions the content assertions of a User Path, Container or Transaction (the global assertions)
 * are applied to, as in the Runtime parameters of the project settings.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonPropertyOrder({ContentAssertionsSettings.CONTENT_TYPES, ContentAssertionsSettings.APPLY_ON_CUSTOM_ACTIONS})
@JsonSerialize(as = ImmutableContentAssertionsSettings.class)
@JsonDeserialize(as = ImmutableContentAssertionsSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ContentAssertionsSettings {
	String CONTENT_TYPES = "content_types";
	String APPLY_ON_CUSTOM_ACTIONS = "apply_on_custom_actions";

	// NeoLoad defaults, written for a new NeoLoad project when the setting is absent. The getters stay Optional:
	// an absent setting keeps the NeoLoad value when the as-code project is applied to an existing one.
	String DEFAULT_CONTENT_TYPES = "text/html,text/xhtml";
	boolean DEFAULT_APPLY_ON_CUSTOM_ACTIONS = false;

	/** Comma-separated content types; an empty value applies the global assertions to no response. */
	@JsonProperty(CONTENT_TYPES)
	Optional<String> getContentTypes();

	@JsonProperty(APPLY_ON_CUSTOM_ACTIONS)
	Optional<Boolean> getApplyOnCustomActions();

	class Builder extends ImmutableContentAssertionsSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
