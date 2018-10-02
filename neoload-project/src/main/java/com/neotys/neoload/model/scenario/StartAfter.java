package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.StartAfterToStringConverter;
import com.neotys.neoload.model.converter.StringToStartAfterConverter;

@JsonSerialize(converter=StartAfterToStringConverter.class)
@JsonDeserialize(converter=StringToStartAfterConverter.class)
@Value.Immutable
public interface StartAfter extends Composite<Object, StartAfter.Type> {
	enum Type {
		POPULATION,
		TIME
	}
}
