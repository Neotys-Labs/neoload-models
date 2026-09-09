package com.neotys.neoload.model.v3.project.userpath;

import javax.validation.constraints.Pattern;

import org.immutables.value.Value;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * A fixed-duration delay (former {@code Delay}). Carries a single {@code value}
 * (a duration in milliseconds or a {@code ${variable}}), plus the {@code name} / {@code description}
 * inherited from {@code Element}. Its default name is {@link Delay#DEFAULT_NAME}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DelayConstant extends Delay {

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return Delay.DEFAULT_NAME;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	String getValue();

	class Builder extends ImmutableDelayConstant.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
