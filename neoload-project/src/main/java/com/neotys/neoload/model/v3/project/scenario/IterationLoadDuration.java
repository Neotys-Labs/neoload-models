package com.neotys.neoload.model.v3.project.scenario;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface IterationLoadDuration extends LoadDuration {
	int getCount();

	@Override
	default Type getType() {
		return Type.ITERATION;
	}

	@Override
	default long getAmount() {
		return getCount();
	}

	class Builder extends ImmutableIterationLoadDuration.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
