package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jmeter.timers.UniformRandomTimer;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

final class StepConverters {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepConverters.class);
    private final Map<Class, BiFunction<?, HashTree, List<Step>>> convertersMap;


    StepConverters() {

        convertersMap = ImmutableMap.<Class, BiFunction<?, HashTree, List<Step>>>builder()
                .put(TransactionController.class, new TransactionControllerConverter(this))
                .put(HTTPSamplerProxy.class, new HTTPSamplerProxyConverter())
                .put(ConstantTimer.class, new ConstantTimerConverter())
                .put(GenericController.class, new SimpleControllerConverter(this))
                .put(UniformRandomTimer.class, new UniformerRandomTimerConverter())
                .build();
    }

    @SuppressWarnings("unchecked")
    private <T> BiFunction<Object, HashTree, List<Step>> getConverters(Class<T> clazz) {
        return (BiFunction<Object, HashTree, List<Step>>) convertersMap.get(clazz);
    }

    List<Step> convertStep(HashTree subTree) {
        //walk sub tree and convert each step
        ArrayList<Step> list = new ArrayList<>();
        for (Object o : subTree.list()) {
            BiFunction<Object, HashTree, List<Step>> converter = getConverters(o.getClass());
            if (converter != null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            LOGGER.error("Type not Tolerate for converted in Step ");
            EventListenerUtils.readUnsupportedAction(o.getClass() + "\n");
        }
        return list;
    }

}
