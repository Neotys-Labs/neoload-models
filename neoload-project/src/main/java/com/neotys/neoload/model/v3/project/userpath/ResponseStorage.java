package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * Where the response body of a request is stored: the file {@code path}, the {@code variable}
 * receiving the stored file path, and whether the file is removed when the test finishes.
 */
@JsonInclude(value = Include.NON_DEFAULT)
@JsonPropertyOrder({ResponseStorage.PATH, ResponseStorage.VARIABLE, ResponseStorage.DELETE_WHEN_TEST_FINISHED})
@JsonSerialize(as = ImmutableResponseStorage.class)
@JsonDeserialize(as = ImmutableResponseStorage.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@SuppressWarnings("java:S2097")
public interface ResponseStorage {
	String PATH = "path";
	String VARIABLE = "variable";
	String DELETE_WHEN_TEST_FINISHED = "delete_when_test_finished";

	String DEFAULT_VARIABLE = "responseFilePath";

	@JsonProperty(PATH)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPath();

	@JsonProperty(VARIABLE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultVariableFilter.class)
	@Value.Default
	default String getVariable() {
		return DEFAULT_VARIABLE;
	}

	@JsonProperty(DELETE_WHEN_TEST_FINISHED)
	@Value.Default
	default Boolean getDeleteWhenTestFinished() {
		return false;
	}

	class DefaultVariableFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_VARIABLE.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_VARIABLE.hashCode();
		}
	}

	class Builder extends ImmutableResponseStorage.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
