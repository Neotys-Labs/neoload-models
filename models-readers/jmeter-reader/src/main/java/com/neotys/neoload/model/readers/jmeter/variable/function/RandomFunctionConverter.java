package com.neotys.neoload.model.readers.jmeter.variable.function;

import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

public class RandomFunctionConverter implements BiFunction<String, String, Variable> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomFunctionConverter.class);

    //Constructor
    public RandomFunctionConverter() {
    }

    //Methods
    @Override
    public Variable apply(final String value, final String key) {
        final String[] parameters = value.split(",");
        final RandomNumberVariable.Builder randomBuilder = RandomNumberVariable.builder()
                .name(key);
        checkMinParameters(parameters[0], randomBuilder);
        checkMaxParameters(parameters[1], randomBuilder);
        return randomBuilder.build();
    }

    private void checkMaxParameters(final String parameter, final RandomNumberVariable.Builder randomBuilder) {
        if (!parameter.isEmpty()) {
            if ("${__".equals(parameter.substring(0, 3))) {
                LOGGER.warn("We can't manage a variable function into the parameters of a variable function");
            } else
                try {
                    randomBuilder.max(Integer.parseInt(parameter));
                } catch (Exception e) {
                    try {
                        randomBuilder.max(Integer.parseInt(ContainerUtils.getValue(parameter)));
                    } catch (Exception e1) {
                        LOGGER.warn("We can't manage a variable function into the Max Number \n"
                                + "So we put 1 in value of Port Number", e1);
                        EventListenerUtils.readUnsupportedParameter("RandomFunctionVariable", "Random Variable", "Max");
                        randomBuilder.max(1);
                    }
                }
        }
    }

    private void checkMinParameters(String parameter, RandomNumberVariable.Builder randomBuilder) {
        if (!parameter.isEmpty()) {
            if ("${__".equals(parameter.substring(0, 3))) {
                LOGGER.warn("We can't manage a variable function into the parameters of a variable function");
            } else
                try {
                    randomBuilder.min(Integer.parseInt(parameter));
                } catch (Exception e) {
                    try {
                        randomBuilder.min(Integer.parseInt(ContainerUtils.getValue(parameter)));
                    } catch (Exception e1) {
                        LOGGER.warn("We can't manage a variable Function into the Min Number \n"
                                + "So we put 0 in value of Port Number", e1);
                        EventListenerUtils.readUnsupportedParameter("RandomFunctionVariable", "Random Variable", "Min");
                        randomBuilder.min(0);
                    }
                }
        }
    }
}

