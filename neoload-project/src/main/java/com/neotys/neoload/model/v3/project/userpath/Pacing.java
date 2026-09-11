package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.PacingDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.PacingSerializer;

@JsonSerialize(using = PacingSerializer.class)
@JsonDeserialize(using = PacingDeserializer.class)
public interface Pacing {
	String PATTERN = "^(\\d+(\\.\\d+)?(h|ms|m|s)?|(\\d+(h|ms|m|s)){2,}|\\$\\{[^}]+\\})$";
}
