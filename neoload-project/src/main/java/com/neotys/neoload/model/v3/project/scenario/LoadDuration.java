package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.DurationToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToDurationConverter;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.constraints.SecondsDurationCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.duration.message}")
@JsonSerialize(converter=DurationToStringConverter.class)
@JsonDeserialize(converter=StringToDurationConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Gson.TypeAdapters
public interface LoadDuration extends Composite<Integer, LoadDuration.Type> {
	enum Type {
		ITERATION,
		TIME
	}

	@Value.Auxiliary
	Optional<@SecondsDurationCheck(groups={NeoLoad.class}) String> getInvalidText();

	class Builder extends ImmutableLoadDuration.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
