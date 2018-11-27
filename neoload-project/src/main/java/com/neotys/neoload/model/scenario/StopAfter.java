package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.StopAfterToStringConverter;
import com.neotys.neoload.model.converter.StringToStopAfterConverter;
import com.neotys.neoload.model.core.Composite;
import com.neotys.neoload.model.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.validation.constraints.CompositeCheck.stopafter.message}")
@JsonSerialize(converter=StopAfterToStringConverter.class)
@JsonDeserialize(converter=StringToStopAfterConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface StopAfter extends Composite<Object, StopAfter.Type>{
	enum Type {
		CURRENT_ITERATION,
		TIME
	}
	
	class Builder extends ImmutableStopAfter.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
