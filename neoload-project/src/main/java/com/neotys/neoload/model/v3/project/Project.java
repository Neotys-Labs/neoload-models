package com.neotys.neoload.model.v3.project;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.project.server.Server;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.UserPath;
import com.neotys.neoload.model.repository.Variable;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableProject.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Project {
	String USER_PATHS = "user_paths";
	String ACTIONS = "actions";
	String END = "end";

	String SERVERS = "servers";
	
	String DEFAULT_NAME = "MyProject";
	
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonIgnore
	List<Container> getSharedElements();

	//	@JsonProperty(USER_PATHS)
//	@UniqueElementNameCheck(groups={NeoLoad.class})
//	@Valid
	@JsonIgnore
	List<UserPath> getUserPaths();

	@JsonProperty(SERVERS)
	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Server> getServers();

	@JsonIgnore
	List<Variable> getVariables();

	@UniqueElementNameCheck(groups={NeoLoad.class})
	@Valid
	List<Population> getPopulations();

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

