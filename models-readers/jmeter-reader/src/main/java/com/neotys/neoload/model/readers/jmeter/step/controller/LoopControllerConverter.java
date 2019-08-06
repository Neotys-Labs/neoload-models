package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.LoopController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the LoopController of JMeter into Loop of Neoload
 */
public class LoopControllerConverter implements BiFunction<LoopController, HashTree, List<Step>> {

    //Attributes
    private final StepConverters converter;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoopControllerConverter.class);

    //Constructor
    public LoopControllerConverter(StepConverters converters) {
        this.converter = converters;
    }

    //Methods
    @Override
    public List<Step> apply(LoopController loopController, HashTree hashTree) {
        Loop.Builder loopBuilder = Loop.builder()
                .description(loopController.getComment())
                .name(loopController.getName())
                .times(loopController.getLoopString());

        loopBuilder.addAllSteps(converter.convertStep(hashTree.get(loopController)));
        LOGGER.info("LoopController correctly converted");
        EventListenerUtils.readSupportedFunction("LoopController", "Loop");
        return ImmutableList.of(loopBuilder.build());
    }
}
