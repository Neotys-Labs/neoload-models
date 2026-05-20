package com.neotys.neoload.model.v3.project.variable;

import java.util.Optional;

import javax.validation.constraints.Size;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableSharedQueueVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, SharedQueueVariable.QUEUE_SIZE, SharedQueueVariable.CONSUMER_TIMEOUT,
		SharedQueueVariable.SWAP_ACTIVATED, SharedQueueVariable.SWAP_FILE, SharedQueueVariable.SWAP_LOADED,
		SharedQueueVariable.SWAP_DUMP, SharedQueueVariable.DELIMITER})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SharedQueueVariable extends Variable {

	String QUEUE_SIZE        = "queue_size";
	String CONSUMER_TIMEOUT  = "consumer_timeout";
	String SWAP_ACTIVATED    = "swap_activated";
	String SWAP_FILE         = "swap_file";
	String SWAP_LOADED       = "swap_loaded";
	String SWAP_DUMP         = "swap_dump";
	String DELIMITER         = "delimiter";

	@JsonProperty(QUEUE_SIZE)
	@Value.Default
	@RangeCheck(min = 1, groups = {NeoLoad.class})
	default int getQueueSize() {
		return 10000;
	}

	@JsonProperty(CONSUMER_TIMEOUT)
	@Value.Default
	@RangeCheck(min = 0, groups = {NeoLoad.class})
	default long getConsumerTimeout() {
		return 5000;
	}

	@JsonProperty(SWAP_ACTIVATED)
	@Value.Default
	default boolean isSwapActivated() {
		return false;
	}

	@JsonProperty(SWAP_FILE)
	Optional<String> getSwapFile();

	@JsonProperty(SWAP_LOADED)
	@Value.Default
	default boolean isSwapLoaded() {
		return false;
	}

	@JsonProperty(SWAP_DUMP)
	@Value.Default
	default boolean isSwapDump() {
		return true;
	}

	@JsonProperty(DELIMITER)
	@Value.Default
	@Size(min = 1, max = 1, groups = {NeoLoad.class})
	default String getDelimiter() {
		return ";";
	}

	class Builder extends ImmutableSharedQueueVariable.Builder {}

	static Builder builder() {
		return new Builder();
	}
}