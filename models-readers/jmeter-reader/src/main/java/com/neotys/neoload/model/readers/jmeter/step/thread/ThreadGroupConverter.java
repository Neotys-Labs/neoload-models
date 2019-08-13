package com.neotys.neoload.model.readers.jmeter.step.thread;

import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
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

/**
 * this class convert the ThreadGroup into a UserPath but in Population too
 */
public class ThreadGroupConverter {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGroupConverter.class);
    private final StepConverters stepConverters;
    private final ThreadGroup threadGroup;
    private final HashTree subTree;

    //Constructor
    public ThreadGroupConverter(final StepConverters converters, final ThreadGroup threadGroup, final HashTree subTree) {
        this.stepConverters = converters;
        this.threadGroup = threadGroup;
        this.subTree = subTree;
    }

    //Methods

    /**
     * We can optimize the walk of the HashTree because here we walk the Hashtree twice
     * So maybe create a mother class for the converter and this mother class
     * manage which converter have to be use for this element
     * @return
     */
    public ConvertThreadGroupResult convert() {
        //Create User Path
        final UserPath.Builder userPathBuilder = UserPath.builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment());
        //process subtree
        //final List<Variable> variables = variableConverters.convertVariable(subTree); // for convert the variable
        final List<Step> steps = stepConverters.convertStep(subTree); //For convert the step
        final List<Variable> variables = ContainerUtils.getVariableContainer();
        final Container containerBuilder = getContainer(steps);
        userPathBuilder.actions(containerBuilder);
        final UserPathPolicy userpolicy = getUserPathPolicy(threadGroup);
        final Population population = getPopulation(threadGroup, userpolicy);
        final PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);
        return new ConvertThreadGroupResult(userPathBuilder.build(), population, populationPolicy,variables);
    }


    static Container getContainer(final List<Step> steps) {
        LOGGER.info("Step added");
        EventListenerUtils.readSupportedFunction("Steps in ThreadGroup","Container");
        return Container.builder()
                .addAllSteps(steps)
                .build();
    }

    static UserPathPolicy getUserPathPolicy(final ThreadGroup threadGroup) {
        EventListenerUtils.readSupportedFunction("Parameters UserPath Thread","UserPathPolicy");
        LOGGER.info("UserPath converted");
        return UserPathPolicy
                .builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
    }

    static Population getPopulation(final ThreadGroup threadGroup, final UserPathPolicy userpolicy) {
        LOGGER.info("Population converted");
        EventListenerUtils.readSupportedFunction("Parameters Population Thread","Population");
        return Population
                .builder()
                .addUserPaths(userpolicy)
                .name(threadGroup.getName())
                .build();
    }


}
