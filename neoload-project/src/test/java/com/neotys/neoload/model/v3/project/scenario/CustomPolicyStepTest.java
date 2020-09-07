package com.neotys.neoload.model.v3.project.scenario;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomPolicyStepTest {
    @Test
    public void constants() {
        assertEquals("users", CustomPolicyStep.USERS);
        assertEquals("when", CustomPolicyStep.WHEN);
    }
}
