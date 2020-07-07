package com.neotys.neoload.model.v3.project.scenario;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DynatraceAnomalyRuleTest {

    @Test
    public void constants() {
        assertEquals("metric_id", DynatraceAnomalyRule.METRIC_ID);
        assertEquals("operator", DynatraceAnomalyRule.OPERATOR);
        assertEquals("value", DynatraceAnomalyRule.VALUE);
        assertEquals("severity", DynatraceAnomalyRule.SEVERITY);
    }
}