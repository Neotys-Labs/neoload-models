package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

import static com.neotys.neoload.model.v3.project.variable.FileVariable.CHANGE_POLICY;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.COLUMN_NAMES;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.DELIMITER;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.IS_FIRST_LINE_COLUMN_NAMES;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.ORDER;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.OUT_OF_VALUE;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.PATH;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.SCOPE;
import static com.neotys.neoload.model.v3.project.variable.FileVariable.START_FROM_LINE;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableFileVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, COLUMN_NAMES, IS_FIRST_LINE_COLUMN_NAMES, START_FROM_LINE,
		DELIMITER, PATH, CHANGE_POLICY, SCOPE, ORDER, OUT_OF_VALUE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface FileVariable extends Variable {

	enum ChangePolicy {
		@JsonProperty("each_use")
		EACH_USE,
		@JsonProperty("each_request")
		EACH_REQUEST,
		@JsonProperty("each_page")
		EACH_PAGE,
		@JsonProperty("each_user")
		EACH_USER,
		@JsonProperty("each_iteration")
		EACH_ITERATION
	}

	enum Scope {
		@JsonProperty("unique")
		UNIQUE,
		@JsonProperty("global")
		GLOBAL,
		@JsonProperty("local")
		LOCAL
	}

	enum Order {
		@JsonProperty("sequential")
		SEQUENTIAL,
		@JsonProperty("random")
		RANDOM,
		@JsonProperty("any")
		ANY
	}

	enum OutOfValue {
		@JsonProperty("cycle")
		CYCLE,
		@JsonProperty("stop_test")
		STOP,
		@JsonProperty("no_value_code")
		NO_VALUE
	}

	String COLUMN_NAMES 				= "column_names";
	String IS_FIRST_LINE_COLUMN_NAMES 	= "is_first_line_column_names";
	String START_FROM_LINE 				= "start_from_line";
	String DELIMITER 					= "delimiter";
	String PATH 						= "path";
	String CHANGE_POLICY 				= "change_policy";
	String SCOPE 						= "scope";
	String ORDER 						= "order";
	String OUT_OF_VALUE 				= "out_of_value";

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

	@JsonProperty(CHANGE_POLICY)
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return ChangePolicy.EACH_ITERATION;
	}

	@JsonProperty(SCOPE)
	@Value.Default
	default Scope getScope() {
		return Scope.GLOBAL;
	}

	@JsonProperty(ORDER)
	@Value.Default
	default Order getOrder() {
		return Order.ANY;
	}

	@JsonProperty(OUT_OF_VALUE)
	@Value.Default
	default OutOfValue getOutOfValue() {
		return OutOfValue.CYCLE;
	}
}
