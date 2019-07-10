package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JMeterReaderTest {

    @Before
    public void before() {
        Servers.clear();
    }

    @After
    public void after() {
        Servers.clear();
    }

    @Test
    public void testRead() throws IOException {
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

        StepConverters stepConverters = new StepConverters(mock(EventListener.class));
        VariableConverters variableConverters = new VariableConverters((mock(EventListener.class)));
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters,threadGroup,hashTree.get(testPlan).get(threadGroup),variableConverters).convert();
        Project.Builder project = Project.builder();
        Project result = jMeterReader.read();
        project.name("test");
        project.addUserPaths(convert.getUserPath());
        project.addPopulations(convert.getPopulation());
        populationPolicyList.add(convert.getPopulationPolicy());
        project.addScenarios(jMeterReader.getScenario(populationPolicyList,"",""));

        assertEquals(result, project.build());
    }

    @Test
    public void testReadScript() throws IOException {

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

        StepConverters stepConverters = new StepConverters(mock(EventListener.class));
        VariableConverters variableConverters = new VariableConverters((mock(EventListener.class)));
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters,threadGroup,hashTree.get(testPlan).get(threadGroup),variableConverters).convert();
        File file = mock(File.class);
        doReturn(hashTree).when(jMeterReader).readJMeterProject(file);

        Project.Builder project = Project.builder();
        Project.Builder projResult = Project.builder();
        ImmutableProject result = jMeterReader.readScript(projResult, file);
        project.name("test");
        project.addUserPaths(convert.getUserPath());
        project.addPopulations(convert.getPopulation());
        populationPolicyList.add(convert.getPopulationPolicy());
        project.addScenarios(jMeterReader.getScenario(populationPolicyList,"",""));

        assertEquals(result, project.build());


    }

    @Test
    public void testReadScriptError() throws IOException {
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

        Assertions.assertThatThrownBy(()  -> jMeterReader.readScript(mock(Project.Builder.class),file))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Not a functionnal Script");
        verify(jMeterReader, times(1)).readJMeterProject(file);

    }

    @Test
    public void testGetScenario() {
        JMeterReader jMeterReader = new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter");
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
        JMeterReader jMeterReader = new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter");
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
        JMeterReader jMeterReader = new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter");
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

        StepConverters stepConverters = new StepConverters(mock(EventListener.class));
        VariableConverters variableConverters = new VariableConverters((mock(EventListener.class)));
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters,threadGroup,hashTree.get(threadGroup),variableConverters).convert();
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
        JMeterReader jMeterReader = new JMeterReader(mock(EventListener.class), "/test", "test", "/jmeter");
        PopulationPolicy populationPolicy = mock(PopulationPolicy.class);
        Servers.addServer("blazedemo", 8080, "HTTP");

        Scenario scenario = Scenario.builder()
                .addPopulations(populationPolicy)
                .name("TestScenario")
                .description("Scenario")
                .build();

        Project project = Project.builder()
                .name("test")
                .addScenarios(scenario)
                .addAllServers(Servers.getServers())
                .build();

        Project.Builder result = Project.builder();
        jMeterReader.buildProject(result, scenario);
        assertEquals(project, result.build());
    }

}