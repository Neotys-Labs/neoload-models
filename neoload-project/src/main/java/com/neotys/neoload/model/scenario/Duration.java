package com.neotys.neoload.model.scenario;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.converter.DurationToStringConverter;
import com.neotys.neoload.model.converter.StringToDurationConverter;
import com.neotys.neoload.model.core.Composite;
import com.neotys.neoload.model.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.validation.constraints.CompositeCheck.duration.message}")
@JsonSerialize(converter=DurationToStringConverter.class)
@JsonDeserialize(converter=StringToDurationConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Deprecated
public interface Duration extends Composite<Integer, Duration.Type> {
	enum Type {
		ITERATION,
		TIME
	}

	class Builder extends ImmutableDuration.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
