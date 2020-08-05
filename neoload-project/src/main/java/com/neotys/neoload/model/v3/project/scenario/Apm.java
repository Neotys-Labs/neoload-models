package com.neotys.neoload.model.v3.project.scenario;

import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonSerialize(as = ImmutableApm.class)
@JsonDeserialize(as = ImmutableApm.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Apm {
    String DYNATRACE_TAGS = "dynatrace_tags";
    String DYNATRACE_ANOMALY_RULES = "dynatrace_anomaly_rules";

    @JsonProperty(DYNATRACE_TAGS)
    List<String> getDynatraceTags();

    @JsonProperty(DYNATRACE_ANOMALY_RULES)
    @Valid
    List<DynatraceAnomalyRule> getDynatraceAnomalyRules();
    
    class Builder extends ImmutableApm.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
