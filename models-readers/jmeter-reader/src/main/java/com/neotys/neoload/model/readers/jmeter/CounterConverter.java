package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.variable.CounterVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class CounterConverter implements BiFunction<CounterConfig, HashTree, List<Variable>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterConverter.class);

    CounterConverter() { }

    @Override
    public List<Variable> apply(CounterConfig counterConfig, HashTree hashTree) {
        final CounterVariable.Builder counterBuilder = CounterVariable.builder()
                .name(counterConfig.getVarName())
                .description(counterConfig.getComment())
                .increment(Integer.parseInt(counterConfig.getIncrementAsString()))
                .end(Integer.parseInt(counterConfig.getEndAsString()))
                .start(Integer.parseInt(counterConfig.getStartAsString()));
        checkOutofValue(counterConfig);
        checkScope(counterConfig,counterBuilder);
        LOGGER.info("Counter data have been cconverted");
        return ImmutableList.of(counterBuilder.build());
    }

     private void checkScope(CounterConfig counterConfig, CounterVariable.Builder counterBuilder) {
        if (counterConfig.isPerUser()){
            counterBuilder.scope(Variable.Scope.LOCAL);
        } else{
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
