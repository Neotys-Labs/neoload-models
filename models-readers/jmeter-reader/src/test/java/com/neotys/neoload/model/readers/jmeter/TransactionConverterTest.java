package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.CmdEventListener;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.assertj.core.api.Assertions;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

public class TransactionConverterTest {

    @Test
    public void testApplyNoSteps() {
        CmdEventListener eventListener = new CmdEventListener("", "", "");
        TransactionControllerConverter testcontrol = new TransactionControllerConverter(new Converters(eventListener)
                , eventListener);

        TransactionController transactionController = Mockito.mock(TransactionController.class);
        when(transactionController.getName()).thenReturn("my thread group");
        when(transactionController.getComment()).thenReturn("My comment");
        HashTree hashTree = Mockito.mock(HashTree.class);

        HashTree subTree = Mockito.mock(HashTree.class);
        when(subTree.list()).thenReturn(Collections.emptyList());
        when(hashTree.get(eq(transactionController))).thenReturn(subTree);
        List<Step> teststep = testcontrol.apply(transactionController, hashTree);
        assertEquals(teststep.size(), 1);
        assertEquals(teststep.get(0).getName(), "my thread group");
    }

    @Test
    public void testAApplySteps() {
        CmdEventListener eventListener = new CmdEventListener("", "", "");
        TransactionControllerConverter testcontrol = new TransactionControllerConverter(new Converters(eventListener)
                , eventListener);

        TransactionController transactionController = Mockito.mock(TransactionController.class);
        when(transactionController.getName()).thenReturn("my thread group");
        when(transactionController.getComment()).thenReturn("My comment");
        HashTree hashTree = Mockito.mock(HashTree.class);

        ConstantTimer constantTimer = new ConstantTimer();
        List collections = new ArrayList();
        collections.add(constantTimer);
        collections.add(constantTimer);

        HashTree subTree = Mockito.mock(HashTree.class);
        when(subTree.list()).thenReturn(collections);
        when(hashTree.get(eq(transactionController))).thenReturn(subTree);

        List<Step> teststep = testcontrol.apply(transactionController, hashTree);
        List<Step> steps = ImmutableList.<Step>builder().addAll(teststep).addAll(teststep).addAll(teststep).build();
        assertEquals(teststep.size(), 1);
        assertEquals(teststep.get(0).getName(), "my thread group");


        int result = (int) teststep.stream().flatMap(Element::flattened)
                .filter(s -> !(s instanceof Container))
                .count();

        assertEquals(result, collections.size());

        System.out.println(teststep);
        //result.getSteps() == teststep
        // result.getSteps().stream() == teststep.stream()
        //.forEach(System.out::println);

        teststep.stream()
                .filter(s -> s instanceof Container)
                .flatMap(c -> ((Container) c).getSteps().stream())
                .forEach((System.out::println));
//                    if (step instanceof Delay) {
//                        Delay delay = (Delay) step;
//                        System.out.println("name=" + delay.getName());
//                        System.out.println("value=" + delay.getValue());
//                    }
//                });
//
//}

    }
}
