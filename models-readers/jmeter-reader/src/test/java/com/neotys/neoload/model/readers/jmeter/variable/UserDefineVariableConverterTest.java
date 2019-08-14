package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class UserDefineVariableConverterTest {
    private TestEventListener spy;

    private VariableFunctionConverter variableFunctionConverter;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        variableFunctionConverter = new VariableFunctionConverter();
    }

    @Test
    public void testApply(){
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS,"org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1","valeur1");
        arguments.addArgument("variable2","valeur2");

        List<Variable> result = new UserDefineVariableConverter(variableFunctionConverter).apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("valeur1")
                .build();

        ConstantVariable variable2 = ConstantVariable.builder()
                .name("variable2")
                .value("valeur2")
                .build();
        expected.add(variable1);expected.add(variable2);
        assertEquals(result,expected);

     }

}