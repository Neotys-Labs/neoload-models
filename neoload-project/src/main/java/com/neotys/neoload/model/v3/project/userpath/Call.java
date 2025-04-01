package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonDeserialize(as = ImmutableCall.class)
@Value.Style(validationMethod = ValidationMethod.NONE)
@Value.Immutable
public interface Call extends Step{
	@Value.Default
	@Override
	default String getName() {
		return "Call";
	}

	@JsonValue
	@RequiredCheck(groups = {NeoLoad.class})
	String getValue();
}
