package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.binding.converter.DurationGrammar;
import com.neotys.neoload.model.v3.binding.converter.Durations;
import com.neotys.neoload.model.v3.binding.converter.ParsedDuration;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DurationConstant extends Duration {
	String VALUE = "value";

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = Duration.PATTERN, message = Duration.PATTERN_MESSAGE, groups = {NeoLoad.class})
	String getValueInMsOrVariable();

	default ParsedDuration parseValue() {
		return Durations.parse(getValueInMsOrVariable(), DurationGrammar.MILLISECONDS);
	}

	class Builder extends ImmutableDurationConstant.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
