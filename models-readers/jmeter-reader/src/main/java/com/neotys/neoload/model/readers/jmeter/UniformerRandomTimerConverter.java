package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.timers.UniformRandomTimer;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class UniformerRandomTimerConverter implements BiFunction<UniformRandomTimer, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UniformerRandomTimerConverter.class);

    UniformerRandomTimerConverter() { }

    @Override
    public List<Step> apply(UniformRandomTimer uniformRandomTimer, HashTree hashTree) {
        LOGGER.info("Constant timer Correctly converted");
        EventListenerUtils.readSupportedAction("ConstantTimer");
        Delay delay = Delay.builder()
                .name(uniformRandomTimer.getName())
                .description(uniformRandomTimer.getComment())
                .value(checkDelay(uniformRandomTimer))
                .build();
        return ImmutableList.of(delay);
    }

    static String checkDelay(UniformRandomTimer uniformRandomTimer) {
        double baseDelay = 0.0;
        double randomDelay = 0.0;
        String delay = "";
        final PropertyIterator propertyIterator = uniformRandomTimer.propertyIterator();
        while (propertyIterator.hasNext()) {
            JMeterProperty jMeterProperty = propertyIterator.next();
            switch (jMeterProperty.getName()) {
                case "ConstantTimer.delay":
                    baseDelay = Double.parseDouble(jMeterProperty.getStringValue());
                    break;
                case "RandomTimer.range":
                    randomDelay = Double.parseDouble(jMeterProperty.getStringValue());
                    break;
                default:
                    LOGGER.error("UniformRandomTimer has not be created with success");
                    EventListenerUtils.readUnsupportedAction("Not Right UniformRandomTimer");
            }
        }
        delay = String.valueOf(Math.round(baseDelay + (Math.random()*randomDelay)));
        return delay;
    }
}
