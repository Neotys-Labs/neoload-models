package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.DurationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.DurationSerializer;
import com.neotys.neoload.model.v3.validation.constraintvalidators.ElementNameValidator;

/**
 * A duration, shared by {@code pacing}, {@code think_time} and {@code delay}. It is either a
 * {@link DurationConstant} (fixed duration) or a {@link DurationRandom} (random duration between a
 * {@code min} and a {@code max}). Values are stored in milliseconds or as a {@code ${variable}}.
 * A variable is referenced as {@code ${name}} or {@code ${name.field}}, each part being a valid element name
 * (see {@link ElementNameValidator}) without {@code .} nor a closing brace.
 */
@JsonSerialize(using = DurationSerializer.class)
@JsonDeserialize(using = DurationDeserializer.class)
public interface Duration {
	String VARIABLE_NAME_PATTERN = "[^" + ElementNameValidator.FORBIDDEN_CHARS + ".}]+";
	String PATTERN = "(\\d{1,18}|\\$\\{" + VARIABLE_NAME_PATTERN + "(\\." + VARIABLE_NAME_PATTERN + ")*\\})";
	String PATTERN_MESSAGE = "{com.neotys.neoload.model.v3.validation.constraints.DurationPattern.message}";
}
