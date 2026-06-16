package com.neotys.neoload.model.v3.project;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.FrameworksDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.FrameworksSerializer;
import com.neotys.neoload.model.v3.project.framework.Framework;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.v3.validation.constraints.ValidSchemaVersion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Project.SCHEMA_VERSION, Project.NAME, Project.SLA_PROFILES, Project.SERVERS, Project.USER_PATHS, Project.POPULATIONS, Project.SCENARIOS, Project.FRAMEWORKS, Project.PROJECT_SETTINGS})

@JsonDeserialize(as = ImmutableProject.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Project {
	String SCHEMA_VERSION = "schemaVersion";
	String DEFAULT_SCHEMA_VERSION = "3.0";
	String NAME = "name";
	String SLA_PROFILES = "sla_profiles";
	String VARIABLES = "variables";
	String SERVERS = "servers";
	String USER_PATHS = "user_paths";
	String POPULATIONS = "populations";
	String SCENARIOS = "scenarios";
	String FRAMEWORKS = "frameworks";
	String PROJECT_SETTINGS = "project_settings";

	@JsonProperty(SCHEMA_VERSION)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SchemaVersionDefaultFilter.class)
	@ValidSchemaVersion(groups = {NeoLoad.class})
	@Value.Default
	default String getSchemaVersion() {
		return DEFAULT_SCHEMA_VERSION;
	}

	@JsonProperty(NAME)
	Optional<String> getName();

	@JsonProperty(SLA_PROFILES)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<SlaProfile> getSlaProfiles();
	
	@JsonProperty(VARIABLES)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Variable> getVariables();

	@JsonProperty(SERVERS)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Server> getServers();

	@JsonProperty(USER_PATHS)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<UserPath> getUserPaths();

	@JsonProperty(POPULATIONS)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Population> getPopulations();
	
	@JsonProperty(SCENARIOS)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Scenario> getScenarios();

	@JsonProperty(FRAMEWORKS)
	@JsonSerialize(using = FrameworksSerializer.class)
	@JsonDeserialize(using = FrameworksDeserializer.class)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Framework> getFrameworks();

	@JsonProperty(PROJECT_SETTINGS)
	@Valid
	Map<String,String> getProjectSettings();

	@JsonIgnore
	List<Dependency> getDependencies();

	class Builder extends ImmutableProject.Builder {}
	static Builder builder() {
		return new Builder();
	}
}

