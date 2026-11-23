package com.neotys.neoload.model.v3.project.settings;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * The qTest integration of the {@code project_settings}.
 */
@JsonInclude(value = Include.NON_ABSENT)
@JsonPropertyOrder({QTestSettings.ENABLED, QTestSettings.URL, QTestSettings.TOKEN, QTestSettings.USERNAME, QTestSettings.PASSWORD,
		QTestSettings.AUTH_METHOD, QTestSettings.MAPPING_MODE, QTestSettings.PROJECT_ID, QTestSettings.CYCLE_ID, QTestSettings.CREATE_DEFECTS})
@JsonSerialize(as = ImmutableQTestSettings.class)
@JsonDeserialize(as = ImmutableQTestSettings.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface QTestSettings {
	String ENABLED = "enabled";
	String URL = "url";
	String TOKEN = "token";
	String USERNAME = "username";
	String PASSWORD = "password";
	String AUTH_METHOD = "auth_method";
	String MAPPING_MODE = "mapping_mode";
	String PROJECT_ID = "project_id";
	String CYCLE_ID = "cycle_id";
	String CREATE_DEFECTS = "create_defects";

	// NeoLoad defaults, written for a new NeoLoad project when the setting is absent. The getters stay Optional:
	// an absent setting keeps the NeoLoad value when the as-code project is applied to an existing one.
	boolean DEFAULT_ENABLED = false;
	AuthMethod DEFAULT_AUTH_METHOD = AuthMethod.TOKEN;

	enum AuthMethod {
		TOKEN, LOGIN_PWD
	}

	enum MappingMode {
		PER_SCENARIO, PER_USER_PATH
	}

	@JsonProperty(ENABLED)
	Optional<Boolean> getEnabled();

	@JsonProperty(URL)
	Optional<String> getUrl();

	/** The API token, as clear text or encrypted by NeoLoad. */
	@JsonProperty(TOKEN)
	Optional<String> getToken();

	@JsonProperty(USERNAME)
	Optional<String> getUsername();

	/** The password, as clear text or encrypted by NeoLoad. */
	@JsonProperty(PASSWORD)
	Optional<String> getPassword();

	/** Case-insensitive, as NeoLoad read it before the typed settings. */
	@JsonProperty(AUTH_METHOD)
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
	Optional<AuthMethod> getAuthMethod();

	/** Written by NeoLoad when the results are mapped per User Path; case-insensitive. */
	@JsonProperty(MAPPING_MODE)
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
	Optional<MappingMode> getMappingMode();

	@JsonProperty(PROJECT_ID)
	Optional<Long> getProjectId();

	@JsonProperty(CYCLE_ID)
	Optional<Long> getCycleId();

	@JsonProperty(CREATE_DEFECTS)
	Optional<Boolean> getCreateDefects();

	class Builder extends ImmutableQTestSettings.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
