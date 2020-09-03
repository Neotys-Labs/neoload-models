package com.neotys.neoload.model.v3.project.scenario;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomLoadPolicyTest {
    @Test
    public void constants() {
        assertEquals("steps", CustomLoadPolicy.STEPS);
        assertEquals("rampup", CustomLoadPolicy.RAMPUP);
    }
}
