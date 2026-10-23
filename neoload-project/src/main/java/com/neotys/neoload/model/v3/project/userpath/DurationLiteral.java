package com.neotys.neoload.model.v3.project.userpath;

import java.time.Duration;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DurationLiteral extends DurationOrVariable {
	Duration getValue();

	class Builder extends ImmutableDurationLiteral.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
