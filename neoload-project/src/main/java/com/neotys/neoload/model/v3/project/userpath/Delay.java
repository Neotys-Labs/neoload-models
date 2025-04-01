package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Delay extends StepDuration {
	String DEFAULT_NAME = "delay";

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	class Builder extends ImmutableDelay.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
