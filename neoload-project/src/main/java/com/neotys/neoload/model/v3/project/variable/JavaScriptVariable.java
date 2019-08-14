package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableJavaScriptVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, JavaScriptVariable.SCRIPT})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface JavaScriptVariable extends Variable {

	String SCRIPT = "script";

	@JsonProperty(SCRIPT)
	@RequiredCheck(groups = {NeoLoad.class})
	String getScript();

	class Builder extends ImmutableJavaScriptVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
