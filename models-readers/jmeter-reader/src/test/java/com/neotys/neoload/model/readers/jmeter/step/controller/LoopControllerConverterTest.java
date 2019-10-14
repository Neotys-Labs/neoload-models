package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class LoopControllerConverterTest {
    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testApplyNoSteps() {

        StepConverters testcontrol = new StepConverters();
        LoopController loopController = new LoopController();
        loopController.setName("test");
        loopController.setComment("loop");
        loopController.setLoops(25);

        HashTree hashTree = new HashTree();
        hashTree.add(loopController);

        List<Step> result = testcontrol.convertStep(hashTree);
        Loop loopBuilder = Loop.builder()
                .description(loopController.getComment())
                .name(loopController.getName())
                .count(loopController.getLoopString())
                .build();
        List<Step> expected = new ArrayList<>();
        expected.add(loopBuilder);
        assertEquals(expected,result);



    }

    @Test
    public void testAApplySteps() {

        StepConverters testcontrol = new StepConverters();
        LoopController loopController = new LoopController();
        loopController.setName("test");
        loopController.setComment("loop");
        loopController.setLoops(25);

        ConstantTimer constantTimer = new ConstantTimer();
        constantTimer.setName("constant");
        constantTimer.setDelay("3");

        HashTree hashTree = new HashTree();
        hashTree.add(loopController);
        hashTree.get(loopController).add(constantTimer);

        List<Step> result = testcontrol.convertStep(hashTree);
        Loop loopBuilder = Loop.builder()
                .description(loopController.getComment())
                .name(loopController.getName())
                .count(loopController.getLoopString())
                .addSteps(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build())
                .build();
        List<Step> expected = new ArrayList<>();
        expected.add(loopBuilder);
        assertEquals(expected,result);

    }


}