package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class ConstantTimerConverter implements BiFunction<ConstantTimer, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantTimerConverter.class);

    ConstantTimerConverter() {

    }

    @Override
    public List<Step> apply(ConstantTimer constantTimer, HashTree hashtree) {
        LOGGER.info("Constant timer Correctly converted");
        EventListenerUtils.readSupportedAction("ConstantTimer");
        return ImmutableList.of(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build());
    }
}
