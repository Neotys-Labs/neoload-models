package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, JavaScript.SCRIPT})
@JsonDeserialize(as = ImmutableJavaScript.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface JavaScript extends Step {
	String SCRIPT = "script";

	@JsonProperty(SCRIPT)
	@RequiredCheck(groups = {NeoLoad.class})
	String getScript();

	class Builder extends ImmutableJavaScript.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
