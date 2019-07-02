package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

public class ConvertThreadGroupResult {

    private UserPath userPath;
    private Population population;
    private PopulationPolicy populationPolicy;

    ConvertThreadGroupResult(UserPath userPath, Population population, PopulationPolicy populationPolicy) {
        this.userPath = userPath;
        this.population = population;
        this.populationPolicy = populationPolicy;
    }

    @Override
    public String toString() {
        return "ConvertThreadGroupResult{" +
                "userPath=" + userPath +
                ", population=" + population +
                '}';
    }

    Population getPopulation() {
        return population;
    }

    UserPath getUserPath() {
        return userPath;
    }

    PopulationPolicy getPopulationPolicy() {
        return populationPolicy;}
}
