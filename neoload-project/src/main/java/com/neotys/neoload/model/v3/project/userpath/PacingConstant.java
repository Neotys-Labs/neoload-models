package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface PacingConstant extends Pacing {
	String VALUE = "value";

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = Pacing.PATTERN, groups = {NeoLoad.class})
	String getValue();

	class Builder extends ImmutablePacingConstant.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
