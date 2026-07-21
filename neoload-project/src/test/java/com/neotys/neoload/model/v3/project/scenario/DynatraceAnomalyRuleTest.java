package com.neotys.neoload.model.v3.project.scenario;

import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule.Operator;
import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule.Severity;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DynatraceAnomalyRuleTest {

    @Test
    public void constants() {
        assertEquals("metric_id", DynatraceAnomalyRule.METRIC_ID);
        assertEquals("operator", DynatraceAnomalyRule.OPERATOR);
        assertEquals("value", DynatraceAnomalyRule.VALUE);
        assertEquals("severity", DynatraceAnomalyRule.SEVERITY);
    }

    // Operator and Severity enum names are the accepted YAML vocabulary (validated via
    // @ValueOfEnumCheck on the String getOperator()/getSeverity()); pin the exact set so a
    // rename/removal that would break the external contract is caught.
    @Test
    public void operators() {
        assertArrayEquals(new Operator[]{Operator.ABOVE, Operator.BELOW}, Operator.values());
    }

    @Test
    public void severities() {
        assertArrayEquals(new Severity[]{
                Severity.AVAILABILITY,
                Severity.CUSTOM_ALERT,
                Severity.ERROR,
                Severity.INFO,
                Severity.PERFORMANCE,
                Severity.RESOURCE_CONTENTION
        }, Severity.values());
    }
}