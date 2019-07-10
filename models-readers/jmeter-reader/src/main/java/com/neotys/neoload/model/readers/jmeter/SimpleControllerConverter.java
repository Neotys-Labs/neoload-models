package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.GenericController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class SimpleControllerConverter implements BiFunction<GenericController, HashTree, List<Step>> {

        private final StepConverters converter;
        private final EventListener eventListener;
        private static final Logger LOGGER = LoggerFactory.getLogger(SimpleControllerConverter.class);

        SimpleControllerConverter(StepConverters converters, EventListener eventListener) {
            this.converter = converters;
            this.eventListener = eventListener;
        }


        public List<Step> apply(GenericController simpleController, HashTree hashTree) {
            LOGGER.info("SimpleController corretly converted");
            eventListener.readSupportedAction("SimpleController");
            Container.Builder builder = Container.builder().description(simpleController.getComment()).name(simpleController.getName());
            builder.addAllSteps(converter.convertStep(hashTree.get(simpleController)));
            return ImmutableList.of(builder.build());
        }
}
