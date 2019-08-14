package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
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

/**
 * This class convert UserSefinedVariable of JMter into Variables of neoload
 */
public class UserDefineVariableConverter implements BiFunction<Arguments, HashTree, List<Variable>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDefineVariableConverter.class);

    private final VariableFunctionConverter variableFunctionConverter;

    //Constructor
    UserDefineVariableConverter(VariableFunctionConverter variableFunctionConverter) {
        this.variableFunctionConverter = variableFunctionConverter;
    }

    //Methods
    /**
     * In this method, there is not UserDefinedVariable Element in the HashTree of JMeter
     * It's a config element, so it's very too general and we have to check is the name of the gui_class is UserDefinedVariable
     * and after that, we use the same solution, to browse the properties like in CSVDataSet
     *
     * @param jMeterProperties
     * @param hashTree
     * @return
     */
    public List<Variable> apply(final Arguments jMeterProperties, final HashTree hashTree) {
        final List<Variable> variableList = new ArrayList<>();
        if (ArgumentsPanel.class.getName().equals(jMeterProperties.getPropertyAsString(Arguments.GUI_CLASS))) {
            Map<String, String> variableMap = jMeterProperties.getArgumentsAsMap();
            for(String key : variableMap.keySet()) {
                String value = variableMap.get(key);
                checkVariableFunction(value,key,variableList);
            }
        }
        LOGGER.info("The conversion of User Defined Variable is a Success");
        EventListenerUtils.readSupportedFunction("UserDefinedVariable", "Constant String variable");
        return variableList;
    }

    private void checkVariableFunction(String value , String key, List<Variable> variableList) {
        if (value.substring(0,3).contains("${__")){
            Variable valueConverted = variableFunctionConverter.convertVariableFunction(value,key);
            if(valueConverted != null){
                variableList.add(valueConverted);
                ContainerUtils.addKeyValue(key,value);
            }else{
                LOGGER.warn("We can't convert the variable function");
                EventListenerUtils.readUnsupportedAction("Convert Variable Action");
                variableList.add(ConstantVariable.builder().name(key).value(value).build());
                ContainerUtils.addKeyValue(key, value);
            }
        }else{
            variableList.add(ConstantVariable.builder().name(key).value(value).build());
            ContainerUtils.addKeyValue(key, value);
        }
    }
}
