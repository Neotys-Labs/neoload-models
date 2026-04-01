package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, DebugLogger.TEXT, DebugLogger.FILE})
@JsonDeserialize(as = ImmutableDebugLogger.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DebugLogger extends Step {

	String DEFAULT_NAME = "debug_logger";
	String DEFAULT_FILE = "logs/runTimeLog.txt";
	String TEXT = "text";
	String FILE = "file";

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(TEXT)
	@RequiredCheck(groups = {NeoLoad.class})
	String getText();

	@JsonProperty(FILE)
	@Value.Default
	default String getFile() {
		return DEFAULT_FILE;
	}

	class Builder extends ImmutableDebugLogger.Builder {}
	static Builder builder() {
		return new Builder();
	}
}