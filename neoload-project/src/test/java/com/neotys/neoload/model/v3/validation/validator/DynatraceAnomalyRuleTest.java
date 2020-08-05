package com.neotys.neoload.model.v3.validation.validator;

import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule;
import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule.Operator;
import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule.Severity;
import com.neotys.neoload.model.v3.project.scenario.ImmutableDynatraceAnomalyRule;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynatraceAnomalyRuleTest {

    private static final String ONE_VIOLATION = "Data Model is invalid. Violation Number: 1.";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String CONSTRAINTS_ANOMALY_RULE_METRIC_ID_NULL = ONE_VIOLATION + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'metric_id': missing value or value is empty." + LINE_SEPARATOR;

    private static final String CONSTRAINTS_ANOMALY_RULE_OPERATOR_NULL = ONE_VIOLATION + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'operator': missing value or value is empty." + LINE_SEPARATOR;

    private static final String CONSTRAINTS_ANOMALY_RULE_OPERATOR_BAD_ENUM_VALUE = ONE_VIOLATION + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'operator': must be one of: ABOVE, BELOW." + LINE_SEPARATOR;

    private static final String CONSTRAINTS_ANOMALY_RULE_VALUE_NULL = ONE_VIOLATION + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'value': missing value or value is empty." + LINE_SEPARATOR;

    private static final String CONSTRAINTS_ANOMALY_RULE_SEVERITY_NULL = ONE_VIOLATION + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'severity': missing value or value is empty." + LINE_SEPARATOR;

    private static final String CONSTRAINTS_ANOMALY_RULE_SEVERITY_BAD_ENUM_VALUE = ONE_VIOLATION + LINE_SEPARATOR +
            "Violation 1 - Incorrect value for 'severity': must be one of: AVAILABILITY, CUSTOM_ALERT, ERROR, INFO, PERFORMANCE, RESOURCE_CONTENTION." + LINE_SEPARATOR;

    @Test
    public void validateMetricId() {
        final Validator validator = new Validator();

        DynatraceAnomalyRule anomalyRule = DynatraceAnomalyRule.builder()
                .value("0")
                .operator(Operator.BELOW.name())
                .severity(Severity.AVAILABILITY.name())
                .build();
        Validation validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_METRIC_ID_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withMetricId(" ");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_METRIC_ID_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withMetricId("builtin:cpu.some.metric");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertTrue(validation.isValid());
        assertFalse(validation.getMessage().isPresent());
    }

    @Test
    public void validateOperator() {
        final Validator validator = new Validator();

        DynatraceAnomalyRule anomalyRule = DynatraceAnomalyRule.builder()
                .metricId("m")
                .value("0")
                .severity(Severity.AVAILABILITY.name())
                .build();
        Validation validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_OPERATOR_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withOperator("  ");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_OPERATOR_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withOperator("wrongOperator");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_OPERATOR_BAD_ENUM_VALUE, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withOperator(Operator.ABOVE.name());
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertTrue(validation.isValid());
        assertFalse(validation.getMessage().isPresent());
    }

    @Test
    public void validateValue() {
        final Validator validator = new Validator();

        DynatraceAnomalyRule anomalyRule = DynatraceAnomalyRule.builder()
                .metricId("some metric")
                .operator(Operator.BELOW.name())
                .severity(Severity.AVAILABILITY.name())
                .build();
        Validation validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_VALUE_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withValue("  ");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_VALUE_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withValue("123");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertTrue(validation.isValid());
        assertFalse(validation.getMessage().isPresent());
    }

    @Test
    public void validateSeverity() {
        final Validator validator = new Validator();

        DynatraceAnomalyRule anomalyRule = DynatraceAnomalyRule.builder()
                .metricId("m")
                .operator(Operator.BELOW.name())
                .value("0")
                .build();
        Validation validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_SEVERITY_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withSeverity("  ");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_SEVERITY_NULL, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withSeverity("wrongSeverity");
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertFalse(validation.isValid());
        assertEquals(CONSTRAINTS_ANOMALY_RULE_SEVERITY_BAD_ENUM_VALUE, validation.getMessage().get());

        anomalyRule = ImmutableDynatraceAnomalyRule.copyOf(anomalyRule).withSeverity(Severity.CUSTOM_ALERT.name());
        validation = validator.validate(anomalyRule, NeoLoad.class);
        assertTrue(validation.isValid());
        assertFalse(validation.getMessage().isPresent());
    }
}