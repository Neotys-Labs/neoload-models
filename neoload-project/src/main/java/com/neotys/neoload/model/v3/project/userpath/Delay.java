package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Delay extends StepDuration {
	class Builder extends ImmutableDelay.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
