package com.neotys.neoload.model.readers.jmeter.step.timer;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.Step;
import java.util.List;
import java.util.function.BiFunction;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class convert the ConstantTimer of Jmeter into Delay Step of Neoload
 */
public class ConstantTimerConverter implements BiFunction<ConstantTimer, HashTree, List<Step>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantTimerConverter.class);

    //Methods
    @Override
    public List<Step> apply(final ConstantTimer constantTimer, final HashTree hashtree) {
        LOGGER.info("Constant timer Correctly converted");
        EventListenerUtils.readSupportedFunction("ConstantTimer","ConstantTimer");
        return List.of(Delay.builder().name(constantTimer.getName()).duration(DurationConstant.builder().value(TimerDuration.of(constantTimer.getDelay())).build()).build());
    }
}
