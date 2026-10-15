package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.Valid;
import org.immutables.value.Value;

/**
 * Step used for both the {@code delay} and the {@code think_time} step keys. The wait itself is its
 * {@link Duration}; the step only adds the {@code name} / {@code description} inherited from
 * {@code Element}, written by {@code StepsSerializer} / {@code StepsDeserializer}.
 */
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface Delay extends Step {

	/**
	 * Default (implicit) names, used by the serializer to decide the simplified scalar syntax vs the
	 * expanded object. They also become the NeoLoad element name when none is given (converters need
	 * a meaningful name, not a sentinel).
	 */
	String DEFAULT_NAME = "delay";
	String DEFAULT_THINK_TIME_NAME = "think_time";

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return defaultName();
	}

	/**
	 * Selects the step YAML key ({@code think_time} when true, {@code delay} otherwise) and mirrors
	 * NLGui {@code DelayAction.isThinkTime()}. It is never written to YAML.
	 */
	@Value.Default
	default boolean isThinkTime() {
		return false;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	Duration getDuration();

	default String defaultName() {
		return isThinkTime() ? DEFAULT_THINK_TIME_NAME : DEFAULT_NAME;
	}

	class Builder extends ImmutableDelay.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
