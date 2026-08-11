package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.Size;
import org.immutables.value.Value;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({SharedQueueSwapFile.PATH, SharedQueueSwapFile.DELIMITER, SharedQueueSwapFile.LOAD_FROM_FILE, SharedQueueSwapFile.SAVE_TO_FILE})
@JsonSerialize(as = ImmutableSharedQueueSwapFile.class)
@JsonDeserialize(as = ImmutableSharedQueueSwapFile.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SharedQueueSwapFile {
	String PATH           = "path";
	String DELIMITER      = "delimiter";
	String LOAD_FROM_FILE = "load_from_file";
	String SAVE_TO_FILE   = "save_to_file";

	@RequiredCheck(groups = {NeoLoad.class})
	@JsonProperty(PATH)
	String getPath();

	@JsonProperty(DELIMITER)
	@Value.Default
	@Size(min = 1, max = 1, groups = {NeoLoad.class})
	default String getDelimiter() {
		return ";";
	}

	@JsonProperty(LOAD_FROM_FILE)
	@Value.Default
	default boolean isLoadFromFile() {
		return false;
	}

	@JsonProperty(SAVE_TO_FILE)
	@Value.Default
	default boolean isSaveToFile() {
		return true;
	}

	class Builder extends ImmutableSharedQueueSwapFile.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
