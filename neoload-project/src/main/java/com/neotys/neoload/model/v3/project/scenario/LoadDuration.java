package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.DurationToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToDurationConverter;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.OptionalLong;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.duration.message}")
@JsonSerialize(converter=DurationToStringConverter.class)
@JsonDeserialize(converter=StringToDurationConverter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@Gson.TypeAdapters
public interface LoadDuration extends Composite<Object, LoadDuration.Type> {
	enum Type {
		ITERATION,
		TIME
	}

	/**
	 * @return the number of seconds (TIME) or iterations (ITERATION), empty when the value is absent or an invalid duration.
	 */
	default OptionalLong amount() {
		final Object value = getValue();
		if (value instanceof Integer) {
			return OptionalLong.of((Integer) value);
		}
		if (value instanceof SecondsDuration && ((SecondsDuration) value).isLiteral()) {
			return OptionalLong.of(((SecondsDuration) value).getAmount());
		}
		return OptionalLong.empty();
	}

	class Builder extends ImmutableLoadDuration.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
