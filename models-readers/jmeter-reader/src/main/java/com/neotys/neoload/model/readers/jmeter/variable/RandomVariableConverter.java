package com.neotys.neoload.model.readers.jmeter.variable;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.RandomVariableConfig;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the RandomVariable of JMeter into Random Variable of Neoload
 */
public class RandomVariableConverter implements BiFunction<RandomVariableConfig, HashTree, List<Variable>> {

    //Attribute
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomVariableConverter.class);

    //Constructor
    RandomVariableConverter() {
    }

    //Methods
    @Override
    public List<Variable> apply(final RandomVariableConfig randomVariableConfig, final  HashTree hashTree) {
        RandomNumberVariable.Builder randomNumberVariable = RandomNumberVariable.builder()
                .name(randomVariableConfig.getVariableName())
                .description(randomVariableConfig.getComment());

        final String  randomVariable = "RandomVariable";
        if (!randomVariableConfig.getOutputFormat().isEmpty() || !randomVariableConfig.getRandomSeed().isEmpty()){
            LOGGER.warn("We don't manage the RandomSeed and OutPutFormat parameters");
            EventListenerUtils.readUnsupportedParameter(randomVariable,"String","OutPutFormat");
            EventListenerUtils.readUnsupportedParameter(randomVariable,"String","RandomSeed");
        }

        /*
        First, we try to take the value of the different parameters,
        If there is an error, we try to check the variables and take the good one
        Finally, if there is an error again,
        Maybe the user put a variable with a function or fill the form with a wrong value
         */
        try{
            randomNumberVariable.min(Integer.parseInt(randomVariableConfig.getMinimumValue()));
        }catch (Exception e){
            try{
                randomNumberVariable.min(Integer.parseInt(ContainerUtils.getValue(randomVariableConfig.getMinimumValue())));
            }catch (Exception e1){
                LOGGER.warn("We can't manage the variable into the Min Number \n"
                        + "So we put 0 in value of Min Number", e1);
                EventListenerUtils.readUnsupportedParameter(randomVariable, "Variable String","Min");
                randomNumberVariable.min(0);
            }
        }

        try{
            randomNumberVariable.max(Integer.parseInt(randomVariableConfig.getMaximumValue()));
        }catch (Exception e){
            try{
                randomNumberVariable.max(Integer.parseInt(ContainerUtils.getValue(randomVariableConfig.getMaximumValue())));
            }catch (Exception e1){
                LOGGER.warn("We can't manage the variable into the Max Number \n"
                        + "So we put 1 in value of Max Number", e1);
                EventListenerUtils.readUnsupportedParameter(randomVariable, "Variable String","Max");
                randomNumberVariable.max(1);
            }
        }

        LOGGER.warn("We don't manage the PerThread parameters");
        EventListenerUtils.readUnsupportedParameter(randomVariable,"Boolean","PerThread");
        LOGGER.info("Convertion of RandomVariable");
        EventListenerUtils.readSupportedFunction("RandomVariable"," Random Variable");
        return ImmutableList.of(randomNumberVariable.build());
    }
}
