package com.neotys.neoload.model.v3.project.scenario;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.DurationToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToDurationConverter;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.duration.message}")
@JsonSerialize(converter=DurationToStringConverter.class)
@JsonDeserialize(converter=StringToDurationConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface LoadDuration extends Composite<Integer, LoadDuration.Type> {
	enum Type {
		ITERATION,
		TIME
	}

	class Builder extends ImmutableLoadDuration.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
