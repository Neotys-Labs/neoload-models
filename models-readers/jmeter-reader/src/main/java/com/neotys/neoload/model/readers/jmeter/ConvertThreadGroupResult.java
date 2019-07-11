package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.Variable;

import java.util.List;

class ConvertThreadGroupResult {
        private UserPath userPath;
        private Population population;
        private PopulationPolicy populationPolicy;

    private List<Variable> variableList;

    ConvertThreadGroupResult(UserPath userPath, Population population, PopulationPolicy populationPolicy, List<Variable> variableList) {
        this.userPath = userPath;
        this.population = population;
        this.populationPolicy = populationPolicy;
        this.variableList = variableList;
    }

    UserPath getUserPath() {
        return userPath;
    }

    Population getPopulation() {
        return population;
    }

    PopulationPolicy getPopulationPolicy() {
        return populationPolicy;
    }

    List<Variable> getVariableList() {
        return variableList;
    }
}
