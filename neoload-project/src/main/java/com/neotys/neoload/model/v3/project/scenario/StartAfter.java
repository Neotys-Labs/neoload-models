package com.neotys.neoload.model.v3.project.scenario;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StartAfterToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToStartAfterConverter;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.startafter.message}")
@JsonSerialize(converter=StartAfterToStringConverter.class)
@JsonDeserialize(converter=StringToStartAfterConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface StartAfter extends Composite<Object, StartAfter.Type> {
	enum Type {
		POPULATION,
		TIME
	}
	
	class Builder extends ImmutableStartAfter.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
