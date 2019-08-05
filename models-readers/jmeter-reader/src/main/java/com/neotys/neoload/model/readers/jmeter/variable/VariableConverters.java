package com.neotys.neoload.model.readers.jmeter.variable;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.extractor.ExtractorConverters;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This class store all the Converters Class for Variable Element in JMeter
 */
public final class VariableConverters {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(VariableConverters.class);

    private final Map<Class, BiFunction<?, HashTree, List<Variable>>> convertersMap;

    //Constructor
    public VariableConverters() {
        convertersMap = ImmutableMap.of(
                CSVDataSet.class, new CSVDataSetConverter(),
                CounterConfig.class, new CounterConverter(),
                Arguments.class, new UserDefineVariableConverter());
    }

    //Methods
    @SuppressWarnings("unchecked")
    private <T> BiFunction<Object, HashTree, List<Variable>> getConverters(Class<T> clazz) {
        return (BiFunction<Object, HashTree, List<Variable>>) convertersMap.get(clazz);
    }

    public List<Variable> convertVariable(HashTree subTree) {
        //walk sub tree and convert each step
        ArrayList<Variable> list = new ArrayList<>();
        for (Object o : subTree.list()) {
            BiFunction<Object, HashTree, List<Variable>> converter = getConverters(o.getClass());
            if (converter != null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            //Check if the Jmeter element is not convert in other Converters Class
            if (!new StepConverters().getConvertersMap().containsKey(o.getClass()) && new ExtractorConverters().getConvertersMap().containsKey(o.getClass())) {
                LOGGER.error("Type not Tolerate for converted in Step ");
                EventListenerUtils.readUnsupportedFunction("StepConverters", o.getClass() + " in variable converter\n");
            }
        }
        return list;
    }

    public Map<Class, BiFunction<?, HashTree, List<Variable>>> getConvertersMap() {
        return convertersMap;
    }
}

