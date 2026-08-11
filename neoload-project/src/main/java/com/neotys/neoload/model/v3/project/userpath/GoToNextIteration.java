package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonSerialize(as = ImmutableGoToNextIteration.class)
@JsonDeserialize(as = ImmutableGoToNextIteration.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface GoToNextIteration extends Step {

	// Serializes as a bare scalar ("go_to_next_iteration"), not a named object — name is never written to YAML/JSON.
	// DEFAULT_NAME exists only to satisfy Element.getName(); it is not user-configurable. See StepsSerializer.
	String DEFAULT_NAME = "go_to_next_iteration";

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	class Builder extends ImmutableGoToNextIteration.Builder {}

	static Builder builder() {
		return new Builder();
	}
}