package com.neotys.neoload.model.scenario;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.StopAfterToStringConverter;
import com.neotys.neoload.model.converter.StringToStopAfterConverter;

@JsonSerialize(converter=StopAfterToStringConverter.class)
@JsonDeserialize(converter=StringToStopAfterConverter.class)
@Value.Immutable
public interface StopAfter extends Composite<Optional<Object>, StopAfter.Type>{
	enum Type {
		CURRENT_ITERATION,
		TIME
	}
}
