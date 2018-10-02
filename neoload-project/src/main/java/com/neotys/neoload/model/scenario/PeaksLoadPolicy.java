package com.neotys.neoload.model.scenario;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonInclude(value=Include.NON_ABSENT)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonPropertyOrder({"minimum", "maximum", "start", "duration", "start_after", "step_rampup", "stop_after"})
@JsonDeserialize(as = ImmutablePeaksLoadPolicy.class)
@Value.Immutable
public interface PeaksLoadPolicy extends LoadPolicy {
	enum Peak {
		@JsonProperty("minimum")
        MINIMUM,
        @JsonProperty("maximum")
        MAXIMUM
    }

    PeakLoadPolicy getMinimum();
    PeakLoadPolicy getMaximum();
    Peak getStart();
	@JsonProperty("step_rampup")
	Optional<Integer> getRampup();
}
