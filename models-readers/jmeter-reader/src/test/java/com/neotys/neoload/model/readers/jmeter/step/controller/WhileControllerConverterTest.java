package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.While;
import org.apache.jmeter.control.WhileController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WhileControllerConverterTest {
    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testEmptyWithConditions(){
        WhileControllerConverter whileControllerConverter = new WhileControllerConverter(new StepConverters());

        WhileController whileController = new WhileController();
        whileController.setCondition("test== success");
        HashTree hashTree = new HashTree();
        hashTree.add(whileController);

        List<Step> result = whileControllerConverter.apply(whileController,hashTree);
        List<Step> expected = new ArrayList<>();
        expected.add(While.builder()
                .name(whileController.getName())
                .description(whileController.getCondition())
                .build());
        assertEquals(expected,result);
        verify(spy,times(1)).readUnsupportedAction("Can't manage the conditions");
    }

    @Test
    public void testNotEmptyWithoutConditions(){
        WhileControllerConverter whileControllerConverter = new WhileControllerConverter(new StepConverters());

        ConstantTimer constantTimer = new ConstantTimer();
        WhileController whileController = new WhileController();
        whileController.setCondition("test== success");
        HashTree hashTree = new HashTree();
        hashTree.add(whileController);
        hashTree.get(whileController).add(constantTimer);

        List<Step> result = whileControllerConverter.apply(whileController,hashTree);
        List<Step> expected = new ArrayList<>();
        expected.add(While.builder()
                .name(whileController.getName())
                .addSteps(Delay.builder().name(constantTimer.getName()).value(constantTimer.getDelay()).build())
                .description(whileController.getCondition())
                .build());
        assertEquals(expected,result);
        verify(spy,times(1)).readSupportedFunction("WhileController","While",0);
    }
}