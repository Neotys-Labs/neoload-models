package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import javax.validation.Valid;

public interface DurationAssertionElement {
	String DURATION_ASSERTION = "duration_assertion";

	@JsonProperty(DURATION_ASSERTION)
	@Valid
	Optional<DurationAssertion> getDurationAssertion();
}
