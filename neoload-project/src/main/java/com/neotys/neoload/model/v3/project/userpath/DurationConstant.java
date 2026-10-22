package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.project.duration.MillisecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.MillisecondsDurationCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DurationConstant extends Duration {
	String VALUE = "value";

	@RequiredCheck(groups = {NeoLoad.class})
	@MillisecondsDurationCheck(groups = {NeoLoad.class})
	MillisecondsDuration getValue();

	class Builder extends ImmutableDurationConstant.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
