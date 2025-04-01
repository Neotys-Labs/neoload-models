package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface ThinkTime extends StepDuration {
	@Value.Default
	@Override
	default String getName() {
		return "ThinkTime";
	}

	class Builder extends ImmutableThinkTime.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
