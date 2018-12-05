package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StopAfterToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToStopAfterConverter;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.stopafter.message}")
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
	static Builder builder() {
		return new Builder();
	}
}
