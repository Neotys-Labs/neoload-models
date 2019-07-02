package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.TransactionController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class TransactionControllerConverter implements BiFunction<TransactionController, HashTree, List<Step>> {

    private final Converters converter;
    private final EventListener eventListener;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionControllerConverter.class);

    TransactionControllerConverter(Converters converters, EventListener eventListener) {
        this.converter = converters;
        this.eventListener = eventListener;
    }

    @Override
    public List<Step> apply(TransactionController transactionController, HashTree hashTree) {
        LOGGER.info("ThreadGroup corretly converted");
        eventListener.readSupportedAction("ThreadGroup");
        Container.Builder builder = Container.builder().name(transactionController.getName());
        builder.addAllSteps(converter.convertStep(hashTree.get(transactionController)));
        return ImmutableList.of(builder.build());
    }
}
