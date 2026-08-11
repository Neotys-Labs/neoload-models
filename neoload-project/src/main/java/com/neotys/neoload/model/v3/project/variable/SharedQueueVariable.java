package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.Valid;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableSharedQueueVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, SharedQueueVariable.QUEUE_SIZE, SharedQueueVariable.CONSUMER_TIMEOUT,
		SharedQueueVariable.SWAP_FILE})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SharedQueueVariable extends Variable {

	String QUEUE_SIZE       = "queue_size";
	String CONSUMER_TIMEOUT = "consumer_timeout";
	String SWAP_FILE        = "swap_file";

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

	@Valid
	@JsonProperty(SWAP_FILE)
	Optional<SharedQueueSwapFile> getSwapFile();

	class Builder extends ImmutableSharedQueueVariable.Builder {}

	static Builder builder() {
		return new Builder();
	}
}
