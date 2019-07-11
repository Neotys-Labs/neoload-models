package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.scenario.*;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.ThreadGroup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class PopulationPolicyConverterTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testConvertLoadIterationWithConstant(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setNumThreads(74);
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

       LoadDuration loadDuration =  LoadDuration.builder()
                .type(LoadDuration.Type.ITERATION)
                .value(Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops")))
                .build();
        LoadPolicy loadPolicy = ConstantLoadPolicy.builder()
                .users(threadGroup.getNumThreads())
                .duration(loadDuration)
                .rampup(null)
                .build();

        PopulationPolicy result = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(result,populationPolicy);
    }

    @Test
    public void testConvertLoadIterationWithRampUp(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setNumThreads(7);
        threadGroup.setRampUp(26);
        LoopController loopController = new LoopController();
        loopController.setLoops(38);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration =  LoadDuration.builder()
                .type(LoadDuration.Type.ITERATION)
                .value(Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops")))
                .build();
        LoadPolicy loadPolicy = RampupLoadPolicy.builder()
                .minUsers(1)
                .maxUsers(threadGroup.getNumThreads())
                .incrementUsers(Math.max(1, threadGroup.getRampUp() / threadGroup.getNumThreads()))
                .incrementEvery(LoadDuration.builder()
                        .value(1)
                        .type(LoadDuration.Type.ITERATION)
                        .build())
                .duration(loadDuration)
                .rampup(null)
                .build();

        PopulationPolicy result = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(result,populationPolicy);
    }

    @Test
    public void testConvertLoadIterationConstantWithDelay(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setNumThreads(42);
        threadGroup.setDelay(69);
        LoopController loopController = new LoopController();
        loopController.setLoops(73);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration =  LoadDuration.builder()
                .type(LoadDuration.Type.ITERATION)
                .value(Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops")))
                .build();
        LoadPolicy loadPolicy = ConstantLoadPolicy.builder()
                .users(threadGroup.getNumThreads())
                .duration(loadDuration)
                .rampup((int)threadGroup.getDelay())
                .build();

        PopulationPolicy result = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(result,populationPolicy);
    }

    @Test
    public void testConverterLoadRuntimeWithConstant(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(true);
        threadGroup.setNumThreads(73);
        threadGroup.setDuration(69);
        LoopController loopController = new LoopController();
        loopController.setLoops(-1);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration =  LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .value((int) threadGroup.getDuration())
                .build();
        LoadPolicy loadPolicy = ConstantLoadPolicy.builder()
                .users(threadGroup.getNumThreads())
                .duration(loadDuration)
                .rampup(null)
                .build();

        PopulationPolicy result = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(result,populationPolicy);
    }

    @Test
    public void testConverterLoadRuntimeWithRampUp(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(true);
        threadGroup.setNumThreads(73);
        threadGroup.setDuration(69);
        threadGroup.setRampUp(1);
        LoopController loopController = new LoopController();
        loopController.setLoops(-1);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration =  LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .value((int) threadGroup.getDuration())
                .build();
        LoadPolicy loadPolicy = RampupLoadPolicy.builder()
                .minUsers(1)
                .maxUsers(threadGroup.getNumThreads())
                .incrementUsers(Math.max(1, threadGroup.getRampUp() / threadGroup.getNumThreads()))
                .incrementEvery(LoadDuration.builder()
                        .value(1)
                        .type(LoadDuration.Type.ITERATION)
                        .build())
                .duration(loadDuration)
                .rampup(null)
                .build();

        PopulationPolicy result = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(result,populationPolicy);
    }

    @Test
    public void testConverterInfiniteLoop(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setNumThreads(69);
        threadGroup.setRampUp(1);
        LoopController loopController = new LoopController();
        loopController.setLoops(-1);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

        LoadPolicy loadPolicy = RampupLoadPolicy.builder()
                .minUsers(1)
                .maxUsers(threadGroup.getNumThreads())
                .incrementUsers(Math.max(1, threadGroup.getRampUp() / threadGroup.getNumThreads()))
                .incrementEvery(LoadDuration.builder()
                        .value(1)
                        .type(LoadDuration.Type.ITERATION)
                        .build())
                .rampup(null)
                .build();

        PopulationPolicy result = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(result,populationPolicy);
    }

}