package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * The duration is drawn between {@code min} (optional, 0 when absent) and {@code max} (required),
 * {@code min} being lower than or equal to {@code max}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@MinNotGreaterThanMaxCheck(element = "duration", groups = {NeoLoad.class})
public interface DurationRandom extends Duration {
	String MIN = "min";
	String MAX = "max";

	Optional<DurationOrVariable> getMin();

	@RequiredCheck(groups = {NeoLoad.class})
	DurationOrVariable getMax();

	class Builder extends ImmutableDurationRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
