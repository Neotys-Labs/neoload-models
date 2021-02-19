package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.GenericController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the SimpleController of JMeter into Transaction of Neoload
 */
public class SimpleControllerConverter implements BiFunction<GenericController, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerConverter.class);

    //Attributs
    private final StepConverters converter;

    //Constructor
    public SimpleControllerConverter(final StepConverters converters) {
        this.converter = converters;
    }

    //Methods
    public List<Step> apply(final GenericController simpleController, HashTree hashTree) {
        final Container.Builder builder = Container.builder().description(simpleController.getComment()).name(simpleController.getName());
        builder.addAllSteps(converter.convertStep(hashTree.get(simpleController)));
        LOGGER.info("SimpleController correctly converted");
        EventListenerUtils.readSupportedFunction("GenericController", "SimpleController");
        return ImmutableList.of(builder.build());
    }
}
