package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.FileVariableCheck;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import javax.validation.constraints.Size;
import org.immutables.value.Value;

@FileVariableCheck(groups={NeoLoad.class})
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableFileVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, FileVariable.COLUMN_NAMES, FileVariable.IS_FIRST_LINE_COLUMN_NAMES, FileVariable.START_FROM_LINE,
	FileVariable.DELIMITER, FileVariable.PATH, ChangePolicyVariable.CHANGE_POLICY, ScopeVariable.SCOPE, FileVariable.ORDER, OutOfValueVariable.OUT_OF_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
// S2097 suppressed: DefaultOrderFilter overrides equals(Object) to compare the property value
// (not another filter instance), which is how the CUSTOM value filter selects the default value
// to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface FileVariable extends ChangePolicyVariable, ScopeVariable, OutOfValueVariable {

	String COLUMN_NAMES 				= "column_names";
	String IS_FIRST_LINE_COLUMN_NAMES 	= "is_first_line_column_names";
	String START_FROM_LINE 				= "start_from_line";
	String DELIMITER 					= "delimiter";
	String PATH 						= "path";
	String ORDER 						= "order";

	enum Order {
		@JsonProperty("sequential")
		SEQUENTIAL,
		@JsonProperty("random")
		RANDOM,
		@JsonProperty("any")
		ANY
	}

	@JsonProperty(COLUMN_NAMES)
	List<String> getColumnNames();

	@JsonProperty(IS_FIRST_LINE_COLUMN_NAMES)
	@Value.Default
	default boolean isFirstLineColumnNames() {
		return false;
	}

	@JsonProperty(START_FROM_LINE)
	@Value.Default
	@RangeCheck(min=1, groups={NeoLoad.class})
	default int getStartFromLine() {
		return 1;
	}

	@JsonProperty(DELIMITER)
	@Value.Default
	@Size(min = 1, max = 1, groups={NeoLoad.class})
	default String getDelimiter() {
		return ",";
	}

	@JsonProperty(PATH)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPath();

	// Written only when it differs from its default value.
	@JsonProperty(ORDER)
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = DefaultOrderFilter.class)
	@Value.Default
	default Order getOrder() {
		return Order.ANY;
	}

	class DefaultOrderFilter {
		@Override
		public boolean equals(final Object value) {
			return Order.ANY.equals(value);
		}

		@Override
		public int hashCode() {
			return Order.ANY.hashCode();
		}
	}

	class Builder extends ImmutableFileVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
