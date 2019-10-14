package com.neotys.neoload.model.readers.jmeter.step.thread;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.scenario.*;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.ThreadGroup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class PopulationPolicyConverterTest {

    @Before
    public void before() {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testConverterIterationLoadDuration() {
        final int numThreads = 74;
        final int rampup = 0;
        final int delay = 69;
        final int loops = 1;

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setNumThreads(numThreads);
        threadGroup.setRampUp(rampup);
        threadGroup.setDelay(delay);
        LoopController loopController = new LoopController();
        loopController.setLoops(loops);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy actual = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration = LoadDuration.builder()
                .type(LoadDuration.Type.ITERATION)
                .value(Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops")))
                .build();
        LoadPolicy loadPolicy = ConstantLoadPolicy.builder()
                .users(threadGroup.getNumThreads())
                .duration(loadDuration)
                .rampup(rampup)
                .startAfter(StartAfter.builder().type(StartAfter.Type.TIME).value(69).build())
                .build();

        PopulationPolicy expected = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();

        assertEquals(expected, actual);
    }
    @Test
    public void testConverterTimeLoadDuration() {
        final int numThreads = 73;
        final int duration = 69;
        final int rampup = 0;
        final int delay = 0;
        final int loops = 38;

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(true);
        threadGroup.setNumThreads(numThreads);
        threadGroup.setDuration(duration);
        LoopController loopController = new LoopController();
        loopController.setLoops(loops);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy actual = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration = LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .value((int) threadGroup.getDuration())
                .build();
        LoadPolicy loadPolicy = ConstantLoadPolicy.builder()
                .users(threadGroup.getNumThreads())
                .duration(loadDuration)
                .rampup(rampup)
                .startAfter(StartAfter.builder()
                        .value(delay)
                        .type(StartAfter.Type.TIME)
                        .build())
                .build();

        PopulationPolicy expected = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void testConverterEmptyLoadDuration() {
        final int numThreads = 73;
        final int duration = 69;
        final int rampup = 1;
        final int delay = 0;
        final int loops = -1;

        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setNumThreads(numThreads);
        threadGroup.setDuration(duration);
        threadGroup.setRampUp(rampup);
        LoopController loopController = new LoopController();
        loopController.setLoops(loops);
        threadGroup.setSamplerController(loopController);

        PopulationPolicy actual = PopulationPolicyConverter.convert(threadGroup);

        LoadDuration loadDuration = LoadDuration.builder().build();

        LoadPolicy loadPolicy = ConstantLoadPolicy.builder()
                .users(numThreads)
                .duration(loadDuration)
                .rampup(rampup)
                .startAfter(StartAfter.builder()
                        .value(delay)
                        .type(StartAfter.Type.TIME)
                        .build()
                ).build();

        PopulationPolicy expected = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();

        assertEquals(expected, actual);
    }
}