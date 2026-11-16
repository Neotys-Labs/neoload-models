package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface PercentageLiteral extends PercentageOrVariable {
	int getValue();

	class Builder extends ImmutablePercentageLiteral.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
