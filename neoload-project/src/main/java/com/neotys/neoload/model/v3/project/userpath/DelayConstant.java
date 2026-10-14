package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

/**
 * A fixed-duration delay or think time. Carries a single {@code value}
 * (a duration in milliseconds or a {@code ${variable}}), plus the {@code name} / {@code description}
 * inherited from {@code Element}. Its default name depends on {@link Delay#isThinkTime()}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DelayConstant extends Delay {

	String VALUE = "value";

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return defaultName();
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	String getValue();

	class Builder extends ImmutableDelayConstant.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
