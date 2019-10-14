package com.neotys.neoload.model.readers.jmeter.step;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.readers.jmeter.variable.CSVDataSetConverter;
import com.neotys.neoload.model.readers.jmeter.variable.CSVDataSetConverterTest;
import com.neotys.neoload.model.readers.jmeter.variable.CounterConverter;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.control.SwitchController;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.css.Counter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StepConvertersTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        ContainerUtils.clearAll();
    }

    @Test
    public void testConvertStepWithObject() {
        HashTree hashTree = new HashTree();
        ConstantTimer constantTimer = new ConstantTimer();
        hashTree.add(constantTimer);
        List<Step> result = new StepConverters().convertStep(hashTree);
        List<Step> expected = new ArrayList<>();
        expected.add(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build());
        assertEquals(result,expected);
    }

    @Test
    public void testConvertStepWithVariableObject(){
        HashTree hashTree = new HashTree();
        CounterConfig counterConfig = new CounterConfig();
        counterConfig.setName("test");
        counterConfig.setVarName("Thomas");
        counterConfig.setStart("25");
        counterConfig.setIncrement("1");
        counterConfig.setEnd("26");
        hashTree.add(counterConfig);
        StepConverters stepConverters = new StepConverters();
        stepConverters.convertStep(hashTree);
        List<Variable> result = ContainerUtils.getVariableContainer();
        List<Variable> expected = (new CounterConverter().apply(counterConfig,new HashTree()));
        assertEquals(result,expected);
    }


}