package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

/**
 * A random delay or think time: the duration is drawn between {@code min} (optional)
 * and {@code max} (required). Durations are stored in milliseconds or as a {@code ${variable}}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface DelayRandom extends Delay {

	String MIN = "min";
	String MAX = "max";

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return defaultName();
	}

	Optional<@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class}) String> getMin();

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	String getMax();

	class Builder extends ImmutableDelayRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
