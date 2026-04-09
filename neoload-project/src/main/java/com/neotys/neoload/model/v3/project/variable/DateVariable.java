package com.neotys.neoload.model.v3.project.variable;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableDateVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, DateVariable.PATTERN, DateVariable.START_DATE, DateVariable.INC_TYPE, DateVariable.INC_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DateVariable extends Variable {

	String PATTERN = "pattern";
	String START_DATE = "start_date";
	String INC_TYPE = "inc_type";
	String INC_VALUE = "inc_value";

	enum IncType {
		@JsonProperty("second") SECOND,
		@JsonProperty("minute") MINUTE,
		@JsonProperty("hour") HOUR,
		@JsonProperty("day") DAY,
		@JsonProperty("month") MONTH,
		@JsonProperty("year") YEAR
	}

	@JsonProperty(PATTERN)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPattern();

	@JsonProperty(START_DATE)
	Optional<String> getStartDate();

	@JsonProperty(INC_TYPE)
	@Value.Default
	default IncType getIncType() {
		return IncType.DAY;
	}

	@JsonProperty(INC_VALUE)
	@Value.Default
	default int getIncValue() {
		return 1;
	}

	class Builder extends ImmutableDateVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}