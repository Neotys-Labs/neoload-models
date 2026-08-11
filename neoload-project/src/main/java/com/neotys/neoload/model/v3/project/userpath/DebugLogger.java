package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, DebugLogger.TEXT, DebugLogger.FILE})
@JsonSerialize(as = ImmutableDebugLogger.class)
@JsonDeserialize(as = ImmutableDebugLogger.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface DebugLogger extends Step {

	String DEFAULT_NAME = "debug_logger";
	String DEFAULT_FILE = "logs/runTimeLog.txt";
	String TEXT = "text";
	String FILE = "file";

	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(TEXT)
	@RequiredCheck(groups = {NeoLoad.class})
	String getText();

	@JsonProperty(FILE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultFileFilter.class)
	@Value.Default
	default String getFile() {
		return DEFAULT_FILE;
	}

	// Excludes the default name from serialization: the property is omitted when the filter's
	// equals(value) returns true.
	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	// Excludes the default file path from serialization: the property is omitted when the filter's
	// equals(value) returns true.
	class DefaultFileFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_FILE.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_FILE.hashCode();
		}
	}

	class Builder extends ImmutableDebugLogger.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
