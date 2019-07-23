package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.IfController;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jmeter.timers.UniformRandomTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IfControllerConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testIfForAll(){
        IfController ifController = new IfController();
        ifController.setEvaluateAll(true);
        HashTree hashTree = new HashTree();
        ConstantTimer constantTimer = new ConstantTimer();
        hashTree.add(ifController);
        hashTree.get(ifController).add(constantTimer);

        List<Step> result = new IfControllerConverter(new StepConverters()).apply(ifController,hashTree);
        List<Step> expected = new ArrayList<>();
        If.Builder ifBuilder = If.builder()
                .name("")
                .description("")
                .then(Container.builder()
                        .addAllSteps(new StepConverters().convertStep(hashTree.get(ifController)))
                        .build());
        expected.add(ifBuilder.build());
        assertEquals(result,expected);
    }

    @Test
    public void testIfForChildrenAndWithWarnings(){
        IfController ifController = new IfController();
        ifController.setEvaluateAll(false);
        ifController.setUseExpression(true);
        HashTree hashTree = new HashTree();
        ConstantTimer constantTimer = new ConstantTimer();
        UniformRandomTimer uniformRandomTimer = new UniformRandomTimer();

        List<AbstractTestElement> ifChildren = ImmutableList.of(constantTimer, uniformRandomTimer);

        hashTree.add(ifController);
        hashTree.get(ifController).add(ifChildren);

        List<Step> result = new IfControllerConverter(new StepConverters()).apply(ifController,hashTree);
        List<Step> expected = new ArrayList<>();

        If.Builder ifBuilder = If.builder()
                .name("children " + constantTimer.getClass().getSimpleName())
                .description("")
                .then(Container.builder()
                        .addAllSteps(new StepConverters().getConverters(constantTimer.getClass()).apply(constantTimer,null))
                        .build());
        expected.add(ifBuilder.build());

        If.Builder ifBuilder2 = If.builder()
                .name("children " + uniformRandomTimer.getClass().getSimpleName())
                .description("")
                .then(Container.builder()
                        .addAllSteps(new StepConverters().getConverters(uniformRandomTimer.getClass()).apply(uniformRandomTimer,null))
                        .build());
        expected.add(ifBuilder2.build());
        assertThat(result).isEqualTo(expected);
        verify(spy,times(1)).readSupportedFunctionWithWarn("IfController","Options",0, "Variable Expression");

    }





}