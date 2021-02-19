package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.TransactionController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the TransactionController of JMeter into Transaction of Neoload
 */
public class TransactionControllerConverter implements BiFunction<TransactionController, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionControllerConverter.class);

    //Attributs
    private final StepConverters converter;

    //Constructor
    public TransactionControllerConverter(final StepConverters converters) {
        this.converter = converters;
    }

    //Methods
    @Override
    public List<Step> apply(final TransactionController transactionController, HashTree hashTree) {
        final Container.Builder builder = Container.builder().description(transactionController.getComment()).name(transactionController.getName());
        builder.addAllSteps(converter.convertStep(hashTree.get(transactionController)));
        LOGGER.info("TransactionController correctly converted");
        EventListenerUtils.readSupportedFunction("Transaction Controller","TransactionController");
        return ImmutableList.of(builder.build());
    }


}
