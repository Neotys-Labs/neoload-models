package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class UserDefineVariableConverter implements BiFunction<Arguments, HashTree, List<Variable>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDefineVariableConverter.class);

    @Override
    public List<Variable> apply(Arguments jMeterProperties, HashTree hashTree) {
        List<Variable> variableList = new ArrayList<>();
            if(ArgumentsPanel.class.getName().equals(jMeterProperties.getPropertyAsString(Arguments.GUI_CLASS))){
                Map<String, String> variableMap = jMeterProperties.getArgumentsAsMap();
                variableList = variableMap.keySet().stream().map(key -> ConstantVariable.builder()
                        .name(key)
                        .value(variableMap.get(key))
                        .build()).collect(Collectors.toList());
            }
            LOGGER.info("The conversion of User Defined Variable is a Success");
        EventListenerUtils.readSupportedFunction("UserDefinedVariable", "Constant String variable");
            return variableList;
    }
}
