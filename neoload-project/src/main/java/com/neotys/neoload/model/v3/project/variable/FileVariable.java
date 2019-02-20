package com.neotys.neoload.model.v3.project.variable;

import java.util.List;

import javax.validation.constraints.Size;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.FileVariableCheck;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@FileVariableCheck(groups={NeoLoad.class})
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableFileVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, FileVariable.COLUMN_NAMES, FileVariable.IS_FIRST_LINE_COLUMN_NAMES, FileVariable.START_FROM_LINE,
	FileVariable.DELIMITER, FileVariable.PATH, Variable.CHANGE_POLICY, Variable.SCOPE, Variable.ORDER, Variable.OUT_OF_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface FileVariable extends Variable {

	String COLUMN_NAMES 				= "column_names";
	String IS_FIRST_LINE_COLUMN_NAMES 	= "is_first_line_column_names";
	String START_FROM_LINE 				= "start_from_line";
	String DELIMITER 					= "delimiter";
	String PATH 						= "path";

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

	class Builder extends ImmutableFileVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
