package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.timers.ConstantTimer;
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
    private final EventListener eventListener;


    StepConverters(final EventListener eventListener){

        this.eventListener = eventListener;
        convertersMap = ImmutableMap.of(
                TransactionController.class, new TransactionControllerConverter(this,eventListener),
                HTTPSamplerProxy.class, new HTTPSamplerProxyConverter(eventListener),
                ConstantTimer.class, new ConstantTimerConverter(eventListener),
                GenericController.class, new SimpleControllerConverter(this,eventListener),
                LoopController.class, new LoopControllerConverter(this,eventListener));
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
            if(converter!=null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            LOGGER.error("Type not Tolerate for converted in Step ");
            eventListener.readUnsupportedAction(o.getClass()+"\n");
        }
        return list;
    }

}
