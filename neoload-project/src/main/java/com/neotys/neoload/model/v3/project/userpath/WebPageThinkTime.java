package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.WebPageThinkTimeDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.WebPageThinkTimeSerializer;

@JsonSerialize(using = WebPageThinkTimeSerializer.class)
@JsonDeserialize(using = WebPageThinkTimeDeserializer.class)
public interface WebPageThinkTime {
	// A plain number in seconds (integer or decimal), a time literal with optional h/m/s/ms
	// suffixed components (e.g. 500ms, 2s, 1h30m), or a variable reference (e.g. ${max_think_time}).
	String VALUE_PATTERN = "(\\d+(\\.\\d+)?|(\\d+(h|ms|m|s))+|\\$\\{[^}]+\\})";
}
