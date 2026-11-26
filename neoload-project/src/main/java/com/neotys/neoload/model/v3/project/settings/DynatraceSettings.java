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
 * The Dynatrace integration of the {@code project_settings}.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonPropertyOrder({DynatraceSettings.ENABLED, DynatraceSettings.URL, DynatraceSettings.TOKEN})
@JsonSerialize(as = ImmutableDynatraceSettings.class)
@JsonDeserialize(as = ImmutableDynatraceSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface DynatraceSettings {
	String ENABLED = "enabled";
	String URL = "url";
	String TOKEN = "token";

	@JsonProperty(ENABLED)
	Optional<Boolean> getEnabled();

	@JsonProperty(URL)
	Optional<String> getUrl();

	/** The API token, as clear text or encrypted by NeoLoad. */
	@JsonProperty(TOKEN)
	Optional<String> getToken();

	class Builder extends ImmutableDynatraceSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
