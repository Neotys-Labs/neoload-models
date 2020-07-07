package com.neotys.neoload.model.v3.project.scenario;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApmTest {

    @Test
    public void constants() {
        assertEquals("dynatrace_tags", Apm.DYNATRACE_TAGS);
        assertEquals("dynatrace_anomaly_rules", Apm.DYNATRACE_ANOMALY_RULES);
    }
}