package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface PacingRandom extends Pacing {
	String MIN = "min";
	String MAX = "max";
	String DEFAULT_MIN = "0";

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = Pacing.PATTERN, groups = {NeoLoad.class})
	@Value.Default
	default String getMin() {
		return DEFAULT_MIN;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = Pacing.PATTERN, groups = {NeoLoad.class})
	String getMax();

	class Builder extends ImmutablePacingRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
