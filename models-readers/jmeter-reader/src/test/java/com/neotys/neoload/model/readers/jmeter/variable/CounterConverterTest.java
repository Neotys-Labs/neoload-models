package com.neotys.neoload.model.readers.jmeter.variable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.CounterVariable;
import com.neotys.neoload.model.v3.project.variable.OutOfValueVariable;
import com.neotys.neoload.model.v3.project.variable.ScopeVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import java.util.ArrayList;
import java.util.List;
import org.apache.jmeter.modifiers.CounterConfig;
import org.junit.Before;
import org.junit.Test;

public class CounterConverterTest {
    private TestEventListener spy;

    @Before
    public void before()   {
         spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testApplyLocalAndWarn(){
        CounterConfig counterConfig = new CounterConfig();
        counterConfig.setIsPerUser(true);
        counterConfig.setResetOnThreadGroupIteration(true);
        counterConfig.setEnd("100");
        counterConfig.setIncrement("12");
        counterConfig.setStart("1");
        counterConfig.setVarName("Bellecour");

        List<Variable> result = new CounterConverter().apply(counterConfig,null);
        List<Variable> expected = new ArrayList<>();

        verify(spy, times(1)).readUnsupportedParameter("Counter", "Reset on each Thread Group Iteration", "not supported");

        CounterVariable.Builder counterBuilder = CounterVariable.builder()
                .name(counterConfig.getVarName())
                .description(counterConfig.getComment())
                .increment(Integer.parseInt(counterConfig.getIncrementAsString()))
                .end(Integer.parseInt(counterConfig.getEndAsString()))
                .start(Integer.parseInt(counterConfig.getStartAsString()))
                .scope(ScopeVariable.Scope.LOCAL)
                .outOfValue(OutOfValueVariable.OutOfValue.CYCLE);
        expected.add(counterBuilder.build());
        assertEquals(result, expected);
    }

    @Test
    public void testApplyGlobal(){
    CounterConfig counterConfig = new CounterConfig();
        counterConfig.setIsPerUser(false);
        counterConfig.setResetOnThreadGroupIteration(false);
        counterConfig.setEnd("100");
        counterConfig.setIncrement("12");
        counterConfig.setStart("1");
        counterConfig.setVarName("Bellecour");

    List<Variable> result = new CounterConverter().apply(counterConfig,null);
    List<Variable> expected = new ArrayList<>();

    CounterVariable.Builder counterBuilder = CounterVariable.builder()
            .name(counterConfig.getVarName())
            .description(counterConfig.getComment())
            .increment(Integer.parseInt(counterConfig.getIncrementAsString()))
            .end(Integer.parseInt(counterConfig.getEndAsString()))
            .start(Integer.parseInt(counterConfig.getStartAsString()))
            .scope(ScopeVariable.Scope.GLOBAL)
            .outOfValue(OutOfValueVariable.OutOfValue.CYCLE);
    expected.add(counterBuilder.build());
    assertEquals(result, expected);
    }

}