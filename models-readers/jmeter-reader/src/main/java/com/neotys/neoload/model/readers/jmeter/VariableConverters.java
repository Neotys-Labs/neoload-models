package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

    final class VariableConverters {
        private static final Logger LOGGER = LoggerFactory.getLogger(VariableConverters.class);
        private final Map<Class, BiFunction<?, HashTree, List<Variable>>> convertersMap;
        private final EventListener eventListener;


        VariableConverters(final EventListener eventListener){

            this.eventListener = eventListener;
            convertersMap = ImmutableMap.of(
                    CSVDataSet.class, new CSVDataSetConverter(eventListener));
        }

        @SuppressWarnings("unchecked")
        private <T> BiFunction<Object, HashTree, List<Variable>> getConverters(Class<T> clazz) {
            return (BiFunction<Object, HashTree, List<Variable>>) convertersMap.get(clazz);
        }

        List<Variable> convertVariable(HashTree subTree) {
            //walk sub tree and convert each step
            ArrayList<Variable> list = new ArrayList<>();
            for (Object o : subTree.list()) {
                BiFunction<Object, HashTree, List<Variable>> converter = getConverters(o.getClass());
                if(converter!=null) {
                    list.addAll(converter.apply(o, subTree));
                    continue;
                }
                LOGGER.error("Type not Tolerate for converted in Variable ");
                eventListener.readUnsupportedAction(o.getClass()+"\n");
            }
            return list;
        }

    }

