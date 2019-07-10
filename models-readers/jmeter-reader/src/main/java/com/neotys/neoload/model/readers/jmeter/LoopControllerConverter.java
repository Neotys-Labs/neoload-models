package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.LoopController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class LoopControllerConverter implements BiFunction<LoopController, HashTree, List<Step>> {

    private final StepConverters converter;
    private final EventListener eventListener;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoopControllerConverter.class);

    public LoopControllerConverter(StepConverters converters, EventListener eventListener) {
        this.converter = converters;
        this.eventListener = eventListener;
    }

    public List<Step> apply(LoopController loopController, HashTree hashTree) {
        LOGGER.info("LoopController corretly converted");
        eventListener.readSupportedAction("LoopController");
        Container.Builder builder = Container.builder().description(loopController.getComment()).name(loopController.getName());
        builder.addAllSteps(converter.convertStep(hashTree.get(loopController)));
        return ImmutableList.of(builder.build());
    }
}
