package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.config.Arguments;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class UserDefineVariableConverterTest {
    private TestEventListener spy;

    @Before
    public void before() {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testConstantValue() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "valeur1");
        arguments.addArgument("variable2", "valeur2");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("valeur1")
                .description("valeur1")
                .build();

        ConstantVariable variable2 = ConstantVariable.builder()
                .name("variable2")
                .value("valeur2")
                .description("valeur2")
                .build();
        expected.add(variable1);
        expected.add(variable2);

        assertEquals(expected, result);
    }

    @Test
    public void testFunctionPLevel1() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "${__P(1,2)}");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("2")
                .description("${__P(1,2)}")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testFunctionPLevel2() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "${__P(1,${__P(2,3)})}");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("3")
                .description("${__P(1,${__P(2,3)})}")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testFunctionOtherLevel1() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "${__fn(1,2)}");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("${__fn(1,2)}")
                .description("${__fn(1,2)}")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testWithVariable() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "${__P(toto,${var})}");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("${var}")
                .description("${__P(toto,${var})}")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testComplex1() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "cp8_${__P(toto,${var})}123");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("cp8_${var}123")
                .description("cp8_${__P(toto,${var})}123")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testComplex2() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "cp8_${__P(toto,${var})}123${__P(titi,12)}555");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("cp8_${var}12312555")
                .description("cp8_${__P(toto,${var})}123${__P(titi,12)}555")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testComplex3() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "${__P(jmeterPlugin.sts.datasetDirectory,NOT_SET)}/UploadDocuments");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("NOT_SET/UploadDocuments")
                .description("${__P(jmeterPlugin.sts.datasetDirectory,NOT_SET)}/UploadDocuments")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testComplex4() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "cc8_${__P(AbbreviatedEnvironmentName,pp)}_${__time(yyyy-MM-dd_HH-mm-ss)}");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("cc8_pp_${__time(yyyy-MM-dd_HH-mm-ss)}")
                .description("cc8_${__P(AbbreviatedEnvironmentName,pp)}_${__time(yyyy-MM-dd_HH-mm-ss)}")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }

    @Test
    public void testRandom() {
        Arguments arguments = new Arguments();
        arguments.setProperty(Arguments.GUI_CLASS, "org.apache.jmeter.config.gui.ArgumentsPanel");
        arguments.addArgument("variable1", "${__Random(0,10)}");

        List<Variable> result = new UserDefineVariableConverter().apply(arguments, new HashTree());
        List<Variable> expected = new ArrayList<>();

        ConstantVariable variable1 = ConstantVariable.builder()
                .name("variable1")
                .value("${__Random(0,10)}")
                .description("${__Random(0,10)}")
                .build();

        expected.add(variable1);

        assertEquals(expected, result);
    }
}