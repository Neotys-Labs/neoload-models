package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.StartAfterToStringConverter;
import com.neotys.neoload.model.converter.StringToStartAfterConverter;
import com.neotys.neoload.model.core.Composite;
import com.neotys.neoload.model.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.validation.constraints.CompositeCheck.startafter.message}")
@JsonSerialize(converter=StartAfterToStringConverter.class)
@JsonDeserialize(converter=StringToStartAfterConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
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
