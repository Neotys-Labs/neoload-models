package com.neotys.neoload.model.readers.jmeter.step;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.controller.IfControllerConverter;
import com.neotys.neoload.model.readers.jmeter.step.controller.SimpleControllerConverter;
import com.neotys.neoload.model.readers.jmeter.step.controller.TransactionControllerConverter;
import com.neotys.neoload.model.readers.jmeter.step.httpRequest.HTTPSamplerProxyConverter;
import com.neotys.neoload.model.readers.jmeter.step.timer.ConstantTimerConverter;
import com.neotys.neoload.model.readers.jmeter.step.timer.UniformerRandomTimerConverter;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.IfController;
import org.apache.jmeter.control.TransactionController;
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

public final class StepConverters {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepConverters.class);
    private final Map<Class, BiFunction<?, HashTree, List<Step>>> convertersMap;


    public StepConverters() {

        convertersMap = ImmutableMap.<Class, BiFunction<?, HashTree, List<Step>>>builder()
                .put(TransactionController.class, new TransactionControllerConverter(this))
                .put(HTTPSamplerProxy.class, new HTTPSamplerProxyConverter())
                .put(ConstantTimer.class, new ConstantTimerConverter())
                .put(GenericController.class, new SimpleControllerConverter(this))
                .put(UniformRandomTimer.class, new UniformerRandomTimerConverter())
                .put(IfController.class, new IfControllerConverter(this))
                .build();
    }

    @SuppressWarnings("unchecked")
    public <T> BiFunction<Object, HashTree, List<Step>> getConverters(Class<T> clazz) {
        return (BiFunction<Object, HashTree, List<Step>>) convertersMap.get(clazz);
    }

    public List<Step> convertStep(HashTree subTree) {
        //walk sub tree and convert each step
        ArrayList<Step> list = new ArrayList<>();
        for (Object o : subTree.list()) {
            BiFunction<Object, HashTree, List<Step>> converter = getConverters(o.getClass());
            if (converter != null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            LOGGER.error("Type not Tolerate for converted in Step ");
            EventListenerUtils.readUnsupportedFunction("StepConverters",o.getClass() + " in step converter\n");
        }
        return list;
    }

}
