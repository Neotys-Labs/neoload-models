package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface GoToNextIteration extends Step {

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