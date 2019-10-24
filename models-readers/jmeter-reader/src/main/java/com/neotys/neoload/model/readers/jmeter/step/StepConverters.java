package com.neotys.neoload.model.readers.jmeter.step;

import com.google.common.collect.ImmutableMap;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.extractor.ExtractorConverters;
import com.neotys.neoload.model.readers.jmeter.step.controller.*;
import com.neotys.neoload.model.readers.jmeter.step.httpRequest.HTTPSamplerProxyConverter;
import com.neotys.neoload.model.readers.jmeter.step.httpRequest.HttpDefaultRequestConverter;
import com.neotys.neoload.model.readers.jmeter.step.timer.ConstantTimerConverter;
import com.neotys.neoload.model.readers.jmeter.step.timer.UniformerRandomTimerConverter;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.control.*;
import org.apache.jmeter.protocol.http.control.RecordingController;
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

/**
 * This class store all the Converters Class for Step Element in JMeter
 */
public final class StepConverters {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(StepConverters.class);

    private final Map<Class, BiFunction<? extends Object, HashTree, List<Step>>> convertersMap;

    //Constructor
    public StepConverters() {

        convertersMap = ImmutableMap.<Class, BiFunction<?, HashTree, List<Step>>>builder()
                .put(TransactionController.class, new TransactionControllerConverter(this))
                .put(HTTPSamplerProxy.class, new HTTPSamplerProxyConverter())
                .put(ConstantTimer.class, new ConstantTimerConverter())
                .put(GenericController.class, new SimpleControllerConverter(this))
                .put(UniformRandomTimer.class, new UniformerRandomTimerConverter())
                .put(IfController.class, new IfControllerConverter(this))
                .put(RecordingController.class, new RecordingControllerConverter(this))
                .put(ConfigTestElement.class, new HttpDefaultRequestConverter())
                .put(LoopController.class, new LoopControllerConverter(this))
                .put(WhileController.class, new WhileControllerConverter(this))
                .put(SwitchController.class, new SwitchControllerConverter(this))
                .build();
    }

    //Methods
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
                List<Step> stepList = converter.apply(o, subTree);
                if (stepList != null) {
                    list.addAll(stepList);
                }
            } else if( VariableConverters.getConvertersMap().containsKey(o.getClass())){
                VariableConverters.convertVariable(subTree,o);
            }else if (!new ExtractorConverters().getConvertersMap().containsKey(o.getClass())) {
                LOGGER.error("Type not Tolerate for converted in Step ");
                EventListenerUtils.readUnsupportedFunction("StepConverters", o.getClass() + " in step converter\n");
            }
        }
        return list;
    }

    public Map<Class, BiFunction<? extends Object, HashTree, List<Step>>> getConvertersMap() {
        return convertersMap;
    }

}
