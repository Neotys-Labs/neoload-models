package com.neotys.neoload.model.readers.jmeter.variable;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.v3.project.variable.CounterVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert CounterElement of JMeter into Counter Variable of Neoload
 */
public class CounterConverter implements BiFunction<CounterConfig, HashTree, List<Variable>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterConverter.class);

    //Constructor
    public CounterConverter() { //public for a stepConverterTest
    }

    //Methods
    @Override
    public List<Variable> apply(CounterConfig counterConfig, HashTree hashTree) {
        final CounterVariable.Builder counterBuilder = CounterVariable.builder()
                .name(counterConfig.getVarName())
                .description(counterConfig.getComment());

        /*
        First, we try to take the value of the different parameters,
        If there is an error, we try to check the variables and take the good one
        Finally, if there is an error again,
        Maybe the user put a variable with a function or fill the form with a wrong value
         */
        final String counterVariable = "CounterVariable";
        final String variableString = "Variable String";
        try{
            counterBuilder.increment(Integer.parseInt(counterConfig.getIncrementAsString()));
        }catch(Exception e){
            try{
                counterBuilder.increment(Integer.parseInt(ContainerUtils.getValue(counterConfig.getIncrementAsString())));
            } catch (Exception e1){
                LOGGER.warn("We can't manage the variable into the Increment Number \n"
                        + "So we put 0 in value of Port Number", e1);
                EventListenerUtils.readUnsupportedParameter(counterVariable, variableString,"Increment");

                counterBuilder.increment(0);
            }
        }

        try{
            counterBuilder.end(Integer.parseInt(counterConfig.getEndAsString()));

        }catch(Exception e){
            try{
                counterBuilder.end(Integer.parseInt(ContainerUtils.getValue(counterConfig.getEndAsString())));
            } catch (Exception e1){
                LOGGER.warn("We can't manage the variable into the End Number \n"
                        + "So we put 0 in value of End Number", e1);
                EventListenerUtils.readUnsupportedParameter(counterVariable, variableString,"End");
                counterBuilder.end(0);
            }
        }

        try{
            counterBuilder.start(Integer.parseInt(counterConfig.getStartAsString()));
        }catch(Exception e){
            try{
                counterBuilder.start(Integer.parseInt(ContainerUtils.getValue(counterConfig.getStartAsString())));
            } catch (Exception e1){
                LOGGER.warn("We can't manage the variable into the Start Number \n"
                        + "So we put 0 in value of Start Number", e1);
                EventListenerUtils.readUnsupportedParameter(counterVariable, variableString,"Start");
                counterBuilder.start(0);
            }
        }

        checkOutofValue(counterConfig);
        checkScope(counterConfig, counterBuilder);
        LOGGER.info("Counter data have been cconverted");
        EventListenerUtils.readSupportedFunction("CounterData", "Counter Variable");
        return ImmutableList.of(counterBuilder.build());
    }

    private void checkScope(CounterConfig counterConfig, CounterVariable.Builder counterBuilder) {
        if (counterConfig.isPerUser()) {
            counterBuilder.scope(Variable.Scope.LOCAL);
        } else {
            counterBuilder.scope(Variable.Scope.GLOBAL);
        }
    }

    private void checkOutofValue(CounterConfig counterConfig) {
        if (counterConfig.isResetOnThreadGroupIteration()) {
            LOGGER.warn("We can't converted this parameter");
            EventListenerUtils.readUnsupportedParameter("Counter", "Reset on each Thread Group Iteration", "not supported");
        }
    }
}
