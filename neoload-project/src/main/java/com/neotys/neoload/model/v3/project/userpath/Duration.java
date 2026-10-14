package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.DurationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.DurationSerializer;

/**
 * A duration, shared by {@code pacing}, {@code think_time} and {@code delay}. It is either a
 * {@link DurationConstant} (fixed duration) or a {@link DurationRandom} (random duration between a
 * {@code min} and a {@code max}). Values are stored in milliseconds or as a {@code ${variable}}.
 */
@JsonSerialize(using = DurationSerializer.class)
@JsonDeserialize(using = DurationDeserializer.class)
public interface Duration {
	String PATTERN = "(\\d+|\\$\\{[^}]+\\})";
}
