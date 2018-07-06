package com.neotys.neoload.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.repository.Population;
import com.neotys.neoload.model.repository.Server;
import com.neotys.neoload.model.repository.UserPath;
import com.neotys.neoload.model.repository.Variable;
import com.neotys.neoload.model.scenario.Scenario;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonDeserialize(as = ImmutableProject.class)
public interface Project {
	String getName();
	List<UserPath> getUserPaths();
	List<Server> getServers();
	List<Variable> getVariables();
	List<Population> getPopulations();
	List<Scenario> getScenarios();
}

