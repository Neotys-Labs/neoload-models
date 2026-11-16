package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.DurationOrVariableDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.DurationOrVariableSerializer;

/**
 * A millisecond-based duration value: either a {@link DurationLiteral} or a {@link DurationVariable}.
 * A plain number in YAML is a number of milliseconds.
 */
@JsonSerialize(using = DurationOrVariableSerializer.class)
@JsonDeserialize(using = DurationOrVariableDeserializer.class)
public interface DurationOrVariable {
}
