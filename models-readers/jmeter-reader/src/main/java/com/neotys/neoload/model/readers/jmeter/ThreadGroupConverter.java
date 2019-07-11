package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class ThreadGroupConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupConverter.class);
    private final StepConverters stepConverters;
    private final VariableConverters variableConverters;
    private final ThreadGroup threadGroup;
    private final HashTree subTree;

    ThreadGroupConverter(StepConverters converters, ThreadGroup threadGroup, HashTree subTree,VariableConverters variableConverters) {
        this.stepConverters = converters;
        this.threadGroup = threadGroup;
        this.subTree = subTree;
        this.variableConverters = variableConverters;
    }

    ConvertThreadGroupResult convert() {
        //Create User Path
        UserPath.Builder userPathBuilder = UserPath.builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment());
        //process subtree
        final List<Step> steps = stepConverters.convertStep(subTree);
        final List<Variable> variables = variableConverters.convertVariable(subTree);
        Container containerBuilder = getContainer(steps);
        userPathBuilder.actions(containerBuilder);
        UserPathPolicy userpolicy = getUserPathPolicy(threadGroup);
        Population population = getPopulation(threadGroup, userpolicy);
        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);
        return new ConvertThreadGroupResult(userPathBuilder.build(), population, populationPolicy,variables);
    }


    static Container getContainer(List<Step> steps) {
        LOGGER.info("Step added");
        EventListenerUtils.readSupportedAction("Container");
        return Container.builder()
                .addAllSteps(steps)
                .build();
    }

    static UserPathPolicy getUserPathPolicy(ThreadGroup threadGroup) {
        EventListenerUtils.readSupportedAction("UserPathPolicy");
        LOGGER.info("UserPath converted");
        return UserPathPolicy
                .builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
    }

    static Population getPopulation(ThreadGroup threadGroup, UserPathPolicy userpolicy) {
        LOGGER.info("Population converted");
        EventListenerUtils.readSupportedAction("Population");
        return Population
                .builder()
                .addUserPaths(userpolicy)
                .name(threadGroup.getName())
                .build();
    }


}
