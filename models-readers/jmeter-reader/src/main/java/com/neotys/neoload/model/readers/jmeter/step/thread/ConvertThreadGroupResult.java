package com.neotys.neoload.model.readers.jmeter.step.thread;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.Variable;

import java.util.List;

/**
 * This class store the UserPath, Population, PopulationPolicy and variableList converted,
 * It's more easily to use them into the ThreadGroup Converter.
 */
public class ConvertThreadGroupResult {

    //Attributs
    private final UserPath userPath;
    private final Population population;
    private final PopulationPolicy populationPolicy;

    private final List<Variable> variableList;

    //Constructor
    ConvertThreadGroupResult(final UserPath userPath, final Population population, final PopulationPolicy populationPolicy, final List<Variable> variableList) {
        this.userPath = userPath;
        this.population = population;
        this.populationPolicy = populationPolicy;
        this.variableList = variableList;
    }

    //Methods
    public UserPath getUserPath() {
        return userPath;
    }

    public Population getPopulation() {
        return population;
    }

    public PopulationPolicy getPopulationPolicy() {
        return populationPolicy;
    }

    public List<Variable> getVariableList() {
        return variableList;
    }
}
