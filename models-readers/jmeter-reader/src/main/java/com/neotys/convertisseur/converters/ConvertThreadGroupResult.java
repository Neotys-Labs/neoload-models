package com.neotys.convertisseur.converters;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

public class ConvertThreadGroupResult {

    UserPath userPath;
    Population population;
    PopulationPolicy populationPolicy;

    public ConvertThreadGroupResult(UserPath userPath, Population population, PopulationPolicy populationPolicy) {
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

    public Population getPopulation() {
        return population;
    }

    public UserPath getUserPath() {
        return userPath;
    }
    public PopulationPolicy getPopulationPolicy() {
        return populationPolicy;}
}
