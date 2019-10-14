package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.SwitchController;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SwitchControllerConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testApplyWithStep(){
        StepConverters testcontrol = new StepConverters();
        SwitchController switchController = new SwitchController();
        ConstantTimer constantTimer = new ConstantTimer();
        GenericController aCase = new GenericController();
        GenericController aDefault = new GenericController();
        HashTree hashTree = new HashTree();

        switchController.setName("Switcher3"); switchController.setComment("Hunt or be hunted");
        switchController.setSelection("Geralt");
        aCase.setName("Geralt"); aDefault.setName("Default");
        constantTimer.setDelay("26081997");

        hashTree.add(switchController);
        hashTree.get(switchController).add(aCase);hashTree.get(switchController).add(constantTimer);
        hashTree.get(switchController).add(aDefault);

        hashTree.get(switchController).get(aCase).add(constantTimer); hashTree.get(switchController).get(aDefault).add(constantTimer);
        List<Step> result = testcontrol.convertStep(hashTree);
        List<Step> expected = new ArrayList<>();
        Switch aSwitch = Switch.builder()
                .name("Switcher3")
                .description("Hunt or be hunted")
                .value("Geralt")
                .getDefault(Container.builder()
                        .addSteps(Delay.builder().value("26081997").build())
                        .build())
                .addCases(Case.builder()
                        .value("Geralt")
                        .isBreak(true)
                        .build())
                .build();

        expected.add(aSwitch);

        assertEquals(result,result);

        verify(spy,times(1)).readUnsupportedAction("Element not tolerate at the first level of switch node");
    }

}