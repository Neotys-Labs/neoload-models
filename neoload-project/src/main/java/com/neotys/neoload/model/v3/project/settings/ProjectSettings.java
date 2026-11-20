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
 * The {@code project_settings} of a project: each setting overrides the one of the NeoLoad project, an absent
 * setting keeps the NeoLoad value. The deprecated dotted keys ({@code qtest.enabled: true}) are rewritten to the
 * nested form before binding, by {@code DeprecatedKeys}.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonPropertyOrder({ProjectSettings.DYNATRACE, ProjectSettings.QTEST, ProjectSettings.RUNTIME})
@JsonSerialize(as = ImmutableProjectSettings.class)
@JsonDeserialize(as = ImmutableProjectSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface ProjectSettings {
	String DYNATRACE = "dynatrace";
	String QTEST = "qtest";
	String RUNTIME = "runtime";

	@JsonProperty(DYNATRACE)
	Optional<DynatraceSettings> getDynatrace();

	@JsonProperty(QTEST)
	Optional<QTestSettings> getQtest();

	@JsonProperty(RUNTIME)
	Optional<RuntimeSettings> getRuntime();

	class Builder extends ImmutableProjectSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
