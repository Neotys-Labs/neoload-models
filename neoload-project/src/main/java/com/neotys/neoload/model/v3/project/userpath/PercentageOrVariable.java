package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.PercentageOrVariableDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.PercentageOrVariableSerializer;

/**
 * An integer percentage value: either a {@link PercentageLiteral} or a {@link PercentageVariable}.
 * In YAML, the percent sign is optional when reading and always written.
 */
@JsonSerialize(using = PercentageOrVariableSerializer.class)
@JsonDeserialize(using = PercentageOrVariableDeserializer.class)
public interface PercentageOrVariable {
}
