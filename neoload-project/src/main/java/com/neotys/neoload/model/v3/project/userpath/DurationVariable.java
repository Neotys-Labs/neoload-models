package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

/**
 * A variable reference, as written in the YAML, for instance {@code ${name}}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DurationVariable extends DurationOrVariable {
	String getValue();

	class Builder extends ImmutableDurationVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
