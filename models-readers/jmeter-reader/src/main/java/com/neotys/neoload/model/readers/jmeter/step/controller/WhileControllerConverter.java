package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.While;
import org.apache.jmeter.control.WhileController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the WhileController of JMeter into While of Neoload
 */
public class WhileControllerConverter implements BiFunction<WhileController, HashTree, List<Step>> {

    //Attributes
    private final StepConverters converter;
    private static final Logger LOGGER = LoggerFactory.getLogger(WhileControllerConverter.class);

    //Constructor
    public WhileControllerConverter(StepConverters converters) {
        this.converter = converters;
    }

    //Methods
    @Override
    public List<Step> apply(final WhileController whileController, HashTree hashTree) {
        final While.Builder builder = While.builder()
                .addAllSteps(converter.convertStep(hashTree.get(whileController)))
                .name(whileController.getName())
                .description(whileController.getCondition());
        if (!whileController.getCondition().isEmpty()) {
            LOGGER.warn("We can't manage the condition so we put it in the description");
            EventListenerUtils.readUnsupportedAction("Can't manage the conditions");
        }
        LOGGER.info("Convertion of WhileController");
        EventListenerUtils.readSupportedFunction("WhileController","While");
        return ImmutableList.of(builder.build());
    }
}
