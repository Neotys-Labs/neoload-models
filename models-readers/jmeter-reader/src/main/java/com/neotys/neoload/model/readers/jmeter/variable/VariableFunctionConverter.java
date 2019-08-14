package com.neotys.neoload.model.readers.jmeter.variable;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.readers.jmeter.variable.function.RandomFunctionConverter;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class VariableFunctionConverter {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(VariableFunctionConverter.class);

    private  final Map<String, BiFunction<String,String,Variable>> convertersMap;

    //Constructor
    public VariableFunctionConverter() {
        convertersMap = ImmutableMap.<String, BiFunction<String,String,Variable>>builder()
                .put("Random", new RandomFunctionConverter())
                .build();
    }

     BiFunction<String,String,Variable> getConverters(final String string) {
        return  convertersMap.get(string);
    }

    Variable convertVariableFunction(final String value, final String key) {
        final String cleanValue = value.substring(4,value.length()-1); // to have only the function name and parameters
        final String nameFunction = cleanValue.split("\\(")[0];
        String parameters = cleanValue.split(nameFunction)[1];
        parameters = parameters.substring(1,parameters.length()-1); // to take off the parenthesis
        final BiFunction<String,String,Variable> converter = getConverters(nameFunction);
        if(converter != null){
            return converter.apply(parameters,key);
        }
        return null;
    }
}
