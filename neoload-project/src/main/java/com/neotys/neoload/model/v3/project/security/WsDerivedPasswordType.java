package com.neotys.neoload.model.v3.project.security;

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
public interface WsDerivedPasswordType extends WsPasswordType {
	String DERIVED_KEY = "derived_key";
	String SALT = "salt";
	String ITERATION = "iteration";

	@JsonProperty(SALT)
	@RequiredCheck(groups = {NeoLoad.class})
	String getSalt();

	@JsonProperty(ITERATION)
	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "^(([1-9]\\d*)|(\\$\\{.+\\}))$", groups = {NeoLoad.class})
	String getIteration();

	class Builder extends ImmutableWsDerivedPasswordType.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
