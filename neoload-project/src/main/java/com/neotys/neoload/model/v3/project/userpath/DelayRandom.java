package com.neotys.neoload.model.v3.project.userpath;

import javax.validation.constraints.Pattern;

import org.immutables.value.Value;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * A random delay: the duration is drawn between {@code min} (optional, default {@code 0})
 * and {@code max} (required). Durations are stored in milliseconds or as a {@code ${variable}}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DelayRandom extends Delay {

	String MIN = "min";
	String MAX = "max";
	String DEFAULT_MIN = "0";

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return Delay.DEFAULT_NAME;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	@Value.Default
	default String getMin() {
		return DEFAULT_MIN;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	String getMax();

	class Builder extends ImmutableDelayRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
