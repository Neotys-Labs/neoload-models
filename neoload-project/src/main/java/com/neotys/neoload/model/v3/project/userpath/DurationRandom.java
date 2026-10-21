package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.project.MinMaxRange;
import com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.math.BigDecimal;
import java.util.Optional;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

/**
 * The duration is drawn between {@code min} (optional, 0 when absent) and {@code max} (required),
 * {@code min} being lower than or equal to {@code max}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@MinNotGreaterThanMaxCheck(element = "duration", groups = {NeoLoad.class})
public interface DurationRandom extends Duration, MinMaxRange {
	String MIN = "min";
	String MAX = "max";

	Optional<@Pattern(regexp = Duration.PATTERN, message = Duration.PATTERN_MESSAGE, groups = {NeoLoad.class}) String> getMin();

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = Duration.PATTERN, message = Duration.PATTERN_MESSAGE, groups = {NeoLoad.class})
	String getMax();

	@Override
	default Optional<BigDecimal> comparableMin() {
		return getMin().map(DurationRandom::toMilliseconds).orElse(Optional.of(BigDecimal.ZERO));
	}

	@Override
	default Optional<BigDecimal> comparableMax() {
		return toMilliseconds(getMax());
	}

	private static Optional<BigDecimal> toMilliseconds(final String value) {
		if (value == null || value.isEmpty() || !value.chars().allMatch(c -> c >= '0' && c <= '9')) {
			return Optional.empty();
		}
		return Optional.of(new BigDecimal(value));
	}

	class Builder extends ImmutableDurationRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
