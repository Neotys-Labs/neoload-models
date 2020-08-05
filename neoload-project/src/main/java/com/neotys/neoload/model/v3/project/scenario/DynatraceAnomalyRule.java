package com.neotys.neoload.model.v3.project.scenario;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.ValueOfEnumCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonSerialize(as = ImmutableDynatraceAnomalyRule.class)
@JsonDeserialize(as = ImmutableDynatraceAnomalyRule.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DynatraceAnomalyRule {
    String METRIC_ID = "metric_id";
    String OPERATOR = "operator";
    String VALUE = "value";
    String SEVERITY = "severity";


    @JsonProperty(METRIC_ID)
    @RequiredCheck(groups = {NeoLoad.class})
    String getMetricId();

    enum Operator {
        ABOVE, BELOW
    }

    @JsonProperty(OPERATOR)
    @RequiredCheck(groups = {NeoLoad.class})
    @ValueOfEnumCheck(enumClass = Operator.class, groups = {NeoLoad.class})
    String getOperator();

    @JsonProperty(VALUE)
    @RequiredCheck(groups = {NeoLoad.class})
    String getValue();

    enum Severity {
        AVAILABILITY, CUSTOM_ALERT, ERROR, INFO, PERFORMANCE, RESOURCE_CONTENTION;
    }

    @JsonProperty(SEVERITY)
    @RequiredCheck(groups = {NeoLoad.class})
    @ValueOfEnumCheck(enumClass = Severity.class, groups = {NeoLoad.class})
    String getSeverity();
    
    class Builder extends ImmutableDynatraceAnomalyRule.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
