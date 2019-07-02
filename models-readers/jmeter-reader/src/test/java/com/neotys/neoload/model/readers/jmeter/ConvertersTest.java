package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.threads.ThreadGroup;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConvertersTest {

    @Test
    public void testGetContainer() {
        List<Step> steps = new ArrayList<>();
        Container result = Converters.getContainer(steps);
        assertEquals(steps, result.getSteps());
        assertEquals("container", result.getName());
    }

    @Test
    public void testGetUserPathPolicy() {
        ThreadGroup threadGroup = Mockito.mock(ThreadGroup.class);
        Mockito.when(threadGroup.getName()).thenReturn("my thread group");
        Mockito.when(threadGroup.getComment()).thenReturn("My comment");
        System.out.println(Converters.getUserPathPolicy(threadGroup));

    }

}