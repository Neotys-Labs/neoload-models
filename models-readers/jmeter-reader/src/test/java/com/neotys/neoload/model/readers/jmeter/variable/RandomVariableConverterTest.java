package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.VariablesUtils;
import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.RandomVariableConfig;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RandomVariableConverterTest {
    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testWithoutProblems() {
        RandomVariableConverter randomVariableConverter = new RandomVariableConverter();
        RandomVariableConfig randomVariableConfig = new RandomVariableConfig();
        randomVariableConfig.setVariableName("test"); randomVariableConfig.setComment("best internship");
        randomVariableConfig.setMaximumValue("26"); randomVariableConfig.setMinimumValue("8");
        randomVariableConfig.setOutputFormat(""); randomVariableConfig.setRandomSeed("");
        HashTree hashTree = new HashTree();
        hashTree.add(randomVariableConfig);
        List<Variable> result = randomVariableConverter.apply(randomVariableConfig,hashTree);
        List<Variable> expected = new ArrayList<>();
        expected.add(RandomNumberVariable.builder()
                .name(randomVariableConfig.getVariableName())
                .description(randomVariableConfig.getComment())
                .min(Integer.parseInt(randomVariableConfig.getMinimumValue()))
                .max(Integer.parseInt(randomVariableConfig.getMaximumValue()))
                .build());
        assertEquals(expected,result);
    }

    @Test
    public void testWithGoodVariable(){
        new VariablesUtils();
        VariablesUtils.addList("max","26");VariablesUtils.addList("min","8");
        RandomVariableConverter randomVariableConverter = new RandomVariableConverter();
        RandomVariableConfig randomVariableConfig = new RandomVariableConfig();
        randomVariableConfig.setVariableName("test"); randomVariableConfig.setComment("best internship");
        randomVariableConfig.setMaximumValue("${max}"); randomVariableConfig.setMinimumValue("${min}");
        randomVariableConfig.setOutputFormat(""); randomVariableConfig.setRandomSeed("");
        HashTree hashTree = new HashTree();
        hashTree.add(randomVariableConfig);
        List<Variable> result = randomVariableConverter.apply(randomVariableConfig,hashTree);
        List<Variable> expected = new ArrayList<>();
        expected.add(RandomNumberVariable.builder()
                .name(randomVariableConfig.getVariableName())
                .description(randomVariableConfig.getComment())
                .min(8)
                .max(26)
                .build());
        assertEquals(expected,result);
    }

    @Test
    public void testWithProblems(){
        final String  randomVariable = "RandomVariable";
        new VariablesUtils();
        VariablesUtils.addList("max","26");VariablesUtils.addList("min","8");
        RandomVariableConverter randomVariableConverter = new RandomVariableConverter();
        RandomVariableConfig randomVariableConfig = new RandomVariableConfig();
        randomVariableConfig.setVariableName("test"); randomVariableConfig.setComment("best internship");
        randomVariableConfig.setMaximumValue("$__P{max,1}"); randomVariableConfig.setMinimumValue("$__P{min,1}");
        randomVariableConfig.setOutputFormat("df"); randomVariableConfig.setRandomSeed("fd");
        HashTree hashTree = new HashTree();
        hashTree.add(randomVariableConfig);
        List<Variable> result = randomVariableConverter.apply(randomVariableConfig,hashTree);
        List<Variable> expected = new ArrayList<>();
        expected.add(RandomNumberVariable.builder()
                .name(randomVariableConfig.getVariableName())
                .description(randomVariableConfig.getComment())
                .min(0)
                .max(1)
                .build());
        assertEquals(expected,result);
        verify(spy,times(1)).readUnsupportedParameter(randomVariable, "Variable String","Min");
        verify(spy,times(1)).readUnsupportedParameter(randomVariable, "Variable String","Max");
        verify(spy,times(1)).readUnsupportedParameter(randomVariable,"String","RandomSeed");
        verify(spy,times(1)).readUnsupportedParameter(randomVariable,"String","OutPutFormat");
    }
}