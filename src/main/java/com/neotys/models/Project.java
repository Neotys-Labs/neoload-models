package com.neotys.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.converters.datamodel.repository.*;
import com.neotys.models.scenario.Scenario;
import com.neotys.models.repository.Population;
import com.neotys.models.repository.Server;
import com.neotys.models.repository.UserPath;
import com.neotys.models.repository.Variable;
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

