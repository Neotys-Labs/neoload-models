package com.neotys.neoload.model.v3.project.userpath;

import org.immutables.value.Value;

/**
 * Polymorphic duration step, used for both the {@code delay} and the {@code think_time} step keys
 * and for the web page {@code think_time} field. It is either a {@link DelayConstant} (fixed duration)
 * or a {@link DelayRandom} (random duration between a {@code min} and a {@code max}).
 *
 * <p>The two concrete forms are (de)serialized by {@code StepsSerializer} / {@code StepsDeserializer}
 * (and by the web page serializers), which pick the simplified scalar syntax or the expanded object
 * syntax (carrying {@code name} / {@code description}).
 */
public interface Delay extends Step {

	/**
	 * Default (implicit) names, used by the serializer to decide the simplified scalar syntax vs the
	 * expanded object. They also become the NeoLoad element name when none is given (converters need
	 * a meaningful name, not a sentinel).
	 */
	String DEFAULT_NAME = "delay";
	String DEFAULT_THINK_TIME_NAME = "think_time";

	/**
	 * Selects the step YAML key ({@code think_time} when true, {@code delay} otherwise) and mirrors
	 * NLGui {@code DelayAction.isThinkTime()}. It is never written to YAML.
	 */
	@Value.Default
	default boolean isThinkTime() {
		return false;
	}

	default String defaultName() {
		return isThinkTime() ? DEFAULT_THINK_TIME_NAME : DEFAULT_NAME;
	}
}
