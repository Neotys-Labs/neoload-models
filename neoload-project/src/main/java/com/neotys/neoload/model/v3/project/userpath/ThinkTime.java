package com.neotys.neoload.model.v3.project.userpath;

/**
 * Polymorphic {@code think_time} step. It is either a {@link ThinkTimeConstant} (fixed duration)
 * or a {@link ThinkTimeRandom} (random duration between a {@code min} and a {@code max}).
 *
 * <p>This is a marker interface: the two concrete forms are (de)serialized by
 * {@code StepsSerializer} / {@code StepsDeserializer}, which pick the simplified scalar syntax
 * or the expanded object syntax (carrying {@code name} / {@code description}).
 */
public interface ThinkTime extends Step {

	// Default (implicit) name of a think_time. Shared by ThinkTimeConstant / ThinkTimeRandom and used
	// by the serializer to decide the simplified scalar syntax vs the expanded object. It also becomes
	// the NeoLoad element name when none is given (converters need a meaningful name, not a sentinel).
	String DEFAULT_NAME = "think_time";
}
