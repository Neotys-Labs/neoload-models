package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import java.util.List;
import java.util.function.BiFunction;

public class ConstantTimerConverter implements BiFunction<ConstantTimer, HashTree, List<Step>> {

    @Override
    public List<Step> apply(ConstantTimer constantTimer, HashTree hashtree) {
        System.out.println("Convert constant");
        return ImmutableList.of(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build());
    }
}
