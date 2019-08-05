package com.neotys.neoload.model.readers.jmeter.extractor;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import com.neotys.neoload.model.v3.project.userpath.VariableExtractor;
import org.apache.jmeter.extractor.BoundaryExtractor;
import org.apache.jmeter.extractor.RegexExtractor;
import org.apache.jmeter.extractor.XPath2Extractor;
import org.apache.jmeter.extractor.XPathExtractor;
import org.apache.jmeter.extractor.json.jsonpath.JSONPostProcessor;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This class store all the Converters Class for Extractor Element in JMeter
 */
public class ExtractorConverters {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorConverters.class);

    private final Map<Class, BiFunction<?, HashTree, List<VariableExtractor>>> convertersMap;

    //Constructor
    public ExtractorConverters() {
        convertersMap = ImmutableMap.of(
                RegexExtractor.class, new RegularExtractorConverter(),
                BoundaryExtractor.class, new BoundaryExtractorConverter(),
                XPathExtractor.class, new XPathExtractorConverter(),
                XPath2Extractor.class, new XPathExtractorConverter2(),
                JSONPostProcessor.class, new JSONExtractorConverter());
    }

    //Methods
    @SuppressWarnings("unchecked")
    private <T> BiFunction<Object, HashTree, List<VariableExtractor>> getConverters(Class<T> clazz) {
        return (BiFunction<Object, HashTree, List<VariableExtractor>>) convertersMap.get(clazz);
    }

    public List<VariableExtractor> convertParameter(HashTree subTree) {
        //walk sub tree and convert each step
        List<VariableExtractor> list = new ArrayList<>();
        for (Object o : subTree.list()) {
            BiFunction<Object, HashTree, List<VariableExtractor>> converter = getConverters(o.getClass());
            if (converter != null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            //Check if the Jmeter element is not convert in other Converters Class
            if (!new StepConverters().getConvertersMap().containsKey(o.getClass()) && new VariableConverters().getConvertersMap().containsKey(o.getClass())) {
                LOGGER.error("Type not Tolerate for converted in Variable ");
                EventListenerUtils.readUnsupportedFunction("Extractor Converter", o.getClass() + " in variable extractor\n");
            }
        }
        return list;
    }


    public Map<Class, BiFunction<?, HashTree, List<VariableExtractor>>> getConvertersMap() {
        return convertersMap;
    }


}
