package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WebPageThinkTimeConstant extends WebPageThinkTime {
	String VALUE = "value";

	@JsonProperty(VALUE)
	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = VALUE_PATTERN, groups = {NeoLoad.class})
	String getValue();

	class Builder extends ImmutableWebPageThinkTimeConstant.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
