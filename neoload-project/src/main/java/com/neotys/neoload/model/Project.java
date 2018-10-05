package com.neotys.neoload.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.repository.*;
import com.neotys.neoload.model.scenario.Scenario;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableProject.class)
public interface Project {
	Optional<String> getName();
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
	List<Scenario> getScenarios();
}

