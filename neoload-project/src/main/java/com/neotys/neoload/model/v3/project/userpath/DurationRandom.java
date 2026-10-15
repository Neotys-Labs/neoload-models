package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.DurationRandomMaxGreaterThanMinCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.constraints.Pattern;
import org.immutables.value.Value;

/**
 * The duration is drawn between {@code min} (optional, 0 when absent) and {@code max} (required),
 * {@code max} being strictly greater than {@code min}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
@DurationRandomMaxGreaterThanMinCheck(groups = {NeoLoad.class})
public interface DurationRandom extends Duration {
	String MIN = "min";
	String MAX = "max";

	Optional<@Pattern(regexp = Duration.PATTERN, groups = {NeoLoad.class}) String> getMin();

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = Duration.PATTERN, groups = {NeoLoad.class})
	String getMax();

	class Builder extends ImmutableDurationRandom.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
