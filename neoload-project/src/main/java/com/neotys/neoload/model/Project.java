package com.neotys.neoload.model;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.Population;
import com.neotys.neoload.model.repository.Server;
import com.neotys.neoload.model.repository.UserPath;
import com.neotys.neoload.model.repository.Variable;
import com.neotys.neoload.model.scenario.Scenario;
import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.constraints.UniqueElementNameCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableProject.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
public interface Project {
	String DEFAULT_NAME = "MyProject";
	
	@RequiredCheck(groups={NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}
	@JsonIgnore
	List<Container> getSharedElements();
	@JsonIgnore
	List<UserPath> getUserPaths();
	@JsonIgnore
	List<Server> getServers();
	@JsonIgnore
	List<Variable> getVariables();
	@JsonIgnore
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

