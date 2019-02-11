package com.neotys.neoload.model.v3.project;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Project.NAME, Project.SLA_PROFILES, Project.SERVERS, Project.USER_PATHS, Project.POPULATIONS, Project.SCENARIOS})

@JsonDeserialize(as = ImmutableProject.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Project {
	String NAME = "name";
	String SLA_PROFILES = "sla_profiles";
	String VARIABLES = "variables";
	String SERVERS = "servers";
	String USER_PATHS = "user_paths";
	String POPULATIONS = "populations";
	String SCENARIOS = "scenarios";	

	String DEFAULT_NAME = "MyProject";
	
	@JsonProperty(NAME)
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonIgnore
	List<Container> getSharedElements();

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

	@JsonIgnore
	Map<String,String> getProjectSettings();
	
	class Builder extends ImmutableProject.Builder {}
	static Builder builder() {
		return new Builder();
	}
}

