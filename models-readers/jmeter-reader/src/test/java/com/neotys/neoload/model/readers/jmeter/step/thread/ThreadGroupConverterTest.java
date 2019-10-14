package com.neotys.neoload.model.readers.jmeter.step.thread;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.*;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ThreadGroupConverterTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }
    @Test
    public void testGetContainer() {
        List<Step> steps = new ArrayList<>();
        Container result = ThreadGroupConverter.getContainer(steps);
        assertEquals(steps, result.getSteps());
        assertEquals("container", result.getName());
    }

    @Test
    public void testGetUserPathPolicy() {
        ThreadGroup threadGroup = Mockito.mock(ThreadGroup.class);
        Mockito.when(threadGroup.getName()).thenReturn("my thread group");
        Mockito.when(threadGroup.getComment()).thenReturn("My comment");
        UserPathPolicy result = ThreadGroupConverter.getUserPathPolicy(threadGroup);
        assertEquals(threadGroup.getName(),result.getName());
    }

    @Test
    public void testGetPopulation(){
        ThreadGroup threadGroup = Mockito.mock(ThreadGroup.class);
        Mockito.when(threadGroup.getName()).thenReturn("my thread group");
        Mockito.when(threadGroup.getComment()).thenReturn("My comment");
        UserPathPolicy testpolicy = ThreadGroupConverter.getUserPathPolicy(threadGroup);
        Population testpop = ThreadGroupConverter.getPopulation(threadGroup,testpolicy);
        assertEquals(threadGroup.getName(), testpop.getName());
        assertEquals(testpop.getUserPaths().get(0),testpolicy ); // vérifier que celui qu'on a ajouté en premier est bon
    }

    @Test
    public void testConvertThreadGroupUserPath() {
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
        List<Object> objectList = new ArrayList<>();
        ConstantTimer constantTimer = new ConstantTimer();
        constantTimer.setName("Delay");
        constantTimer.setDelay("25");
        objectList.add(constantTimer);

        hashTree.add(objectList);
        StepConverters stepConverters = new StepConverters();
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters,threadGroup,hashTree).convert();
        UserPath.Builder userPath = UserPath
                .builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment());
        Container container = Container.builder()
                .addSteps(Delay.builder()
                        .name(constantTimer.getName())
                        .value(constantTimer.getDelay())
                        .build())
                .build();
        userPath.actions(container);
        assertEquals(convert.getUserPath(),userPath.build());
    }


    @Test
    public void testConvertThreadGroupPopulation(){
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
        List<Object> objectList = new ArrayList<>();
        ConstantTimer constantTimer = new ConstantTimer();
        constantTimer.setName("Delay");
        constantTimer.setDelay("25");
        objectList.add(constantTimer);


        hashTree.add(objectList);

        StepConverters stepConverters = new StepConverters();
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters,threadGroup,hashTree).convert();
        UserPathPolicy.Builder userPath = UserPathPolicy
                .builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment());


        Population.Builder population = Population.builder()
                .name(threadGroup.getName())
                .addUserPaths(userPath.build())
                ;

        assertEquals(convert.getPopulation(),population.build());
    }

    @Test
    public void  testConvertThreadGroupPopulationPolicy(){
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
        List<Object> objectList = new ArrayList<>();
        ConstantTimer constantTimer = new ConstantTimer();
        constantTimer.setName("Delay");
        constantTimer.setDelay("25");
        objectList.add(constantTimer);


        hashTree.add(objectList);

        StepConverters stepConverters = new StepConverters();
        ConvertThreadGroupResult convert = new ThreadGroupConverter(stepConverters,threadGroup,hashTree).convert();
        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);
        assertEquals(convert.getPopulationPolicy(),populationPolicy);
    }



}