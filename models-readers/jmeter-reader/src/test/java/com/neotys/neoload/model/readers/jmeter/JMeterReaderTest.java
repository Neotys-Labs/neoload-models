package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.readers.jmeter.step.httprequest.Servers;
import com.neotys.neoload.model.readers.jmeter.step.thread.ConvertThreadGroupResult;
import com.neotys.neoload.model.readers.jmeter.step.thread.ThreadGroupConverter;
import com.neotys.neoload.model.v3.project.DependencyType;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.neotys.neoload.model.v3.project.Dependency.builder;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JMeterReaderTest {

    private TestEventListener spy;

    @Before
    public void before() {
        Servers.clear();
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        ContainerUtils.clearAll();
    }

    @After
    public void after() {
        Servers.clear();
    }

    @Test
    public void testRead() throws IOException, UpdateClassLoaderExceptionWrapper {
        JMeterReader jMeterReader = spy(new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter"));
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setRampUp(25);
        threadGroup.setNumThreads(10);
        LoopController loopController = new LoopController();
        loopController.setLoops(0);
        threadGroup.setSamplerController(loopController);
        HashTree hashTree = new HashTree();
        TestPlan testPlan = new TestPlan();

        hashTree.add(testPlan);
        hashTree.get(testPlan).add(threadGroup);


        List<Object> objectList = new ArrayList<>();
        ConstantTimer constantTimer = new ConstantTimer();
        objectList.add(constantTimer);
        hashTree.get(testPlan).get(threadGroup).add(objectList);


        doReturn(hashTree).when(jMeterReader).readJMeterProject(Mockito.any());

        List<PopulationPolicy> populationPolicyList = new ArrayList<>();

        StepConverters stepConverters = new StepConverters();
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters, threadGroup, hashTree.get(testPlan).get(threadGroup)).convert();
        Project.Builder project = Project.builder();
        Project result = jMeterReader.read();
        project.name("test");
        project.addUserPaths(convert.getUserPath());
        project.addPopulations(convert.getPopulation());
        populationPolicyList.add(convert.getPopulationPolicy());
        project.addScenarios(jMeterReader.getScenario(populationPolicyList, "", ""));
        project.addDependencies(builder()
                .name("JMeter Tool Library")
                .description("Contains some useful JMeter JS functions.")
                .type(DependencyType.JS_LIBRARY)
                .inputStream(this.getClass().getResourceAsStream("js/jmeter-1.0.js"))
                .filename("jmeter-1.0.js")
                .build());

        assertEquals(result.toString().trim(), project.build().toString().trim());
    }

    @Test
    public void testReadScript() throws IOException, UpdateClassLoaderExceptionWrapper {

        JMeterReader jMeterReader = spy(new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter"));
        List<PopulationPolicy> populationPolicyList = new ArrayList<>();

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setRampUp(25);
        threadGroup.setNumThreads(10);
        LoopController loopController = new LoopController();
        loopController.setLoops(0);
        threadGroup.setSamplerController(loopController);

        HashTree hashTree = new HashTree();
        TestPlan testPlan = new TestPlan();
        hashTree.add(testPlan);
        hashTree.get(testPlan).add(threadGroup);


        List<Object> objectList = new ArrayList<>();
        ConstantTimer constantTimer = new ConstantTimer();
        objectList.add(constantTimer);
        hashTree.get(testPlan).get(threadGroup).add(objectList);

        StepConverters stepConverters = new StepConverters();
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters, threadGroup, hashTree.get(testPlan).get(threadGroup)).convert();
        File file = mock(File.class);
        doReturn(hashTree).when(jMeterReader).readJMeterProject(file);

        Project.Builder project = Project.builder();
        Project.Builder projResult = Project.builder();
        Project result = jMeterReader.readScript(projResult, file);
        project.name("test");
        project.addPopulations(convert.getPopulation());
        project.addUserPaths(convert.getUserPath());
        populationPolicyList.add(convert.getPopulationPolicy());
        project.addScenarios(jMeterReader.getScenario(populationPolicyList, "", ""));
        project.addDependencies(builder()
                .name("JMeter Tool Library")
                .description("Contains some useful JMeter JS functions.")
                .type(DependencyType.JS_LIBRARY)
                .inputStream(this.getClass().getResourceAsStream("js/jmeter-1.0.js"))
                .filename("jmeter-1.0.js")
                .build());

        assertEquals(result.toString().trim(), project.build().toString().trim());
    }

    @Test
    public void testReadScriptError() throws IOException, UpdateClassLoaderExceptionWrapper {
        JMeterReader jMeterReader = spy(new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter"));
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setRampUp(25);
        threadGroup.setNumThreads(10);
        LoopController loopController = new LoopController();
        loopController.setLoops(0);
        threadGroup.setSamplerController(loopController);
        HashTree hashTree = new HashTree();
        ConstantTimer constantTimer = new ConstantTimer();
        hashTree.add(constantTimer);
        doReturn(hashTree).when(jMeterReader).readJMeterProject(Mockito.any());

        File file = mock(File.class);
        doReturn(hashTree).when(jMeterReader).readJMeterProject(file);

        Assertions.assertThatThrownBy(() -> jMeterReader.readScript(mock(Project.Builder.class), file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not a functionnal Script");
        verify(jMeterReader, times(1)).readJMeterProject(file);

    }

    @Test
    public void testGetScenario() {
        JMeterReader jMeterReader = new JMeterReader(spy, "/test", "test", "/jmeter");
        PopulationPolicy populationPolicy = mock(PopulationPolicy.class);
        Scenario scenario = Scenario.builder()
                .addPopulations(populationPolicy)
                .name("TestScenario")
                .description("Scenario")
                .build();

        List<PopulationPolicy> populationPolicyList = new ArrayList<>();
        populationPolicyList.add(populationPolicy);
        Scenario result = jMeterReader.getScenario(populationPolicyList, "TestScenario", "Scenario");

        assertEquals(result, scenario);
    }

    @Test
    public void testThreadGroupElementWithoutThreadGroup() {
        JMeterReader jMeterReader = new JMeterReader(spy, "/test", "test", "/jmeter");
        List<PopulationPolicy> populationPolicyList = new ArrayList<>();
        List<PopulationPolicy> populationPolicyListResult = new ArrayList<>();

        Project.Builder result = Project.builder();
        Project.Builder project = Project.builder();

        jMeterReader.convertThreadGroupElement(result, populationPolicyList, null, new ArrayList());
        assertEquals(result.build(), project.build());
        assertEquals(populationPolicyList, populationPolicyListResult);

    }

    @Test
    public void testThreadGroupElementWithThreadGroup() {
        JMeterReader jMeterReader = new JMeterReader(spy, "/test", "test", "/jmeter");
        List<PopulationPolicy> populationPolicyList = new ArrayList<>();
        List<PopulationPolicy> populationPolicyListResult = new ArrayList<>();

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setDelay(1);
        threadGroup.setRampUp(25);
        threadGroup.setNumThreads(10);
        LoopController loopController = new LoopController();
        loopController.setLoops(0);
        threadGroup.setSamplerController(loopController);

        HashTree hashTree = new HashTree();
        hashTree.add(threadGroup);
        List<Object> objectList = new ArrayList<>();
        ConstantTimer constantTimer = new ConstantTimer();
        objectList.add(constantTimer);
        hashTree.get(threadGroup).add(objectList);

        StepConverters stepConverters = new StepConverters();
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters, threadGroup, hashTree.get(threadGroup)).convert();
        Project.Builder result = Project.builder();
        Project.Builder project = Project.builder();
        project.addUserPaths(convert.getUserPath());
        project.addPopulations(convert.getPopulation());
        populationPolicyList.add(convert.getPopulationPolicy());


        jMeterReader.convertThreadGroupElement(result, populationPolicyListResult, hashTree, threadGroup);
        assertEquals(result.build(), project.build());
        assertEquals(populationPolicyList, populationPolicyListResult);

    }

    @Test
    public void testBuildProject() {
        JMeterReader jMeterReader = new JMeterReader(spy, "/test", "test", "/jmeter");
        PopulationPolicy populationPolicy = mock(PopulationPolicy.class);
        Servers.addServer("toto", "blazedemo", 8080, "HTTP", new HashTree());

        Scenario scenario = Scenario.builder()
                .addPopulations(populationPolicy)
                .name("TestScenario")
                .description("Scenario")
                .build();

        Project project = Project.builder()
                .name("test")
                .addScenarios(scenario)
                .addAllServers(Servers.getServers())
                .addDependencies(builder()
                        .name("JMeter Tool Library")
                        .description("Contains some useful JMeter JS functions.")
                        .type(DependencyType.JS_LIBRARY)
                        .inputStream(this.getClass().getResourceAsStream("js/jmeter-1.0.js"))
                        .filename("jmeter-1.0.js")
                        .build())
                .build();

        Project.Builder result = Project.builder();
        jMeterReader.buildProject(result, scenario);
        assertEquals(project.toString().trim(), result.build().toString().trim());
    }

    @Test
    public void testGetVariableSimple() {
        JMeterReader jMeterReader = new JMeterReader(spy, "/test", "test", "/jmeter");
        TestPlan testPlan = mock(TestPlan.class);
        Map<String, String> variableList = new HashMap<>();
        variableList.put("host", "localhost");
        when(testPlan.getUserDefinedVariables()).thenReturn(variableList);

        Project.Builder result = Project.builder();
        jMeterReader.getVariable(result, testPlan);

        Variable variable = ConstantVariable.builder()
                .name("host")
                .value("localhost")
                .build();
        Project expected = Project.builder().addVariables(variable).build();

        assertEquals(result.build(), expected);
    }

    @Test
    public void testGetVariableComplexe() {
        JMeterReader jMeterReader = new JMeterReader(spy, "/test", "test", "/jmeter");
        TestPlan testPlan = mock(TestPlan.class);
        Map<String, String> variableList = new HashMap<>();
        variableList.put("host", "${__property(http.server,,localhost)}");
        when(testPlan.getUserDefinedVariables()).thenReturn(variableList);

        Project.Builder result = Project.builder();
        jMeterReader.getVariable(result, testPlan);

        Variable variable = ConstantVariable.builder()
                .name("host")
                .value("localhost")
                .build();
        Project expected = Project.builder().addVariables(variable).build();

        assertEquals(result.build(), expected);

    }

}