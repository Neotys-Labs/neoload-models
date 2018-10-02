package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.DurationToStringConverter;
import com.neotys.neoload.model.converter.StringToDurationConverter;

@JsonSerialize(converter=DurationToStringConverter.class)
@JsonDeserialize(converter=StringToDurationConverter.class)
@Value.Immutable
public interface Duration extends Composite<Integer, Duration.Type> {
	enum Type {
		ITERATION,
		TIME
	}
}
